package com.example.makka_pakka.utils.gson

import com.example.makka_pakka.model.MyResponse
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

object GsonUtil {
    val gson = Gson()

    inline fun <reified T> fromJson(json: String?, clazz: Class<T>): MyResponse<T> {
        if (json == null) {
            throw IllegalArgumentException("json is null")
        }
        val type = object : TypeToken<MyResponse<T>>() {}.type
        return gson.fromJson(json, type)
    }
}