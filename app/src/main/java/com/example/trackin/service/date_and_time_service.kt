package com.example.trackin.service

import com.example.trackin.respond.ApiResponse
import com.example.trackin.respond.date_and_times
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface date_and_time_service {
    @GET("date-and-times")
    fun getData(
        @Query("populate") populate: String = "schedule",
        @Query("filters[day][\$eq]") day: String?,
        @Query("filters[schedule][users_permissions_user][id][\$eq]") id: String?,
        @Query("sort") sort: String = "start"
    ): Call<ApiResponse<List<date_and_times>>>

    @GET("date-and-times")
    fun getListSchedule(
        @Query("populate") populate: String = "schedule",
        @Query("filters[schedule][users_permissions_user][id][\$eq]") id: String?,
        @Query("sort") sort: String = "start"
    ): Call<ApiResponse<List<date_and_times>>>


}