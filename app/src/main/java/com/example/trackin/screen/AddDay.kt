package com.example.trackin.screen

import android.content.Context
import android.os.Build
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
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import com.example.trackin.data.DateAndTimesData

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
    var dateAndTimesData: List<DateAndTimesData>

    var title by remember { mutableStateOf(titleData) }
    var room by remember { mutableStateOf(roomData) }

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
    val timePickerState = remember {
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
                            start = "${timePickerState.hour}:${timePickerState.minute}"
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
                            end = "${timePickerStateEnd.hour}:${timePickerStateEnd.minute}"
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
                    .background(MaterialTheme.colorScheme.secondary)
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
                                // This value is used to assign to
                                // the DropDown the same width
                                mTextFieldSize = coordinates.size.toSize()
                            },
                        label = { Text("Day") },
                        trailingIcon = {
                            Icon(icon, "contentDescription",
                                Modifier.clickable { mExpanded = !mExpanded })
                        },
                        readOnly = true,
                    )

                    // Create a drop-down menu with list of cities,
                    // when clicked, set the Text Field text as the city selected
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
                        colors = TextFieldDefaults.textFieldColors(
                            containerColor = MaterialTheme.colorScheme.secondary
                        ),
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
                        colors = TextFieldDefaults.textFieldColors(
                            containerColor = MaterialTheme.colorScheme.secondary
                        ),
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
                    navController.popBackStack()
                }) {
                    Text(text = "Previous")
                }
                Button(onClick = {
                    if (meetData.toInt() > 1) {

                    } else {
                        
                    }
                }) {
                    if (meetData.toInt() > 1) {
                        Text(text = "Next")
                    } else {
                        Text(text = "Submit")
                    }
                }
            }
        }
    }
}

