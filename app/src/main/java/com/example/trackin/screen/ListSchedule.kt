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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Edit
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.trackin.respond.ApiResponse
import com.example.trackin.respond.date_and_times
import com.example.trackin.service.date_and_time_service
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ListSchedule(
    baseUrl: String,
    innerPadding: PaddingValues,
    navController: NavHostController,
    context: Context = LocalContext.current
) {
    val borderColor = MaterialTheme.colorScheme.primary
    val borderColorSecondary = MaterialTheme.colorScheme.secondary
    val sharedPreferences: SharedPreferences =
        LocalContext.current.getSharedPreferences("auth", Context.MODE_PRIVATE)

    val listMonday = remember { mutableStateListOf<date_and_times>() }
    val listTuesday = remember { mutableStateListOf<date_and_times>() }
    val listWednesday = remember { mutableStateListOf<date_and_times>() }
    val listThursday = remember { mutableStateListOf<date_and_times>() }
    val listFriday = remember { mutableStateListOf<date_and_times>() }
    val listSaturday = remember { mutableStateListOf<date_and_times>() }
    val listSunday = remember { mutableStateListOf<date_and_times>() }

    val retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(date_and_time_service::class.java)
    val call = retrofit.getListSchedule(
        id = sharedPreferences.getString("id", "")!!,
    )
    call.enqueue(
        object : Callback<ApiResponse<List<date_and_times>>> {
            override fun onResponse(
                call: Call<ApiResponse<List<date_and_times>>>,
                response: Response<ApiResponse<List<date_and_times>>>
            ) {
                if (response.isSuccessful) {
                    listMonday.clear()
                    listTuesday.clear()
                    listWednesday.clear()
                    listThursday.clear()
                    listFriday.clear()
                    listSaturday.clear()
                    listSunday.clear()
                    response.body()?.data!!.forEach { schedule ->
                        when (schedule.attributes?.day) {
                            "Monday" -> {
                                listMonday.add(schedule)
                            }

                            "Tuesday" -> {
                                listTuesday.add(schedule)
                            }

                            "Wednesday" -> {
                                listWednesday.add(schedule)
                            }

                            "Thursday" -> {
                                listThursday.add(schedule)
                            }

                            "Friday" -> {
                                listFriday.add(schedule)
                            }

                            "Saturday" -> {
                                listSaturday.add(schedule)
                            }

                            "Sunday" -> {
                                listSunday.add(schedule)
                            }
                        }
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

    Column(
        modifier = Modifier.padding(innerPadding),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
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
                            color = borderColorSecondary,
                            start = Offset(x = 0f, y = size.height),
                            end = Offset(x = size.width, y = size.height),
                            strokeWidth = borderSize
                        )
                    }
                    .clickable(onClick = {
                        navController.navigate("ListTask")
                    }),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Task")
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    start = 18.dp,
                    end = 18.dp,
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            //monday
            if (listMonday.isNotEmpty()) {
                stickyHeader {
                    Column(
                        modifier = Modifier
                            .clip(shape = RoundedCornerShape(25.dp))
                            .background(MaterialTheme.colorScheme.primaryContainer)
                            .padding(vertical = 7.dp, horizontal = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                    ) {
                        Text("Monday", fontSize = 16.sp)
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
                            listMonday.forEach { schedule ->
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
                                        Text(text = schedule.attributes?.schedule?.data?.attributes?.title!!)
                                        val timeStart = schedule.attributes?.start?.split(":")
                                        val timeEnd = schedule.attributes?.end?.split(":")
                                        Text(
                                            text = "${timeStart?.get(0)}.${timeStart?.get(1)}-${
                                                timeEnd?.get(
                                                    0
                                                )
                                            }.${timeEnd?.get(1)}"
                                        )
                                        Text(text = "Class: ${schedule.attributes?.schedule?.data?.attributes?.room}")
                                    }
                                    Row {
                                        IconButton(onClick = {
                                            navController.navigate("DetailSchedule/${schedule.attributes?.schedule?.data?.id}")
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
            //tuesday
            if (listTuesday.isNotEmpty()) {
                stickyHeader {
                    Column(
                        modifier = Modifier
                            .clip(shape = RoundedCornerShape(25.dp))
                            .background(MaterialTheme.colorScheme.primaryContainer)
                            .padding(vertical = 7.dp, horizontal = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                    ) {
                        Text("Tuesday", fontSize = 16.sp)
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
                            listTuesday.forEach { schedule ->
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
                                        Text(text = schedule.attributes?.schedule?.data?.attributes?.title!!)
                                        val timeStart = schedule.attributes?.start?.split(":")
                                        val timeEnd = schedule.attributes?.end?.split(":")
                                        Text(
                                            text = "${timeStart?.get(0)}.${timeStart?.get(1)}-${
                                                timeEnd?.get(
                                                    0
                                                )
                                            }.${timeEnd?.get(1)}"
                                        )
                                        Text(text = "Class: ${schedule.attributes?.schedule?.data?.attributes?.room}")
                                    }
                                    Row {
                                        IconButton(onClick = {
                                            navController.navigate("DetailSchedule/${schedule.attributes?.schedule?.data?.id}")
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
            //wednesday
            if (listWednesday.isNotEmpty()) {
                stickyHeader {
                    Column(
                        modifier = Modifier
                            .clip(shape = RoundedCornerShape(25.dp))
                            .background(MaterialTheme.colorScheme.primaryContainer)
                            .padding(vertical = 7.dp, horizontal = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                    ) {
                        Text("Wednesday", fontSize = 16.sp)
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
                            listWednesday.forEach { schedule ->
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
                                        Text(text = schedule.attributes?.schedule?.data?.attributes?.title!!)
                                        val timeStart = schedule.attributes?.start?.split(":")
                                        val timeEnd = schedule.attributes?.end?.split(":")
                                        Text(
                                            text = "${timeStart?.get(0)}.${timeStart?.get(1)}-${
                                                timeEnd?.get(
                                                    0
                                                )
                                            }.${timeEnd?.get(1)}"
                                        )
                                        Text(text = "Class: ${schedule.attributes?.schedule?.data?.attributes?.room}")
                                    }
                                    Row {
                                        IconButton(onClick = {
                                            navController.navigate("DetailSchedule/${schedule.attributes?.schedule?.data?.id}")
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
            //thursday
            if (listThursday.isNotEmpty()) {
                stickyHeader {
                    Column(
                        modifier = Modifier
                            .clip(shape = RoundedCornerShape(25.dp))
                            .background(MaterialTheme.colorScheme.primaryContainer)
                            .padding(vertical = 7.dp, horizontal = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                    ) {
                        Text("Thursday", fontSize = 16.sp)
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
                            listThursday.forEach { schedule ->
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
                                        Text(text = schedule.attributes?.schedule?.data?.attributes?.title!!)
                                        val timeStart = schedule.attributes?.start?.split(":")
                                        val timeEnd = schedule.attributes?.end?.split(":")
                                        Text(
                                            text = "${timeStart?.get(0)}.${timeStart?.get(1)}-${
                                                timeEnd?.get(
                                                    0
                                                )
                                            }.${timeEnd?.get(1)}"
                                        )
                                        Text(text = "Class: ${schedule.attributes?.schedule?.data?.attributes?.room}")
                                    }
                                    Row {
                                        IconButton(onClick = {
                                            navController.navigate("DetailSchedule/${schedule.attributes?.schedule?.data?.id}")
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
            //friday
            if (listFriday.isNotEmpty()) {
                stickyHeader {
                    Column(
                        modifier = Modifier
                            .clip(shape = RoundedCornerShape(25.dp))
                            .background(MaterialTheme.colorScheme.primaryContainer)
                            .padding(vertical = 7.dp, horizontal = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                    ) {
                        Text("Friday", fontSize = 16.sp)
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
                            listFriday.forEach { schedule ->
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
                                        Text(text = schedule.attributes?.schedule?.data?.attributes?.title!!)
                                        val timeStart = schedule.attributes?.start?.split(":")
                                        val timeEnd = schedule.attributes?.end?.split(":")
                                        Text(
                                            text = "${timeStart?.get(0)}.${timeStart?.get(1)}-${
                                                timeEnd?.get(
                                                    0
                                                )
                                            }.${timeEnd?.get(1)}"
                                        )
                                        Text(text = "Class: ${schedule.attributes?.schedule?.data?.attributes?.room}")
                                    }
                                    Row {
                                        IconButton(onClick = {
                                            navController.navigate("DetailSchedule/${schedule.attributes?.schedule?.data?.id}")
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
            //saturday
            if (listSaturday.isNotEmpty()) {
                stickyHeader {
                    Column(
                        modifier = Modifier
                            .clip(shape = RoundedCornerShape(25.dp))
                            .background(MaterialTheme.colorScheme.primaryContainer)
                            .padding(vertical = 7.dp, horizontal = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                    ) {
                        Text("Saturday", fontSize = 16.sp)
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
                            listSaturday.forEach { schedule ->
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
                                        Text(text = schedule.attributes?.schedule?.data?.attributes?.title!!)
                                        val timeStart = schedule.attributes?.start?.split(":")
                                        val timeEnd = schedule.attributes?.end?.split(":")
                                        Text(
                                            text = "${timeStart?.get(0)}.${timeStart?.get(1)}-${
                                                timeEnd?.get(
                                                    0
                                                )
                                            }.${timeEnd?.get(1)}"
                                        )
                                        Text(text = "Class: ${schedule.attributes?.schedule?.data?.attributes?.room}")
                                    }
                                    Row {
                                        IconButton(onClick = {
                                            navController.navigate("DetailSchedule/${schedule.attributes?.schedule?.data?.id}")
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
            //sunday
            if (listSunday.isNotEmpty()) {
                stickyHeader {
                    Column(
                        modifier = Modifier
                            .clip(shape = RoundedCornerShape(25.dp))
                            .background(MaterialTheme.colorScheme.primaryContainer)
                            .padding(vertical = 7.dp, horizontal = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                    ) {
                        Text("Sunday", fontSize = 16.sp)
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
                            listSunday.forEach { schedule ->
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
                                        Text(text = schedule.attributes?.schedule?.data?.attributes?.title!!)
                                        val timeStart = schedule.attributes?.start?.split(":")
                                        val timeEnd = schedule.attributes?.end?.split(":")
                                        Text(
                                            text = "${timeStart?.get(0)}.${timeStart?.get(1)}-${
                                                timeEnd?.get(
                                                    0
                                                )
                                            }.${timeEnd?.get(1)}"
                                        )
                                        Text(text = "Class: ${schedule.attributes?.schedule?.data?.attributes?.room}")
                                    }
                                    Row {
                                        IconButton(onClick = {
                                            navController.navigate("DetailSchedule/${schedule.attributes?.schedule?.data?.id}")
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