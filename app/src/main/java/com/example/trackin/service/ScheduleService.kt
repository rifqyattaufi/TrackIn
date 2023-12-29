package com.example.trackin.service

import com.example.trackin.data.ScheduleDataWrapper
import com.example.trackin.respond.ApiResponse
import com.example.trackin.respond.schedules
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ScheduleService {
    @POST("schedules")
    fun addSchedule(@Body schedule: ScheduleDataWrapper): Call<ApiResponse<schedules>>

    @GET("schedules/{id}")
    fun detailSchedule(
        @Path("id") id: String,
        @Query("populate") populate: String = "date_and_times",
    ): Call<ApiResponse<schedules>>
}