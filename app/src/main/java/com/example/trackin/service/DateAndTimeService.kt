package com.example.trackin.service

import com.example.trackin.data.DateAndTimeDataWrapper
import com.example.trackin.respond.ApiResponse
import com.example.trackin.respond.date_and_times
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface DateAndTimeService {
    @POST("date-and-times")
    fun addDateAndTime(@Body dateAndTime: DateAndTimeDataWrapper): Call<ApiResponse<date_and_times>>

    @DELETE("date-and-times/{id}")
    fun deleteDateAndTime(
        @Path("id") id: String
    ): Call<ApiResponse<date_and_times>>

    @PUT("date-and-times/{id}")
    fun updateDateAndTime(
        @Path("id") id: String,
        @Body dateAndTime: DateAndTimeDataWrapper
    ): Call<ApiResponse<date_and_times>>
}