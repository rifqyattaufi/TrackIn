package com.example.trackin.screen

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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ListSchedule(
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
            //tuesday
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
            //wednesday
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
            //thursday
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
            //friday
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
            //saturday
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
            //sunday
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
}