package com.example.trackin.screen

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.trackin.PreferencesManager
import com.example.trackin.data.SignInData
import com.example.trackin.respond.JWTRespond
import com.example.trackin.service.AuthService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignIn(
    navController: NavController,
    baseUrl: String,
    context: Context = LocalContext.current
) {
    val preferencesManager = remember { PreferencesManager(context) }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    )
    {
        Text(text = "Sign In")
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") }
        )
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") }
        )
        Spacer(modifier = Modifier.height(10.dp))
        Button(onClick = {
            if (email == "") {
                Toast.makeText(
                    context,
                    "Email cannot be empty",
                    Toast.LENGTH_SHORT
                ).show()
            } else if (password == "") {
                Toast.makeText(
                    context,
                    "Password cannot be empty",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                val retrofit = Retrofit
                    .Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                    .create(AuthService::class.java)
                val call = retrofit.login(
                    SignInData(identifier = email, password = password)
                )
                call.enqueue(
                    object : Callback<JWTRespond> {
                        override fun onResponse(
                            call: Call<JWTRespond>,
                            response: Response<JWTRespond>
                        ) {
                            if (response.isSuccessful) {
                                preferencesManager.saveData("jwt", response.body()?.jwt!!)
                                preferencesManager.saveData(
                                    "id",
                                    response.body()?.user?.id.toString()
                                )
                                navController.navigate("Home")
                            } else {
                                Toast.makeText(
                                    context,
                                    "Invalid email or password",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }

                        override fun onFailure(call: Call<JWTRespond>, t: Throwable) {
                            Toast.makeText(
                                context,
                                "Error: ${t.message}",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                )
            }
        }) {
            Text(text = "Login")
        }
        Row {
            Text(text = "Don't have an account?")
            Spacer(modifier = Modifier.height(10.dp))
            Button(onClick = {
                navController.navigate("SignUp")
            }) {
                Text(text = "Sign Up")
            }
        }
    }
}