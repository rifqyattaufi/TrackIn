package com.example.trackin.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@Composable
fun ListTask(
    baseUrl: String,
    innerPadding: PaddingValues,
    navController: NavHostController
) {
    val borderColor = MaterialTheme.colorScheme.primary
    val borderColorSecondary = MaterialTheme.colorScheme.secondary
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
                            color = borderColorSecondary,
                            start = Offset(x = 0f, y = size.height),
                            end = Offset(x = size.width, y = size.height),
                            strokeWidth = borderSize
                        )
                    }
                    .clickable(onClick = {
                        navController.navigate("ListSchedule")
                    }),
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
    }
}