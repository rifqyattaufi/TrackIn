package com.example.trackin.service

import com.example.trackin.data.ScheduleDataWrapper
import com.example.trackin.respond.ApiResponse
import com.example.trackin.respond.schedules
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ScheduleService {
    @POST("schedules")
    fun addSchedule(@Body schedule: ScheduleDataWrapper): Call<ApiResponse<schedules>>
}