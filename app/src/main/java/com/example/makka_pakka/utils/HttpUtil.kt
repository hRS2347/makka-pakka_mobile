package com.example.makka_pakka.utils

import okhttp3.Callback
import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.util.concurrent.TimeUnit

//单例模式，OKHttp3
object HttpUtil {
    val cookieMap = HashMap<String, List<Cookie>>()
    // 基本的网络请求
    private var client: OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(10, TimeUnit.SECONDS) // 设置连接超时为10秒
        .readTimeout(30, TimeUnit.SECONDS) // 设置读取超时为30秒
        .writeTimeout(15, TimeUnit.SECONDS) // 设置写入超时为15秒
        .cookieJar(
            object : CookieJar {
                override fun loadForRequest(url: HttpUrl): List<Cookie> {
                    return cookieMap[url.host] ?: ArrayList<Cookie>()
                }

                override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
                    cookieMap[url.host] = cookies
                }

            }).build()

    fun get(url: String, callback: Callback) {
        val request = Request.Builder().url(url).build()
        client.newCall(request).enqueue(callback)
    }

    fun post(url: String, json: String, callback: Callback) {
        val body = json.toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
        val request = Request.Builder().url(url).post(body).build()
        client.newCall(request).enqueue(callback)
    }

}