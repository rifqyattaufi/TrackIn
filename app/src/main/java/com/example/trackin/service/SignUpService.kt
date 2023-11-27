package com.example.trackin.service

import com.example.trackin.data.SignUpData
import com.example.trackin.respond.JWTRespond
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface SignUpService {
    @POST("auth/local/register")
    fun saveData(@Body signUpData: SignUpData): Call<JWTRespond>
}