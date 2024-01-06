package com.example.trackin.data

class TaskDataWrapper(
    val data: TaskData
)

class TaskData(
    val title: String,
    val deadline: String,
    val status: Boolean,
    val schedule: Int,
    val users_permissions_user: Int
)