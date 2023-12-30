package com.example.trackin.screen

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import androidx.navigation.NavController
import com.example.trackin.respond.ApiResponse
import com.example.trackin.respond.date_and_times
import com.example.trackin.respond.schedules
import com.example.trackin.service.DateAndTimeService
import com.example.trackin.service.ScheduleService
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailSchedule(
    baseUrl: String,
    id: String,
    innerPadding: PaddingValues,
    context: Context = LocalContext.current,
    navController: NavController
) {
    var openAlertSchedule by remember { mutableStateOf(false) }

    val parser = SimpleDateFormat("HH:mm")
    val formatter = SimpleDateFormat("HH:mm")
    val formatterInput = SimpleDateFormat("HH:mm:ss")

    var start = remember { mutableStateListOf<String>() }
    var end = remember { mutableStateListOf<String>() }

    val daysList =
        listOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday")
    var title by remember { mutableStateOf("") }
    var room by remember { mutableStateOf("") }
    val dateAndTimes = remember { mutableStateListOf<date_and_times>() }
    val retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(ScheduleService::class.java)
    val call = retrofit.detailSchedule(id)
    call.enqueue(
        object : Callback<ApiResponse<schedules>> {
            override fun onResponse(
                call: Call<ApiResponse<schedules>>,
                response: Response<ApiResponse<schedules>>
            ) {
                if (response.isSuccessful) {
                    title = response.body()!!.data!!.attributes!!.title!!
                    room = response.body()!!.data!!.attributes!!.room!!
                    dateAndTimes.clear()
                    response.body()!!.data!!.attributes!!.date_and_times!!.data!!.forEach {
                        dateAndTimes.add(it)
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

            override fun onFailure(call: Call<ApiResponse<schedules>>, t: Throwable) {
                Toast.makeText(
                    context,
                    t.message,
                    Toast.LENGTH_LONG
                ).show()
            }

        }
    )

    val retrofitDeleteSchedule = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(ScheduleService::class.java)
    val callDeleteSchedule = retrofitDeleteSchedule.deleteSchedule(id)
    val retrofitDeleteDate = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(DateAndTimeService::class.java)

    if (openAlertSchedule) {
        AlertDialog(
            onDismissRequest = {
                openAlertSchedule = false
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        callDeleteSchedule.enqueue(
                            object : Callback<ApiResponse<schedules>> {
                                override fun onResponse(
                                    call: Call<ApiResponse<schedules>>,
                                    response: Response<ApiResponse<schedules>>
                                ) {
                                    if (response.isSuccessful) {
                                        dateAndTimes.forEach {
                                            val callDeleteDate =
                                                retrofitDeleteDate.deleteDateAndTime(it.id!!.toString())
                                            callDeleteDate.enqueue(
                                                object : Callback<ApiResponse<date_and_times>> {
                                                    override fun onResponse(
                                                        call: Call<ApiResponse<date_and_times>>,
                                                        response: Response<ApiResponse<date_and_times>>
                                                    ) {
                                                        if (response.isSuccessful) {
                                                            navController.navigate("ListSchedule")
                                                        }
                                                    }

                                                    override fun onFailure(
                                                        call: Call<ApiResponse<date_and_times>>,
                                                        t: Throwable
                                                    ) {
                                                        Toast.makeText(
                                                            context,
                                                            t.message,
                                                            Toast.LENGTH_LONG
                                                        ).show()
                                                    }
                                                }
                                            )
                                        }
                                    }
                                }

                                override fun onFailure(
                                    call: Call<ApiResponse<schedules>>,
                                    t: Throwable
                                ) {
                                    Toast.makeText(
                                        context,
                                        t.message,
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            }
                        )
                    }
                ) {
                    Text(text = "Confirm")
                }
            },
            title = {
                Text(text = "Delete ${title}?")
            },
            text = {
                Text(text = "Are you sure want to delete this schedule?")
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        openAlertSchedule = false
                    }
                ) {
                    Text(text = "Cancel")
                }
            },

            )
    }

    LazyColumn(
        modifier = Modifier.padding(
            top = innerPadding.calculateTopPadding(),
            bottom = innerPadding.calculateBottomPadding(),
            start = 18.dp,
            end = 18.dp
        ),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        item {
            Column(
                modifier = Modifier
                    .clip(shape = RoundedCornerShape(25.dp))
                    .background(MaterialTheme.colorScheme.primaryContainer)
                    .fillMaxWidth()
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
                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        label = { Text(text = "Title") },
                    )
                    OutlinedTextField(
                        value = room,
                        onValueChange = { room = it },
                        label = { Text(text = "Room") },
                    )
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                    ) {
                        Button(
                            onClick = {

                            },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(text = "Save")
                        }
                        Button(
                            onClick = {
                                openAlertSchedule = true
                            },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(text = "Delete")
                        }
                    }
                }
            }
        }

        items(dateAndTimes.size) { index ->
            val timeStart = dateAndTimes[index].attributes?.start?.split(":")
            val timeEnd = dateAndTimes[index].attributes?.end?.split(":")
            start.add("${timeStart!![0]}:${timeStart[1]}")
            end.add("${timeEnd!![0]}:${timeEnd[1]}")

            var showDialog by remember { mutableStateOf(false) }
            val timePickerState = remember {
                TimePickerState(
                    initialHour = timeStart[0].toInt(),
                    initialMinute = timeStart[1].toInt(),
                    is24Hour = true
                )
            }

            var showDialogEnd by remember { mutableStateOf(false) }
            val timePickerStateEnd = remember {
                TimePickerState(
                    initialHour = timeEnd[0].toInt(),
                    initialMinute = timeEnd[1].toInt(),
                    is24Hour = true
                )
            }

            var openAlertDate by remember { mutableStateOf(false) }

            if (openAlertDate) {
                AlertDialog(
                    onDismissRequest = {
                        openAlertDate = false
                    },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                val callDeleteDate =
                                    retrofitDeleteDate.deleteDateAndTime(dateAndTimes[index].id!!.toString())
                                callDeleteDate.enqueue(
                                    object : Callback<ApiResponse<date_and_times>> {
                                        override fun onResponse(
                                            call: Call<ApiResponse<date_and_times>>,
                                            response: Response<ApiResponse<date_and_times>>
                                        ) {
                                            if (response.isSuccessful) {
                                                navController.navigate("detailSchedule/$id")
                                            }
                                        }

                                        override fun onFailure(
                                            call: Call<ApiResponse<date_and_times>>,
                                            t: Throwable
                                        ) {
                                            Toast.makeText(
                                                context,
                                                t.message,
                                                Toast.LENGTH_LONG
                                            ).show()
                                        }
                                    }
                                )
                            }
                        ) {
                            Text(text = "Confirm")
                        }
                    },
                    title = {
                        Text(text = "Delete ${title} on ${dateAndTimes[index].attributes?.day!!}?")
                    },
                    text = {
                        Text(text = "Are you sure want to delete this ${title} on ${dateAndTimes[index].attributes?.day}?")
                    },
                    dismissButton = {
                        TextButton(
                            onClick = {
                                openAlertDate = false
                            }
                        ) {
                            Text(text = "Cancel")
                        }
                    },

                    )
            }

            if (showDialog) {
                BasicAlertDialog(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = MaterialTheme.colorScheme.surface,
                            shape = RoundedCornerShape(size = 12.dp)
                        ),
                    onDismissRequest = {
                        showDialog = false
                    }
                ) {
                    Column(
                        modifier = Modifier
                            .background(
                                color = Color.LightGray.copy(alpha = 0.3f)
                            )
                            .padding(top = 28.dp, start = 20.dp, end = 20.dp, bottom = 12.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // time picker
                        TimePicker(state = timePickerState)

                        // buttons
                        Row(
                            modifier = Modifier
                                .padding(top = 12.dp)
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            // dismiss button
                            TextButton(onClick = { showDialog = false }) {
                                Text(text = "Dismiss")
                            }

                            // confirm button
                            TextButton(
                                onClick = {
                                    showDialog = false
                                    start[index] =
                                        formatter.format(parser.parse("${timePickerState.hour}:${timePickerState.minute}"))
                                }
                            ) {
                                Text(text = "Confirm")
                            }
                        }
                    }
                }
            }

            if (showDialogEnd) {
                BasicAlertDialog(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = MaterialTheme.colorScheme.surface,
                            shape = RoundedCornerShape(size = 12.dp)
                        ),
                    onDismissRequest = {
                        showDialogEnd = false
                    }
                ) {
                    Column(
                        modifier = Modifier
                            .background(
                                color = Color.LightGray.copy(alpha = 0.3f)
                            )
                            .padding(top = 28.dp, start = 20.dp, end = 20.dp, bottom = 12.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // time picker
                        TimePicker(state = timePickerStateEnd)

                        // buttons
                        Row(
                            modifier = Modifier
                                .padding(top = 12.dp)
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            // dismiss button
                            TextButton(onClick = { showDialogEnd = false }) {
                                Text(text = "Dismiss")
                            }

                            // confirm button
                            TextButton(
                                onClick = {
                                    showDialogEnd = false
                                    end[index] =
                                        formatter.format(parser.parse("${timePickerStateEnd.hour}:${timePickerStateEnd.minute}"))
                                }
                            ) {
                                Text(text = "Confirm")
                            }
                        }
                    }
                }
            }

            var selectedDay by remember { mutableStateOf(dateAndTimes[index].attributes?.day!!) }
            var textFieldSize by remember { mutableStateOf(Size.Zero) }
            var mExpanded by remember { mutableStateOf(false) }
            val icon = if (mExpanded)
                Icons.Filled.KeyboardArrowUp
            else
                Icons.Filled.KeyboardArrowDown
            Column(
                modifier = Modifier
                    .clip(shape = RoundedCornerShape(25.dp))
                    .background(MaterialTheme.colorScheme.primaryContainer)
                    .fillMaxWidth()
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
                    OutlinedTextField(
                        value = selectedDay,
                        onValueChange = { selectedDay = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .onGloballyPositioned { coordinates ->
                                textFieldSize = coordinates.size.toSize()
                            },
                        label = { Text("Day") },
                        trailingIcon = {
                            Icon(icon, "contentDescription",
                                Modifier.clickable { mExpanded = !mExpanded })
                        },
                        readOnly = true,
                    )
                    DropdownMenu(
                        expanded = mExpanded,
                        onDismissRequest = { mExpanded = false },
                        modifier = Modifier
                            .width(with(LocalDensity.current) { textFieldSize.width.toDp() })
                    ) {
                        daysList.forEach { label ->
                            DropdownMenuItem(onClick = {
                                selectedDay = label
                                mExpanded = false
                            }, text = {
                                Text(text = label)
                            }
                            )
                        }
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        OutlinedTextField(
                            value = start[index],
                            onValueChange = { start[index] = it },
                            label = { Text("Start") },
                            modifier = Modifier
                                .fillMaxWidth(fraction = .47f),
                            readOnly = true,
                            trailingIcon = {
                                Icon(Icons.Filled.Schedule, "contentDescription",
                                    Modifier.clickable { showDialog = true })
                            }
                        )
                        OutlinedTextField(
                            value = end[index],
                            onValueChange = { end[index] = it },
                            label = { Text("End") },
                            readOnly = true,
                            trailingIcon = {
                                Icon(Icons.Filled.Schedule, "contentDescription",
                                    Modifier.clickable { showDialogEnd = true })
                            }
                        )
                    }
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                    ) {
                        Button(
                            onClick = { /*TODO*/ },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(text = "Save")
                        }
                        Button(
                            onClick = {
                                openAlertDate = true
                            },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(text = "Delete")
                        }
                    }
                }
            }
        }

        item {
            Button(
                onClick = { navController.navigate("AddDayOnly/$id") },
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(text = "Add Time")
            }
        }
    }
}