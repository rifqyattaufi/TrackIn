package com.example.trackin.screen

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddSchedule(
    context: Context = LocalContext.current,
    navController: NavController,
    innerPadding: PaddingValues
) {
    var title by remember { mutableStateOf("") }
    var room by remember { mutableStateOf("") }
    var meet by remember { mutableStateOf("") }
    val number = remember { Regex("^\\d+\$") }

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
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(
                        shape = RoundedCornerShape(25.dp)
                    )
                    .padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    modifier = Modifier
                        .fillMaxWidth(),
                    label = { Text("Title") },
                    keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Words)
                )
                OutlinedTextField(
                    value = room,
                    onValueChange = { room = it },
                    modifier = Modifier
                        .fillMaxWidth(),
                    label = { Text("Room") },
                )
                OutlinedTextField(
                    value = meet,
                    onValueChange = {
                        if (it.isEmpty() || it.matches(number)) {
                            meet = it
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth(),
                    label = { Text("Meeting per Weeks") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }
            Button(onClick = {
                if (room.isEmpty() || title.isEmpty() || meet.isEmpty()) {
                    Toast.makeText(context, "Please fill all the fields", Toast.LENGTH_SHORT).show()
                } else {
                    navController.navigate("AddDay/$title/$room/$meet")
                }
            }) {
                Text("Next")
            }
        }
    }
}