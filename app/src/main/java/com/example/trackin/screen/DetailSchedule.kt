package com.example.trackin.screen

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.trackin.respond.ApiResponse
import com.example.trackin.respond.schedules
import com.example.trackin.service.ScheduleService
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Composable
fun DetailSchedule(
    baseUrl: String,
    id: String,
    innerPadding: PaddingValues,
    context: Context = LocalContext.current
) {
    var schedules by remember { mutableStateOf<schedules?>(null) }
    val retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(ScheduleService::class.java)
    val call = retrofit.detailSchedule(id)
    call.enqueue(
        object : Callback<ApiResponse<schedules>> {
            override fun onResponse(
                call: Call<ApiResponse<schedules>>,
                response: Response<ApiResponse<schedules>>
            ) {
                if (response.isSuccessful) {
                    val data = response.body()?.data
                    if (data != null) {
                        schedules = data
                    }
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

            override fun onFailure(call: Call<ApiResponse<schedules>>, t: Throwable) {
                Toast.makeText(
                    context,
                    t.message,
                    Toast.LENGTH_LONG
                ).show()
            }

        }
    )

    if (schedules != null) {
        Column(
            modifier = Modifier.padding(innerPadding)
        ) {
            Column {
                Text(text = schedules!!.attributes?.title!!)
                Text(text = schedules!!.attributes?.room!!)
                schedules!!.attributes?.date_and_times?.data?.forEach {
                    Text(text = it.attributes?.day!!)
                    Text(text = it.attributes?.start!!)
                    Text(text = it.attributes?.end!!)
                }
            }
        }
    }
}