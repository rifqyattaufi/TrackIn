package com.example.trackin

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.compose.AppTheme
import androidx.navigation.compose.rememberNavController
import com.example.trackin.screen.Home
import com.example.trackin.screen.SignIn
import com.example.trackin.screen.SignUp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val sharedPreferences: SharedPreferences =
                LocalContext.current.getSharedPreferences("auth", Context.MODE_PRIVATE)
            val navController = rememberNavController()

            val startDestination: String
            val jwt = sharedPreferences.getString("jwt", "")
            val baseUrl = "http://10.0.2.2:1337/api/"

            if (jwt.equals("")) {
                startDestination = "SignIn"
            } else {
                startDestination = "Home"
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
                            Home(navController = navController, baseUrl = baseUrl)
                        }
                    }
                }
            }
        }
    }
}