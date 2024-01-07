package com.example.trackin.screen

import android.content.Context
import android.content.SharedPreferences
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.trackin.respond.ApiResponse
import com.example.trackin.respond.tasks
import com.example.trackin.service.TaskService
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Composable
fun EditTask(
    id: String,
    baseUrl: String,
    innerPadding: PaddingValues,
    navController: NavController,
    sharedPreferences: SharedPreferences,
    context: Context = LocalContext.current
) {
    val task by remember { mutableStateOf(tasks()) }
    val retrofitTask = Retrofit
        .Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(baseUrl)
        .build()
        .create(TaskService::class.java)
    val callTask = retrofitTask.detailTask(id)
    callTask.enqueue(
        object : Callback<ApiResponse<tasks>> {
            override fun onResponse(
                call: Call<ApiResponse<tasks>>,
                response: Response<ApiResponse<tasks>>
            ) {
                if (response.isSuccessful) {
                    task.id = response.body()?.data?.id
                    task.attributes?.title = response.body()?.data?.attributes?.title
                    task.attributes?.deadline = response.body()?.data?.attributes?.deadline
                    task.attributes?.status = response.body()?.data?.attributes?.status
                    task.attributes?.schedule = response.body()?.data?.attributes?.schedule
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

            override fun onFailure(call: Call<ApiResponse<tasks>>, t: Throwable) {
                Toast.makeText(
                    context,
                    t.message,
                    Toast.LENGTH_LONG
                ).show()
            }

        }
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                top = innerPadding.calculateTopPadding(),
                bottom = innerPadding.calculateBottomPadding(),
                start = 18.dp,
                end = 18.dp
            )
    ) {
        if (task.id != null) {
            Column(
                modifier = Modifier
                    .clip(shape = RoundedCornerShape(25.dp))
                    .background(MaterialTheme.colorScheme.primaryContainer)
                    .fillMaxWidth()
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
                    OutlinedTextField(
                        value = task.attributes?.title!!,
                        onValueChange = { task.attributes?.title = it },
                        label = { Text(text = "Title") },
                        modifier = Modifier.fillMaxWidth(),
                    )
                    OutlinedTextField(
                        value = task.attributes?.deadline!!,
                        onValueChange = { task.attributes?.deadline = it },
                        label = { Text(text = "Room") },
                        modifier = Modifier.fillMaxWidth(),
                    )
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                    ) {
                        Button(
                            onClick = { TODO() },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(text = "Save")
                        }
                        Button(
                            onClick = {
                                TODO()
                            },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(text = "Delete")
                        }
                    }
                }
            }
        } else {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.width(64.dp),
                    color = MaterialTheme.colorScheme.primary,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant,
                )
            }
        }
    }
}