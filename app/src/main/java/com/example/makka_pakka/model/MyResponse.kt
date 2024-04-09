package com.example.makka_pakka.model


data class MyResponse<T>(
    var host: String?,
    var code: Int?,
    var errorMessage: String?,
    var data: T
)

