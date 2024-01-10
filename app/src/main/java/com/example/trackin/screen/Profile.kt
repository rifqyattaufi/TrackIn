package com.example.trackin.screen

import android.content.Context
import android.content.SharedPreferences
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.trackin.respond.user_respond
import com.example.trackin.service.UserService
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Composable
fun Profile(
    baseUrl: String,
    innerPadding: PaddingValues,
    navController: NavHostController,
    sharedPreferences: SharedPreferences,
    context: Context = LocalContext.current
) {
    val id = sharedPreferences.getString("id", "")
    var userName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }

    val retrofitUser = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(baseUrl)
        .build()
        .create(UserService::class.java)
    val getProfile = retrofitUser.getUserProfile(id = id!!)
    getProfile.enqueue(
        object : Callback<user_respond> {
            override fun onResponse(
                call: Call<user_respond>,
                response: Response<user_respond>
            ) {
                if (response.isSuccessful) {
                    userName = response.body()?.username.toString()
                    email = response.body()?.email.toString()
                } else {
                    try {
                        val jObjError =
                            JSONObject(response.errorBody()!!.string())
                        Toast.makeText(
                            context,
                            jObjError.getJSONObject("error")
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

            override fun onFailure(call: Call<user_respond>, t: Throwable) {
                println("Error")
            }

        }
    )
    Column(
        modifier = Modifier
            .padding(
                top = innerPadding.calculateTopPadding(),
                bottom = innerPadding.calculateBottomPadding(),
                start = 18.dp,
                end = 18.dp
            )
            .fillMaxSize()
    ) {
        Text(text = "Username: $userName")
        Text(text = "Email: $email")
        Button(onClick = {
            sharedPreferences.edit().putString("jwt", "").apply()
            sharedPreferences.edit().putString("id", "").apply()
            navController.navigate("SignIn")
        }) {
            Text(text = "Sign Out")
        }
    }
}