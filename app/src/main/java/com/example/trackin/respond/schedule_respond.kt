package com.example.trackin.respond

class schedule_respond<T> {
    var data: T? = null
}

class schedules {
    var id: Int? = null

    var attributes: attributes_schedule? = attributes_schedule()
}

class attributes_schedule {
    var title: String? = null

    var room: String? = null

    var date_and_times: ApiResponse<List<date_and_times>>? = null
}