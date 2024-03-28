package com.example.makka_pakka.model

import android.content.Entity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

data class MyResponse(
    var host: String?,
    var code: Int?,
    var errorMessage: String?,
    var data: UserInfo
){

}

