package com.example.trackin.respond

import com.google.gson.annotations.SerializedName


class JWTRespond {
    @SerializedName("jwt")
    var jwt: String = ""

    @SerializedName("error")
    var error: Error? = null

    fun get_Error(): Error? {
        return error
    }

    fun set_Error(error: Error?) {
        this.error = error
    }
}

class Error {
    private var message: String? = null

    fun getMessage(): String? {
        return message
    }

    fun setMessage(message: String?) {
        this.message = message
    }
}