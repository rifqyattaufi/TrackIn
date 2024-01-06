package com.example.trackin.service

import com.example.trackin.data.TaskDataWrapper
import com.example.trackin.respond.ApiResponse
import com.example.trackin.respond.tasks
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface TaskService {
    @GET("tasks")
    fun getTasks(
        @Query("filters[users_permissions_user][id][\$eq]") id: String?,
        @Query("populate") populate: String = "schedule",
        @Query("sort") sort: String = "deadline",
        @Query("filters[status][\$eq]") status: Boolean = false
    ): Call<ApiResponse<List<tasks>>>

    @POST("tasks")
    fun addTask(
        @Body task: TaskDataWrapper
    ): Call<ApiResponse<tasks>>
}