package com.example.trackin.service

import com.example.trackin.data.DateAndTimeDataWrapper
import com.example.trackin.respond.ApiResponse
import com.example.trackin.respond.date_and_times
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface DateAndTimeService {
    @POST("date-and-times")
    fun addDateAndTime(@Body dateAndTime: DateAndTimeDataWrapper): Call<ApiResponse<date_and_times>>
}