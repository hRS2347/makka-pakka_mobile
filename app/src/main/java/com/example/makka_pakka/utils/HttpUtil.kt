package com.example.makka_pakka.utils

import android.util.Log
import com.example.makka_pakka.AVATAR
import com.example.makka_pakka.IND_CODE
import com.example.makka_pakka.LOGIN
import com.example.makka_pakka.REGISTER
import com.example.makka_pakka.RESET
import com.example.makka_pakka.SEND_HABITS
import com.example.makka_pakka.USER_INFO
import com.example.makka_pakka.host
import com.example.makka_pakka.model.UserInfo
import com.google.gson.Gson
import okhttp3.Callback
import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.FormBody
import okhttp3.HttpUrl
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.util.concurrent.TimeUnit

//单例模式，OKHttp3
object HttpUtil {
    val cookieMap = HashMap<String, List<Cookie>>()
    val gson = Gson()

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


    // 登录
    fun login(email: String, password: String, callback: Callback) {
        // 创建请求
        val request = Request.Builder()
            .url("$host$LOGIN?email=$email&password=$password")
            .post(FormBody.Builder().build()).build().let {
                Log.d("LoginFragment", "login: $it")
                it
            }
        // 发送请求
        client.newCall(request).enqueue(callback)
    }

    // 获取验证码
    fun requestIndCode(email: String, callback: Callback) {
        val request = Request.Builder()
            .url("$host$IND_CODE?email=$email")
            .get().build().let {
                Log.d("LoginFragment", "requestIndCode: $it")
                it
            }
        client.newCall(request).enqueue(callback)
    }

    // 注册
    fun register(email: String, password: String, code: String, callback: Callback) {
        val request = Request.Builder()
            .url("$host$REGISTER?email=$email&password=$password&code=$code")
            .post(FormBody.Builder().build()).build().let {
                Log.d("LoginFragment", "register: $it")
                it
            }
        client.newCall(request).enqueue(callback)
    }

    // 重置密码
    fun reset(email: String, password: String, code: String, callback: Callback) {
        val request = Request.Builder()
            .url("$host$RESET?email=$email&password=$password&code=$code")
            .put(FormBody.Builder().build()).build().let {
                Log.d("LoginFragment", "reset: $it")
                it
            }
        client.newCall(request).enqueue(callback)
    }

    /***
     * post habits
     */
    fun sendHabits(habitsList: ArrayList<String>, callback: Callback) {
        val habits = gson.toJson(habitsList)
        val body = habits.toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
        val request = Request.Builder()
            .url("$host$SEND_HABITS")
            .post(body).build().let {
                Log.d("LoginFragment", "sendHabits: $it")
                it
            }
        client.newCall(request).enqueue(callback)
    }

    // 更新用户信息
    fun updateUserInfo(value: UserInfo, callback: Callback) {
        //put
        val json = gson.toJson(value)
        val body = json.toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
        val request = Request.Builder().url("$host$USER_INFO").put(body).build()
        client.newCall(request).enqueue(callback)
    }


    //上传头像
    fun uploadAvatar(path: String, callback: Callback) {
        val file = File(path)
        val body = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart(
                "file",
                file.name,
                file.asRequestBody("image/*".toMediaTypeOrNull())
            )
            .build()
        val request = Request.Builder()
            .url("$host$AVATAR")
            .post(body)
            .build()
        client.newCall(request).enqueue(callback)
    }
}