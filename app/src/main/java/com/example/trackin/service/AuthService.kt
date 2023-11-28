package com.example.trackin.service

import com.example.trackin.data.SignInData
import com.example.trackin.data.SignUpData
import com.example.trackin.respond.JWTRespond
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {
    @POST("auth/local/register")
    fun saveData(@Body signUpData: SignUpData): Call<JWTRespond>

    @POST("auth/local")
    fun login(@Body signInData: SignInData): Call<JWTRespond>
}