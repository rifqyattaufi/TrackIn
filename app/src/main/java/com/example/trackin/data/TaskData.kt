package com.example.trackin.data

class TaskDataWrapper(
    val data: TaskData
)

class TaskData(
    val title: String? = null,
    val deadline: String? = null,
    val status: Boolean = false,
    val schedule: Int? = null,
    val users_permissions_user: Int? = null
)