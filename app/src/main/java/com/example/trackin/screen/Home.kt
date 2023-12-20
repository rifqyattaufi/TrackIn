package com.example.trackin.screen

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.List
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.trackin.PreferencesManager
import com.example.trackin.respond.ApiResponse
import com.example.trackin.respond.date_and_time_respond
import com.example.trackin.respond.date_and_times
import com.example.trackin.service.date_and_time_service
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.Date


@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun Home(
    baseUrl: String,
    context: Context = LocalContext.current,
    innerPadding: PaddingValues
) {
    val listSchedule = remember { mutableStateListOf<date_and_times>() }
    val preferencesManager = remember { PreferencesManager(context) }
    val retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(date_and_time_service::class.java)
    val dayOfTheWeek: String = SimpleDateFormat("EEEE").format(Date())
    val call = retrofit.getData(
        day = dayOfTheWeek,
        id = preferencesManager.getData("id")
    )
    call.enqueue(
        object : Callback<ApiResponse<List<date_and_times>>> {
            override fun onResponse(
                call: Call<ApiResponse<List<date_and_times>>>,
                response: Response<ApiResponse<List<date_and_times>>>
            ) {
                if (response.isSuccessful()) {
                    listSchedule.clear()
                    response.body()?.data!!.forEach { schedule ->
                        listSchedule.add(schedule)
                    }
                } else {
                    try {
                        val jObjError =
                            JSONObject(response.errorBody()!!.string())
                        Toast.makeText(
                            context,
                            jObjError.getJSONObject("error")
                                .getString("message"),
                            Toast.LENGTH_LONG
                        ).show()
                    } catch (e: Exception) {
                        Toast.makeText(
                            context,
                            e.message,
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }

            override fun onFailure(call: Call<ApiResponse<List<date_and_times>>>, t: Throwable) {
                Toast.makeText(
                    context,
                    t.message,
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                bottom = innerPadding.calculateBottomPadding() + 18.dp,
                top = innerPadding.calculateTopPadding(),
                start = 18.dp,
                end = 18.dp
            )
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        stickyHeader {
            Column(
                modifier = Modifier
                    .clip(shape = RoundedCornerShape(25.dp))
                    .background(MaterialTheme.colorScheme.primaryContainer)
                    .padding(vertical = 7.dp, horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                Text("Today Schedule", fontSize = 16.sp)
            }
        }
        item {
            Column(
                modifier = Modifier
                    .clip(shape = RoundedCornerShape(25.dp))
                    .background(MaterialTheme.colorScheme.primaryContainer)
                    .fillMaxSize()
                    .padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(shape = RoundedCornerShape(25.dp))
                        .background(MaterialTheme.colorScheme.background)
                        .padding(15.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    val colorBorder = MaterialTheme.colorScheme.onBackground
                    listSchedule.forEach { schedule ->
                        Column(
                            modifier = Modifier
                                .drawBehind {
                                    val borderSize = 2.dp.toPx()
                                    drawLine(
                                        color = colorBorder,
                                        start = Offset(0f, size.height),
                                        end = Offset(size.width, size.height),
                                        strokeWidth = borderSize
                                    )
                                }
                        ) {
                            val timeStart = schedule.attributes?.start?.split(":")
                            val timeEnd = schedule.attributes?.end?.split(":")
                            Text(text = schedule.attributes?.schedule?.data?.attributes?.title!!)
                            Text(
                                text = "${timeStart!![0]}:${timeStart[1]}-${timeEnd!![0]}:${timeEnd[1]}",
                                textAlign = TextAlign.End,
                                modifier = Modifier.fillMaxWidth()
                            )
                            Text(text = "Class: ${schedule.attributes?.schedule?.data?.attributes?.room}")
                        }
                    }
                }
            }
        }
        stickyHeader {
            Column(
                modifier = Modifier
                    .clip(shape = RoundedCornerShape(25.dp))
                    .background(MaterialTheme.colorScheme.primaryContainer)
                    .padding(vertical = 7.dp, horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                Text("Today Task", fontSize = 16.sp)
            }
        }
        item {
            Column(
                modifier = Modifier
                    .clip(shape = RoundedCornerShape(25.dp))
                    .background(MaterialTheme.colorScheme.primaryContainer)
                    .fillMaxSize()
                    .padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(shape = RoundedCornerShape(25.dp))
                        .background(MaterialTheme.colorScheme.background)
                        .padding(15.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    val colorBorder = MaterialTheme.colorScheme.onBackground
                    Column(
                        modifier = Modifier
                            .drawBehind {
                                val borderSize = 2.dp.toPx()
                                drawLine(
                                    color = colorBorder,
                                    start = Offset(0f, size.height),
                                    end = Offset(size.width, size.height),
                                    strokeWidth = borderSize
                                )
                            }
                    ) {
                        Text(text = "Verifikasi dan Validasi")
                        Text(
                            text = "10.00-13.00",
                            textAlign = TextAlign.End,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Text(text = "Class: 2.06")
                    }
                    Column(
                        modifier = Modifier
                            .drawBehind {
                                val borderSize = 2.dp.toPx()
                                drawLine(
                                    color = colorBorder,
                                    start = Offset(0f, size.height),
                                    end = Offset(size.width, size.height),
                                    strokeWidth = borderSize
                                )
                            }
                    ) {
                        Text(text = "Verifikasi dan Validasi")
                        Text(
                            text = "10.00-13.00",
                            textAlign = TextAlign.End,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Text(text = "Class: 2.06")
                    }

                }
            }
        }
    }
}
