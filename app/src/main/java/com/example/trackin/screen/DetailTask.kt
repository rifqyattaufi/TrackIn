package com.example.trackin.screen

import android.content.Context
import android.content.SharedPreferences
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
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailTask(
    baseUrl: String,
    innerPadding: PaddingValues,
    id: String,
    navController: NavController,
    sharedPreferences: SharedPreferences,
    context: Context = LocalContext.current,
    titleData: String,
    deadlineData: String,
    subjectIdData: String,
    statusData: String,
) {
    val retrofitTask = Retrofit
        .Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(TaskService::class.java)

    var openAlertSchedule by remember { mutableStateOf(false) }

    var title by remember { mutableStateOf(titleData) }
    var date by remember {
        mutableStateOf(
            SimpleDateFormat("dd-MM-yyyy").format(
                SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parse(
                    deadlineData
                )!!
            )
        )
    }
    var time by remember {
        mutableStateOf(
            SimpleDateFormat("HH:mm").format(
                SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parse(
                    deadlineData
                )!!
            )
        )
    }
    var status by remember { mutableStateOf(statusData.toBoolean()) }
    val timePickerState = rememberTimePickerState(
        initialHour = SimpleDateFormat("HH").format(
            SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parse(
                deadlineData
            )!!
        ).toInt(),
        initialMinute = SimpleDateFormat("mm").format(
            SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parse(
                deadlineData
            )!!
        ).toInt(),
        is24Hour = true
    )
    var showTimePicker by remember { mutableStateOf(false) }
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
                            time = SimpleDateFormat("HH:mm").format(
                                SimpleDateFormat("HH:mm").parse(
                                    "${timePickerState.hour}:${timePickerState.minute}"
                                )!!
                            )
                        }
                    ) {
                        Text(text = "Confirm")
                    }
                }
            }
        }
    }


    val datePickerState = rememberDatePickerState(
        selectableDates = object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                return utcTimeMillis > System.currentTimeMillis() - 1 * 24 * 60 * 60 * 1000
            }
        },
        initialSelectedDateMillis = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parse(
            deadlineData
        )!!.time,
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
                    date = SimpleDateFormat("dd-MM-yyyy").format(
                        datePickerState.selectedDateMillis
                    )
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
    var selectedScheduleTitle by remember { mutableStateOf("") }
    var selectedSchedule by remember { mutableStateOf("") }

    val scheduleList = remember { mutableStateListOf<schedules>() }
    var mExpanded by remember { mutableStateOf(false) }
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
                    response.body()?.data?.forEach {
                        scheduleList.add(it)
                        if (it.id.toString() == subjectIdData) {
                            selectedScheduleTitle = it.attributes?.title!!
                            selectedSchedule = it.id.toString()
                        }
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
                        modifier = Modifier.fillMaxWidth(),
                    )
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
                    Text(text = "Deadline")
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                    ) {
                        OutlinedTextField(
                            value = date,
                            onValueChange = { date = it },
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
                    Text(text = "Status")
                    Row(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .background(
                                    color = if (status) {
                                        MaterialTheme.colorScheme.primary
                                    } else {
                                        MaterialTheme.colorScheme.secondary
                                    },
                                    shape = RoundedCornerShape(
                                        topStart = 12.dp,
                                        bottomStart = 12.dp
                                    )
                                )
                                .clickable(
                                    onClick = {
                                        status = true
                                    }
                                ),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Done",
                                modifier = Modifier.padding(12.dp),
                                color = if (status) {
                                    Color.White
                                } else {
                                    Color.Black
                                }
                            )
                        }
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .background(
                                    color = if (!status) {
                                        MaterialTheme.colorScheme.primary
                                    } else {
                                        MaterialTheme.colorScheme.secondary
                                    },
                                    shape = RoundedCornerShape(
                                        topEnd = 12.dp,
                                        bottomEnd = 12.dp
                                    )
                                )
                                .clickable {
                                    status = false
                                },
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Not Done",
                                modifier = Modifier.padding(12.dp),
                                color = if (!status) {
                                    Color.White
                                } else {
                                    Color.Black
                                }
                            )
                        }
                    }
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                    ) {
                        Button(
                            onClick = {
                                val updateTaskData = TaskDataWrapper(
                                    data = TaskData(
                                        title = title,
                                        deadline = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").format(
                                            SimpleDateFormat("dd-MM-yyyy HH:mm").parse(
                                                "$date $time"
                                            )!!
                                        ),
                                        status = status,
                                        schedule = selectedSchedule.toInt()
                                    )
                                )
                                val updateTask = retrofitTask.updateTask(id, updateTaskData)
                                updateTask.enqueue(
                                    object : Callback<ApiResponse<tasks>> {
                                        override fun onResponse(
                                            call: Call<ApiResponse<tasks>>,
                                            response: Response<ApiResponse<tasks>>
                                        ) {
                                            if (response.isSuccessful) {
                                                Toast.makeText(context, "Saved", Toast.LENGTH_SHORT)
                                                    .show()
                                            } else {
                                                try {
                                                    Toast.makeText(
                                                        context,
                                                        response.errorBody()?.string(),
                                                        Toast.LENGTH_SHORT
                                                    ).show()
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
                                            Toast.makeText(context, t.message, Toast.LENGTH_SHORT)
                                                .show()
                                        }

                                    }
                                )
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
    }
}
