package com.example.makka_pakka.utils.gson

import com.example.makka_pakka.model.MyResponse
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object GsonUtil {
    val gson = Gson()

    inline fun <reified T> fromJsonToResponse(
        json: String?,
        clazz: Class<T>
    ): MyResponse<T> {
        if (json == null) {
            throw IllegalArgumentException("json is null")
        }
        val type = object : TypeToken<MyResponse<T>>() {}.type
        return gson.fromJson(json, type)
    }

    inline fun <reified T> fromJsonToMuList(
        json: String?,
        clazz: Class<T>
    ): List<T> {
        if (json == null) {
            throw IllegalArgumentException("json is null")
        }
        val type = object : TypeToken<MutableList<T>>() {}.type
        return gson.fromJson(json, type)
    }

}