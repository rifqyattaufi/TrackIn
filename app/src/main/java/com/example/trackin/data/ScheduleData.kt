package com.example.trackin.data

import com.google.gson.annotations.SerializedName

class ScheduleDataWrapper(
    @SerializedName("data")
    val scheduleData: ScheduleData
)

class ScheduleData(
    val title: String,
    val room: String,
    val users_permissions_user: Int
)