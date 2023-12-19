package com.example.trackin.respond

import com.google.gson.annotations.SerializedName


class JWTRespond {
    @SerializedName("jwt")
    var jwt: String = ""

    @SerializedName("user")
    var user: user_data? = null
}

class user_data {
    @SerializedName("id")
    var id: Int? = null
}

data class ApiResponse<T>(
    @SerializedName("data")
    var data: T? = null
)