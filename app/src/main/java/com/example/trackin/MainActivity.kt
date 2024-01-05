package com.example.trackin

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.compose.AppTheme
import com.example.trackin.screen.AddDay
import com.example.trackin.screen.AddDayOnly
import com.example.trackin.screen.AddSchedule
import com.example.trackin.screen.DetailSchedule
import com.example.trackin.screen.Home
import com.example.trackin.screen.ListSchedule
import com.example.trackin.screen.ListTask
import com.example.trackin.screen.Profile
import com.example.trackin.screen.SignIn
import com.example.trackin.screen.SignUp
import com.example.trackin.screen.layout.Scaffold as ScaffoldLayout

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val sharedPreferences: SharedPreferences =
                LocalContext.current.getSharedPreferences("auth", Context.MODE_PRIVATE)
            val navController = rememberNavController()

            val jwt = sharedPreferences.getString("jwt", "")

            val baseUrl = "https://api3.tnadam.me/api/"

//            val startDestination: String = "ListSchedule"

            val startDestination: String = if (jwt.equals("")) {
                "SignIn"
            } else {
                "Home"
            }

            AppTheme {
                Surface(
                    color = MaterialTheme.colorScheme.background
                ) {
                    NavHost(
                        navController = navController,
                        startDestination = startDestination
                    ) {
                        composable("SignIn") {
                            SignIn(navController = navController, baseUrl = baseUrl)
                        }
                        composable("SignUp") {
                            SignUp(navController = navController, baseUrl = baseUrl)
                        }
                        composable("Home") {
                            ScaffoldLayout(
                                title = "Home",
                                navController = navController,
                                content = {
                                    Home(
                                        baseUrl = baseUrl,
                                        innerPadding = it
                                    )
                                }
                            )
                        }
                        composable("AddSchedule") {
                            ScaffoldLayout(
                                title = "Add Schedule",
                                navController = navController,
                                content = {
                                    AddSchedule(
                                        innerPadding = it,
                                        navController = navController
                                    )
                                }
                            )
                        }
                        composable(
                            route = "AddDay" + "/{title_key}" + "/{room_key}" + "/{meet_key}",
                            arguments = listOf(
                                navArgument("title_key") { defaultValue = "" },
                                navArgument("room_key") { defaultValue = "" },
                                navArgument("meet_key") { defaultValue = "" },
                            )
                        ) { it ->
                            val titleData = it.arguments?.getString("title_key")
                            val roomData = it.arguments?.getString("room_key")
                            val meetData = it.arguments?.getString("meet_key")
                            ScaffoldLayout(
                                title = "Add Schedule",
                                navController = navController,
                                content = {
                                    AddDay(
                                        baseUrl = baseUrl,
                                        innerPadding = it,
                                        meetData = meetData!!,
                                        roomData = roomData,
                                        titleData = titleData,
                                        navController = navController
                                    )
                                }
                            )
                        }
                        composable("ListSchedule") {
                            ScaffoldLayout(
                                title = "List",
                                navController = navController,
                                content = {
                                    ListSchedule(
                                        baseUrl = baseUrl,
                                        innerPadding = it,
                                        navController = navController
                                    )
                                }
                            )
                        }
                        composable("ListTask") {
                            ScaffoldLayout(
                                title = "List",
                                navController = navController,
                                content = {
                                    ListTask(
                                        baseUrl = baseUrl,
                                        innerPadding = it,
                                        navController = navController
                                    )
                                }
                            )
                        }
                        composable("DetailSchedule/{id}") { it ->
                            val id = it.arguments?.getString("id")
                            ScaffoldLayout(
                                title = "Schedule",
                                navController = navController,
                                content = {
                                    DetailSchedule(
                                        baseUrl = baseUrl,
                                        innerPadding = it,
                                        id = id!!,
                                        navController = navController,
                                        sharedPreferences = sharedPreferences
                                    )
                                },
                                action = {
                                    IconButton(onClick = {
                                        navController.popBackStack()
                                    }) {
                                        Icon(
                                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                            contentDescription = "back"
                                        )
                                    }
                                }
                            )
                        }
                        composable("AddDayOnly/{id}") {
                            val id = it.arguments?.getString("id")
                            ScaffoldLayout(
                                title = "Add Schedule",
                                navController = navController,
                                content = {
                                    AddDayOnly(
                                        baseUrl = baseUrl,
                                        innerPadding = it,
                                        id = id!!,
                                        navController = navController
                                    )
                                }
                            )
                        }
                        composable("Profile") {
                            ScaffoldLayout(
                                title = "Profile",
                                navController = navController,
                                content = {
                                    Profile(
                                        baseUrl = baseUrl,
                                        innerPadding = it,
                                        navController = navController,
                                        sharedPreferences = sharedPreferences
                                    )
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}