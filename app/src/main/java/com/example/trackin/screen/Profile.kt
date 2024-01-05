package com.example.trackin.screen

import android.content.SharedPreferences
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController

@Composable
fun Profile(
    baseUrl: String,
    innerPadding: PaddingValues,
    navController: NavHostController,
    sharedPreferences: SharedPreferences
) {
    Column(
        modifier = Modifier
            .padding(innerPadding)
            .fillMaxSize()
    ) {
        Button(onClick = {
            sharedPreferences.edit().putString("jwt", "").apply()
            navController.navigate("SignIn")
        }) {
            Text(text = "Sign Out")
        }
    }
}