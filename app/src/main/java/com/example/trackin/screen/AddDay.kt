package com.example.trackin.screen

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Schedule
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
import androidx.compose.runtime.mutableIntStateOf
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
import com.example.trackin.data.DateAndTimeDataWrapper
import com.example.trackin.data.DateAndTimesData
import com.example.trackin.data.ScheduleData
import com.example.trackin.data.ScheduleDataWrapper
import com.example.trackin.notification.NotificationReceiver
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
import java.util.Calendar

@SuppressLint("SimpleDateFormat")
@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddDay(
    baseUrl: String,
    context: Context = LocalContext.current,
    innerPadding: PaddingValues,
    meetData: String,
    roomData: String?,
    titleData: String?,
    navController: NavController
) {
    var nextOrSubmit by remember { mutableStateOf("") }
    (if (meetData.toInt() > 1) {
        "Next"
    } else {
        "Submit"
    }).also { nextOrSubmit = it }
    var meet by remember { mutableIntStateOf(meetData.toInt()) }
    val dateAndTimesData = remember { mutableStateListOf<DateAndTimesData>() }
    val sharedPreferences: SharedPreferences =
        LocalContext.current.getSharedPreferences("auth", Context.MODE_PRIVATE)

    var start by remember { mutableStateOf("") }
    var end by remember { mutableStateOf("") }

    var mExpanded by remember { mutableStateOf(false) }
    val mCities =
        listOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday")
    var mSelectedText by remember { mutableStateOf("") }
    var mTextFieldSize by remember { mutableStateOf(Size.Zero) }

    val icon = if (mExpanded)
        Icons.Filled.KeyboardArrowUp
    else
        Icons.Filled.KeyboardArrowDown

    var selectedHour by remember { mutableIntStateOf(0) }
    var selectedMinute by remember { mutableIntStateOf(0) }
    var showDialog by remember { mutableStateOf(false) }
    var timePickerState = remember {
        TimePickerState(
            initialHour = selectedHour,
            initialMinute = selectedMinute,
            is24Hour = true
        )
    }

    var selectedHourEnd by remember { mutableIntStateOf(0) }
    var selectedMinuteEnd by remember { mutableIntStateOf(0) }
    var showDialogEnd by remember { mutableStateOf(false) }
    val timePickerStateEnd = remember {
        TimePickerState(
            initialHour = selectedHourEnd,
            initialMinute = selectedMinuteEnd,
            is24Hour = true
        )
    }
    val parser = SimpleDateFormat("HH:mm")
    val formatter = SimpleDateFormat("HH:mm")
    val formatterInput = SimpleDateFormat("HH:mm:ss")

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
                            selectedHour = timePickerState.hour
                            selectedMinute = timePickerState.minute
                            start =
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
                            selectedHourEnd = timePickerStateEnd.hour
                            selectedMinuteEnd = timePickerStateEnd.minute
                            end =
                                formatter.format(parser.parse("${timePickerStateEnd.hour}:${timePickerStateEnd.minute}"))
                        }
                    ) {
                        Text(text = "Confirm")
                    }
                }
            }
        }
    }

    Box(
        modifier = Modifier
            .padding(
                bottom = innerPadding.calculateBottomPadding() + 18.dp,
                top = innerPadding.calculateTopPadding(),
                start = 18.dp,
                end = 18.dp
            )
            .fillMaxSize()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
        )
        {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(
                        shape = RoundedCornerShape(25.dp)
                    )
                    .padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Column {
                    OutlinedTextField(
                        value = mSelectedText,
                        onValueChange = { mSelectedText = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .onGloballyPositioned { coordinates ->
                                mTextFieldSize = coordinates.size.toSize()
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
                            .width(with(LocalDensity.current) { mTextFieldSize.width.toDp() })
                    ) {
                        mCities.forEach { label ->
                            DropdownMenuItem(onClick = {
                                mSelectedText = label
                                mExpanded = false
                            }, text = {
                                Text(text = label)
                            }
                            )
                        }
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    OutlinedTextField(
                        value = start,
                        onValueChange = { start = it },
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
                        value = end,
                        onValueChange = { end = it },
                        label = { Text("End") },
                        readOnly = true,
                        trailingIcon = {
                            Icon(Icons.Filled.Schedule, "contentDescription",
                                Modifier.clickable { showDialogEnd = true })
                        }
                    )
                }
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                Button(onClick = {
                    if (meet == meetData.toInt()) {
                        navController.popBackStack()
                    } else {
                        mSelectedText = dateAndTimesData.last().day
                        start = dateAndTimesData.last().start
                        end = dateAndTimesData.last().end
                        dateAndTimesData.dropLast(1)
                        meet += 1
                        nextOrSubmit = "Next"
                    }
                }) {
                    Text(text = "Previous")
                }
                Button(onClick =
                {
                    if (mSelectedText.isEmpty() || start.isEmpty() || end.isEmpty()) {
                        Toast.makeText(
                            context,
                            "Please fill all the fields",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        dateAndTimesData.add(
                            DateAndTimesData(
                                mSelectedText,
                                start,
                                end,
                                0
                            )
                        )
                        if (meet > 1) {
                            mSelectedText = ""
                            start = ""
                            end = ""
                            meet -= 1
                            if (meet == 1) {
                                nextOrSubmit = "Submit"
                            }
                        } else {
                            val scheduleData = ScheduleDataWrapper(
                                ScheduleData(
                                    titleData!!,
                                    roomData!!,
                                    sharedPreferences.getString("id", "0")!!.toInt()
                                )
                            )
                            val retrofit = Retrofit
                                .Builder()
                                .baseUrl(baseUrl)
                                .addConverterFactory(GsonConverterFactory.create())
                                .build()
                                .create(ScheduleService::class.java)
                            val call = retrofit.addSchedule(scheduleData)
                            call.enqueue(
                                object : Callback<ApiResponse<schedules>> {
                                    override fun onResponse(
                                        call: Call<ApiResponse<schedules>>,
                                        response: Response<ApiResponse<schedules>>
                                    ) {
                                        if (response.isSuccessful) {
                                            val schedule = response.body()!!.data
                                            dateAndTimesData.forEach {
                                                val dateAndTimeData = DateAndTimesData(
                                                    it.day,
                                                    formatterInput.format(parser.parse(it.start)),
                                                    formatterInput.format(parser.parse(it.end)),
                                                    schedule!!.id!!
                                                )
                                                val retrofit2 = Retrofit
                                                    .Builder()
                                                    .baseUrl(baseUrl)
                                                    .addConverterFactory(GsonConverterFactory.create())
                                                    .build()
                                                    .create(DateAndTimeService::class.java)
                                                val call2 = retrofit2.addDateAndTime(
                                                    DateAndTimeDataWrapper(dateAndTimeData)
                                                )
                                                call2.enqueue(
                                                    object :
                                                        Callback<ApiResponse<date_and_times>> {
                                                        override fun onResponse(
                                                            call: Call<ApiResponse<date_and_times>>,
                                                            response: Response<ApiResponse<date_and_times>>
                                                        ) {
                                                            if (response.isSuccessful) {
                                                                scheduleNotification(
                                                                    context,
                                                                    dateAndTimeData,
                                                                    response.body()?.data?.id!!.toInt()
                                                                )
                                                            } else {
                                                                try {
                                                                    val jObjError =
                                                                        JSONObject(
                                                                            response.errorBody()!!
                                                                                .string()
                                                                        )
                                                                    Toast.makeText(
                                                                        context,
                                                                        jObjError.getJSONObject(
                                                                            "error"
                                                                        )
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
                                                            call: Call<ApiResponse<date_and_times>>,
                                                            t: Throwable
                                                        ) {
                                                            Log.d("TAG", "fail")
                                                        }

                                                    }
                                                )
                                            }
                                            navController.navigate("home")
                                        }
                                    }

                                    override fun onFailure(
                                        call: Call<ApiResponse<schedules>>,
                                        t: Throwable
                                    ) {
                                        TODO("Not yet implemented")
                                    }

                                }
                            )
                        }
                    }
                }
                ) {
                    Text(text = nextOrSubmit)
                }
            }
        }
    }
}

fun scheduleNotification(
    context: Context,
    dateAndTimes: DateAndTimesData,
    notificationId: Int
) {
    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    val intent = Intent(context, NotificationManager::class.java)
    intent.putExtra(NotificationReceiver.NOTIFICATION_ID_EXTRA.toString(), notificationId)

    val pendingIntent = PendingIntent.getBroadcast(
        context,
        notificationId,
        intent,
        PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )

    val calendar = Calendar.getInstance()
    when (dateAndTimes.day) {
        "Monday" -> {
            calendar.set(Calendar.DAY_OF_WEEK, 1)
        }

        "Tuesday" -> {
            calendar.set(Calendar.DAY_OF_WEEK, 2)
        }

        "Wednesday" -> {
            calendar.set(Calendar.DAY_OF_WEEK, 3)
        }

        "Thursday" -> {
            calendar.set(Calendar.DAY_OF_WEEK, 4)
        }

        "Friday" -> {
            calendar.set(Calendar.DAY_OF_WEEK, 5)
        }

        "Saturday" -> {
            calendar.set(Calendar.DAY_OF_WEEK, 6)
        }

        "Sunday" -> {
            calendar.set(Calendar.DAY_OF_WEEK, 7)
        }
    }
    calendar.set(Calendar.HOUR_OF_DAY, dateAndTimes.start.split(":")[0].toInt())
    calendar.set(Calendar.MINUTE, dateAndTimes.start.split(":")[1].toInt() - 30)
    calendar.set(Calendar.SECOND, 0)

    val triggerAtMillis = calendar.timeInMillis

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            triggerAtMillis,
            pendingIntent
        )
    } else {
        alarmManager.setExact(
            AlarmManager.RTC_WAKEUP,
            triggerAtMillis,
            pendingIntent
        )
    }
}


