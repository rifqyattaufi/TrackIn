package com.example.trackin.screen

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerState
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import androidx.navigation.NavController
import com.example.trackin.data.TaskData
import com.example.trackin.data.TaskDataWrapper
import com.example.trackin.respond.ApiResponse
import com.example.trackin.respond.schedules
import com.example.trackin.respond.tasks
import com.example.trackin.service.ScheduleService
import com.example.trackin.service.TaskService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun AddTask(
    baseUrl: String,
    innerPadding: PaddingValues,
    navController: NavController,
    sharedPreferences: SharedPreferences,
    context: Context = LocalContext.current
) {
    var title by remember { mutableStateOf("") }

    var time by remember { mutableStateOf("") }
    var showTimePicker by remember { mutableStateOf(false) }
    var selectedHour by remember { mutableIntStateOf(0) }
    var selectedMinute by remember { mutableIntStateOf(0) }
    var showDialog by remember { mutableStateOf(false) }
    val timePickerState = remember {
        TimePickerState(
            initialHour = selectedHour,
            initialMinute = selectedMinute,
            is24Hour = true
        )
    }
    val formatterTime = SimpleDateFormat("HH:mm", Locale.ROOT)

    if (showTimePicker) {
        BasicAlertDialog(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = MaterialTheme.colorScheme.surface,
                    shape = RoundedCornerShape(size = 12.dp)
                ),
            onDismissRequest = {
                showTimePicker = false
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
                    TextButton(onClick = { showTimePicker = false }) {
                        Text(text = "Dismiss")
                    }

                    // confirm button
                    TextButton(
                        onClick = {
                            showTimePicker = false
                            selectedHour = timePickerState.hour
                            selectedMinute = timePickerState.minute
                            time =
                                formatterTime.format(
                                    Date(
                                        0,
                                        0,
                                        0,
                                        selectedHour,
                                        selectedMinute
                                    )
                                )
                        }
                    ) {
                        Text(text = "Confirm")
                    }
                }
            }
        }
    }

    var date by remember { mutableStateOf("") }
    val formatter = SimpleDateFormat("dd-MM-yyyy", Locale.ROOT)
    val datePickerState = rememberDatePickerState(
        selectableDates = object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                return utcTimeMillis > System.currentTimeMillis() - 1 * 24 * 60 * 60 * 1000
            }
        },
    )
    var showDatePicker by remember { mutableStateOf(false) }

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = {
                showDatePicker = false
            },
            confirmButton = {
                TextButton(onClick = {
                    showDatePicker = false
                    date = formatter.format(Date(datePickerState.selectedDateMillis!!))
                }) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    showDatePicker = false
                }) {
                    Text("Cancel")
                }
            },
        ) {
            DatePicker(
                state = datePickerState,
            )
        }
    }
    var mExpanded by remember { mutableStateOf(false) }
    val scheduleList = remember { mutableStateListOf<schedules>() }
    var selectedSchedule by remember { mutableStateOf("") }
    var selectedScheduleTitle by remember { mutableStateOf("") }
    var mTextFieldSize by remember { mutableStateOf(Size.Zero) }

    val icon =
        if (mExpanded)
            Icons.Filled.KeyboardArrowUp
        else
            Icons.Filled.KeyboardArrowDown


    val retrofitSchedule = Retrofit
        .Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(ScheduleService::class.java)
    val getSchedule = retrofitSchedule.getSchedules(
        id = sharedPreferences.getString("id", null)
    )
    getSchedule.enqueue(
        object : Callback<ApiResponse<List<schedules>>> {
            override fun onResponse(
                call: Call<ApiResponse<List<schedules>>>,
                response: Response<ApiResponse<List<schedules>>>
            ) {
                if (response.isSuccessful) {
                    scheduleList.clear()
                    response.body()?.data.let {
                        scheduleList.addAll(it!!)
                    }
                } else {
                    Toast.makeText(context, response.message(), Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ApiResponse<List<schedules>>>, t: Throwable) {
                Toast.makeText(context, t.message, Toast.LENGTH_SHORT).show()
            }
        }
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                top = innerPadding.calculateTopPadding(),
                bottom = innerPadding.calculateBottomPadding(),
                start = 18.dp,
                end = 18.dp
            )
    )
    {
        Column {
            OutlinedTextField(
                value = selectedScheduleTitle,
                onValueChange = { selectedScheduleTitle = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .onGloballyPositioned { coordinates ->
                        mTextFieldSize = coordinates.size.toSize()
                    },
                label = { Text("Subject") },
                trailingIcon = {
                    Icon(
                        imageVector = icon,
                        contentDescription = "contentDescription",
                        modifier = Modifier.clickable {
                            mExpanded = !mExpanded
                        }
                    )
                },
                readOnly = true,
            )
            DropdownMenu(
                expanded = mExpanded,
                onDismissRequest = { mExpanded = false },
                modifier = Modifier
                    .width(with(LocalDensity.current)
                    { mTextFieldSize.width.toDp() })
            ) {
                scheduleList.forEach { schedule ->
                    DropdownMenuItem(
                        onClick = {
                            selectedSchedule = schedule.id.toString()
                            selectedScheduleTitle = schedule.attributes?.title!!
                            mExpanded = false
                        },
                        text = { Text(schedule.attributes?.title!!) }
                    )
                }
            }
        }
        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text(text = "Title") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 18.dp),
            keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Words)
        )
        Text(text = "Deadline", modifier = Modifier.padding(top = 18.dp))
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            OutlinedTextField(
                value = date,
                onValueChange = { title = it },
                label = { Text(text = "Date") },
                modifier = Modifier
                    .fillMaxWidth(fraction = .47f),
                trailingIcon = {
                    Icon(
                        modifier = Modifier.clickable {
                            showDatePicker = true
                        },
                        imageVector = Icons.Filled.CalendarMonth,
                        contentDescription = "contentDescription",
                    )
                },
                readOnly = true,
            )
            OutlinedTextField(
                value = time,
                onValueChange = { time = it },
                label = { Text(text = "Time") },
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Schedule,
                        contentDescription = "contentDescription",
                        modifier = Modifier.clickable {
                            showTimePicker = true
                        }
                    )
                },
                readOnly = true,
            )
        }
        Button(
            onClick = {
                if (title.isEmpty() || date.isEmpty() || time.isEmpty() || selectedSchedule.isEmpty()) {
                    showDialog = true
                } else {
                    val timeInput = time.split(":")
                    val taskDataWrapper = TaskDataWrapper(
                        TaskData(
                            title = title,
                            deadline = SimpleDateFormat("yyyy-MM-dd").format(
                                SimpleDateFormat("dd-MM-yyyy").parse(
                                    date
                                )
                            ) + "T" +
                                    if (timeInput[0].toInt() + 1 < 25) {
                                        (timeInput[0].toInt() + 1).toString()
                                    } else {
                                        "00"
                                    }
                                    + ":" + timeInput[1] + ":00",
                            status = false,
                            schedule = selectedSchedule.toInt(),
                            users_permissions_user = sharedPreferences.getString("id", null)!!
                                .toInt()
                        )
                    )
                    val retrofit = Retrofit
                        .Builder()
                        .baseUrl(baseUrl)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build()
                        .create(TaskService::class.java)
                    val addTask = retrofit.addTask(
                        taskDataWrapper
                    )
                    addTask.enqueue(
                        object : Callback<ApiResponse<tasks>> {
                            override fun onResponse(
                                call: Call<ApiResponse<tasks>>,
                                response: Response<ApiResponse<tasks>>
                            ) {
                                if (response.isSuccessful) {
                                    Toast.makeText(
                                        context,
                                        "Task added successfully",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    navController.navigate("Home")
                                } else {
                                    try {
                                        Toast.makeText(
                                            context,
                                            response.errorBody()!!.string(),
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        Log.d("AddTask", response.errorBody()!!.string())
                                    } catch (e: Exception) {
                                        Toast.makeText(
                                            context,
                                            e.message,
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                            }

                            override fun onFailure(
                                call: Call<ApiResponse<tasks>>,
                                t: Throwable
                            ) {
                                Toast.makeText(context, t.message, Toast.LENGTH_SHORT).show()
                            }
                        }
                    )
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 18.dp)
        ) {
            Text(text = "Add Task")
        }
    }
}