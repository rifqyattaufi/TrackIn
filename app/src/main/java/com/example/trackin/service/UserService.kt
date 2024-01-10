package com.example.trackin.service

import com.example.trackin.respond.user_respond
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface UserService {
    @GET("users/{id}")
    fun getUserProfile(
        @Path("id") id: String
    ): Call<user_respond>
}