package com.example.trackin.data

import com.google.gson.annotations.SerializedName

class DateAndTimeDataWrapper(
    @SerializedName("data")
    var dateAndTimesData: DateAndTimesData
)

class DateAndTimesData(
    var day: String,
    var start: String,
    var end: String,
    var schedule: Int
)