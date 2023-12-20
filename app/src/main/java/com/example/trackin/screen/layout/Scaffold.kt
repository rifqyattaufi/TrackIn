package com.example.trackin.screen.layout

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.List
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.trackin.screen.Home

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Scaffold(
    navController: NavController,
    baseUrl: String,
    context: Context = LocalContext.current,
    content: @Composable (PaddingValues) -> Unit
) {
    androidx.compose.material3.Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Dashboard")
                },
            )
        },
        bottomBar = {
            BottomAppBar(
                modifier = Modifier.clip(
                    shape = RoundedCornerShape(
                        topStart = 25.dp,
                        topEnd = 25.dp
                    )
                ),
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp, 0.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Button(
                        onClick = { /*TODO*/ },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.background,
                            contentColor = MaterialTheme.colorScheme.onBackground
                        ),
                        shape = CircleShape,
                        modifier = Modifier.size(50.dp),
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Icon(Icons.Outlined.Home, contentDescription = "home")
                    }
                    Button(
                        onClick = { /*TODO*/ },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.background,
                            contentColor = MaterialTheme.colorScheme.onBackground
                        ),
                        shape = CircleShape,
                        modifier = Modifier.size(50.dp),
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Icon(Icons.Outlined.List, contentDescription = "list")
                    }
                    IconButton(
                        onClick = { /*TODO*/ },
                        colors = IconButtonDefaults.iconButtonColors(
                            contentColor = MaterialTheme.colorScheme.onBackground
                        ),
                        modifier = Modifier.size(50.dp)
                    ) {
                        Icon(
                            Icons.Rounded.Add,
                            contentDescription = "add",
                            modifier = Modifier.size(48.dp)
                        )
                    }
                    Button(
                        onClick = { /*TODO*/ },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.background,
                            contentColor = MaterialTheme.colorScheme.onBackground
                        ),
                        shape = CircleShape,
                        modifier = Modifier.size(50.dp),
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Icon(Icons.Outlined.Notifications, contentDescription = "home")
                    }
                    Button(
                        onClick = { /*TODO*/ },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.background,
                            contentColor = MaterialTheme.colorScheme.onBackground
                        ),
                        shape = CircleShape,
                        modifier = Modifier.size(50.dp),
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Icon(Icons.Outlined.AccountCircle, contentDescription = "home")
                    }
                }
            }
        },
    ) { innerPadding ->
        content(innerPadding)
    }
}
