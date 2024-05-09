package com.example.makka_pakka.utils.gson

import android.util.Log
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

    inline fun <reified T> fromJsonToListResponse(
        json: String?,
        clazz: Class<T>
    ): MyResponse<List<T>> {
        try {
            if (json == null) {
                throw IllegalArgumentException("json is null")
            }
            val type = object : TypeToken<MyResponse<List<T>>>() {}.type
            return gson.fromJson(json, type)
        } catch (e: Exception) {
            Log.e("GsonUtil", "fromJsonToListResponse: $e")
        }
        return MyResponse<List<T>>(null, 0, "解析错误", listOf())
    }


}