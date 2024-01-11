package com.example.trackin.screen

import android.content.Context
import android.content.SharedPreferences
import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.text
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.trackin.data.TaskData
import com.example.trackin.data.TaskDataWrapper
import com.example.trackin.respond.ApiResponse
import com.example.trackin.respond.schedules
import com.example.trackin.respond.tasks
import com.example.trackin.service.TaskService
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.Date

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ListTask(
    baseUrl: String,
    innerPadding: PaddingValues,
    navController: NavHostController,
    sharedPreferences: SharedPreferences,
    context: Context = LocalContext.current
) {
    val listSchedule = remember { mutableStateListOf<schedules>() }
    val retrofitGetSchedule = Retrofit
        .Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(TaskService::class.java)

    fun getData() {
        val callGetSchedule = retrofitGetSchedule.getAllTask(
            id = sharedPreferences.getString("id", "")
        )
        callGetSchedule.enqueue(
            object : Callback<ApiResponse<List<schedules>>> {
                override fun onResponse(
                    call: Call<ApiResponse<List<schedules>>>,
                    response: Response<ApiResponse<List<schedules>>>
                ) {
                    if (response.isSuccessful) {
                        listSchedule.clear()
                        response.body()?.data?.let {
                            listSchedule.addAll(it)
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

                override fun onFailure(call: Call<ApiResponse<List<schedules>>>, t: Throwable) {
                    Toast.makeText(context, t.message, Toast.LENGTH_SHORT).show()
                }

            }
        )
    }

    getData()

    val borderColor = MaterialTheme.colorScheme.primary
    val borderColorSecondary = MaterialTheme.colorScheme.secondary
    Column(
        modifier = Modifier.padding(innerPadding),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (listSchedule.isEmpty()) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.width(64.dp),
                    color = MaterialTheme.colorScheme.primary,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant,
                )
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .drawBehind {
                        val borderSize = 4.dp.toPx()
                        drawLine(
                            color = borderColorSecondary,
                            start = Offset(x = 0f, y = size.height),
                            end = Offset(x = size.width, y = size.height),
                            strokeWidth = borderSize
                        )
                    }
                    .clickable(onClick = {
                        navController.navigate("ListSchedule")
                    })
                    .semantics {
                        text = AnnotatedString("ListSchedule")
                    },
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(text = "Schedule")
            }
            Column(
                modifier = Modifier
                    .weight(1f)
                    .drawBehind {
                        val borderSize = 4.dp.toPx()
                        drawLine(
                            color = borderColor,
                            start = Offset(x = 0f, y = size.height),
                            end = Offset(x = size.width, y = size.height),
                            strokeWidth = borderSize
                        )
                    },
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Task")
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 18.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            listSchedule.forEach {
                stickyHeader {
                    Column(
                        modifier = Modifier
                            .clip(shape = RoundedCornerShape(25.dp))
                            .background(MaterialTheme.colorScheme.primaryContainer)
                            .padding(vertical = 7.dp, horizontal = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                    ) {
                        Text(text = it.attributes?.title!!)
                    }
                }
                item {
                    val scheduleId = it.id
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
                            it.attributes?.tasks?.data!!.forEach {
                                Row(
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
                                        .fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Column(
                                        modifier = Modifier
                                            .weight(1f)
                                    ) {
                                        Text(text = it.attributes?.title!!)
                                        Text(
                                            text = "Deadline: " + SimpleDateFormat(
                                                "dd MMMM yyyy HH:mm"
                                            ).format(
                                                SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parse(
                                                    it.attributes?.deadline!!
                                                )!!
                                            ),
                                        )
                                        Text(
                                            text = "Status: " +
                                                    if (it.attributes?.status!!) {
                                                        "Done"
                                                    } else if (it.attributes?.deadline!! < SimpleDateFormat(
                                                            "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
                                                        ).format(Date())
                                                    ) {
                                                        "OVERDUE"
                                                    } else {
                                                        "Not Done"
                                                    }
                                        )
                                    }
                                    Row {
                                        if (!it.attributes?.status!!) {
                                            IconButton(onClick = {
                                                val retrofitCheckTask = Retrofit
                                                    .Builder()
                                                    .baseUrl(baseUrl)
                                                    .addConverterFactory(GsonConverterFactory.create())
                                                    .build()
                                                    .create(TaskService::class.java)
                                                val callCheckTask = retrofitCheckTask.checkTask(
                                                    id = it.id!!.toString(),
                                                    task = TaskDataWrapper(
                                                        TaskData(
                                                            status = true
                                                        )
                                                    )
                                                )
                                                callCheckTask.enqueue(
                                                    object : Callback<ApiResponse<tasks>> {
                                                        override fun onResponse(
                                                            call: Call<ApiResponse<tasks>>,
                                                            response: Response<ApiResponse<tasks>>
                                                        ) {
                                                            if (response.isSuccessful) {
                                                                Toast.makeText(
                                                                    context,
                                                                    "Success",
                                                                    Toast.LENGTH_SHORT
                                                                ).show()
                                                                getData()
                                                            } else {
                                                                try {
                                                                    val jObjError =
                                                                        JSONObject(
                                                                            response.errorBody()!!
                                                                                .string()
                                                                        )
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

                                                        override fun onFailure(
                                                            call: Call<ApiResponse<tasks>>,
                                                            t: Throwable
                                                        ) {
                                                            Toast.makeText(
                                                                context,
                                                                t.message,
                                                                Toast.LENGTH_SHORT
                                                            ).show()
                                                        }

                                                    }
                                                )
                                            }) {
                                                Icon(
                                                    Icons.Outlined.Check,
                                                    contentDescription = "Edit",
                                                    tint = Color.Green
                                                )
                                            }
                                        }
                                        IconButton(onClick = {
                                            navController.navigate("EditTask/${it.id}/${scheduleId}/${it.attributes?.title}/${it.attributes?.deadline}/${it.attributes?.status}")
                                        }) {
                                            Icon(
                                                Icons.Outlined.Edit,
                                                contentDescription = "Edit",
                                                tint = MaterialTheme.colorScheme.primary
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}