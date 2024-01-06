package com.example.trackin.respond

class task_respond<T> {
    var data: T? = null
}

class tasks {
    var id: Int? = null

    var attributes: attributes_task? = attributes_task()
}

class attributes_task {
    var title: String? = null

    var deadline: String? = null

    var schedule: ApiResponse<schedules>? = null

    var status: Boolean? = null
}