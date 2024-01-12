package com.example.trackin.screen

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.trackin.data.SignUpData
import com.example.trackin.respond.JWTRespond
import com.example.trackin.service.AuthService
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUp(
    navController: NavController,
    baseUrl: String,
    context: Context = LocalContext.current
) {
    var showPassword by remember { mutableStateOf(false) }
    var showConfirmPassword by remember { mutableStateOf(false) }

    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.5f)
            .clip(
                shape = RoundedCornerShape(
                    bottomEnd = 47.dp,
                    bottomStart = 47.dp
                )
            )
            .background(color = MaterialTheme.colorScheme.primary),
    )
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(15.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    )
    {
        Column(
            modifier = Modifier
                .clip(
                    shape = RoundedCornerShape(25.dp)
                )
                .background(color = MaterialTheme.colorScheme.background)
                .padding(horizontal = 21.dp, vertical = 33.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "Sign Up",
                fontSize = 32.sp,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(56.dp))
            Column {
                Text(
                    text = "Full Name",
                    fontSize = 15.sp,
                    modifier = Modifier
                        .padding(start = 16.dp, bottom = 8.dp)
                        .fillMaxWidth()
                )
                TextField(
                    value = username,
                    onValueChange = { username = it },
                    shape = RoundedCornerShape(100),
                    colors = TextFieldDefaults.textFieldColors(
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                )
            }
            Spacer(modifier = Modifier.height(15.dp))
            Column {
                Text(
                    text = "Email",
                    fontSize = 15.sp,
                    modifier = Modifier
                        .padding(start = 16.dp, bottom = 8.dp)
                        .fillMaxWidth()
                )
                TextField(
                    value = email,
                    onValueChange = { email = it },
                    shape = RoundedCornerShape(100),
                    colors = TextFieldDefaults.textFieldColors(
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                )
            }
            Spacer(modifier = Modifier.height(15.dp))
            Column {
                Text(
                    text = "Password",
                    fontSize = 15.sp,
                    modifier = Modifier
                        .padding(start = 16.dp, bottom = 8.dp)
                        .fillMaxWidth()
                )
                TextField(
                    value = password,
                    onValueChange = { password = it },
                    shape = RoundedCornerShape(100),
                    colors = TextFieldDefaults.textFieldColors(
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    visualTransformation = if (showPassword) {
                        VisualTransformation.None
                    } else {
                        PasswordVisualTransformation()
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password
                    ),
                    singleLine = true,
                    trailingIcon = {
                        val image = if (showPassword) {
                            Icons.Default.VisibilityOff
                        } else {
                            Icons.Default.Visibility
                        }
                        val description = if (showPassword) {
                            "Hide password"
                        } else {
                            "Show password"
                        }
                        IconButton(onClick = {
                            showPassword = !showPassword
                        }) {
                            Icon(imageVector = image, contentDescription = description)
                        }
                    }
                )
            }
            Spacer(modifier = Modifier.height(15.dp))
            Column {
                Text(
                    text = "Confirm Password",
                    fontSize = 15.sp,
                    modifier = Modifier
                        .padding(start = 16.dp, bottom = 8.dp)
                        .fillMaxWidth()
                )
                TextField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    shape = RoundedCornerShape(100),
                    colors = TextFieldDefaults.textFieldColors(
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    visualTransformation = if (showConfirmPassword) {
                        VisualTransformation.None
                    } else {
                        PasswordVisualTransformation()
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password
                    ),
                    singleLine = true,
                    trailingIcon = {
                        val image = if (showConfirmPassword) {
                            Icons.Default.VisibilityOff
                        } else {
                            Icons.Default.Visibility
                        }
                        val description = if (showConfirmPassword) {
                            "Hide password"
                        } else {
                            "Show password"
                        }
                        IconButton(onClick = {
                            showConfirmPassword = !showConfirmPassword
                        }) {
                            Icon(imageVector = image, contentDescription = description)
                        }
                    }
                )
            }
            Spacer(modifier = Modifier.height(15.dp))
            Button(
                onClick = {
                    if (password != confirmPassword) {
                        Toast
                            .makeText(
                                context,
                                "Password and Confirm Password doesn't match",
                                Toast.LENGTH_SHORT
                            ).show()
                    } else if (username == "") {
                        Toast
                            .makeText(
                                context,
                                "Full Name cannot be empty",
                                Toast.LENGTH_SHORT
                            ).show()
                    } else if (email == "") {
                        Toast
                            .makeText(
                                context,
                                "Email cannot be empty",
                                Toast.LENGTH_SHORT
                            ).show()
                    } else if (password == "") {
                        Toast
                            .makeText(
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
                        val call = retrofit.saveData(
                            SignUpData(
                                username = username,
                                email = email,
                                password = password
                            )
                        )
                        call.enqueue(
                            object : Callback<JWTRespond> {
                                override fun onResponse(
                                    call: Call<JWTRespond>,
                                    response: Response<JWTRespond>,
                                ) {
                                    if (response.isSuccessful) {
                                        navController.navigate("SignIn")
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
                },
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(text = "Sign Up")
            }
            Spacer(modifier = Modifier.height(50.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Already have an account?",
                    fontSize = 13.sp,
                )
                TextButton(onClick = {
                    navController.navigate("SignIn")
                }) {
                    Text(
                        text = "Sign In",
                        fontSize = 13.sp
                    )
                }
            }
        }
    }
}