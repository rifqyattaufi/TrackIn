package com.example.trackin.respond

import com.google.gson.annotations.SerializedName
import java.sql.Time

class date_and_time_respond<T> {
    @SerializedName("data")
    var data: T? = null
}

class date_and_times {
    @SerializedName("id")
    var id: Int? = null

    @SerializedName("attributes")
    var attributes: attributes_dateTime? = attributes_dateTime()
}

class attributes_dateTime {
    @SerializedName("day")
    var day: String? = null

    @SerializedName("start")
    var start: String? = null

    @SerializedName("end")
    var end: String? = null

    @SerializedName("schedule")
    var schedule: ApiResponse<schedules>? = null
}