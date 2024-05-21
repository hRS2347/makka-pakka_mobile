package com.example.makka_pakka.utils

import android.util.Log
import com.example.makka_pakka.AVATAR
import com.example.makka_pakka.GET_USER_INFO
import com.example.makka_pakka.IND_CODE
import com.example.makka_pakka.LOGIN
import com.example.makka_pakka.RECOMMENDATION
import com.example.makka_pakka.RECOMMENDATION_PORT
import com.example.makka_pakka.REGISTER
import com.example.makka_pakka.RESET
import com.example.makka_pakka.SEARCH_CONTENT
import com.example.makka_pakka.SEARCH_MATCH
import com.example.makka_pakka.SEND_HABITS
import com.example.makka_pakka.USER_INFO
import com.example.makka_pakka.host
import com.example.makka_pakka.model.UserInfo
import com.example.makka_pakka.network.LoggingInterceptor
import com.example.makka_pakka.network.ResponseInterceptor
import com.example.makka_pakka.network.TokenInterceptor
import com.example.makka_pakka.port
import com.example.makka_pakka.search_port
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
    private var client: OkHttpClient? = null

    fun setUp(callLogin: (Unit) -> Unit) {
        Log.d("HttpUtil", "setUp")
        this.client = OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS) // 设置连接超时为10秒
            .readTimeout(30, TimeUnit.SECONDS) // 设置读取超时为30秒
            .writeTimeout(15, TimeUnit.SECONDS) // 设置写入超时为15秒
            .addInterceptor(
                LoggingInterceptor()
            )
            .addInterceptor(
                TokenInterceptor()
            )
            .addInterceptor(
                ResponseInterceptor(callLogin)
            )
            .cookieJar(
                object : CookieJar {
                    override fun loadForRequest(url: HttpUrl): List<Cookie> {
                        return cookieMap[url.host] ?: ArrayList<Cookie>()
                    }

                    override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
                        cookieMap[url.host] = cookies
                    }

                }).build()
    }

    fun get(url: String, callback: Callback) {
        val request = Request.Builder().url(url).build()
        client?.newCall(request)?.enqueue(callback)
    }

    fun getSynchronous(url: String): String = try {
        val request = Request.Builder().url(url).build()
        val response = client?.newCall(request)?.execute()
        response?.body?.string() ?: ""
    } catch (e: Exception) {
        e.printStackTrace()
        ""
    }


    fun post(url: String, json: String, callback: Callback) {
        val body = json.toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
        val request = Request.Builder().url(url).post(body).build()
        client?.newCall(request)?.enqueue(callback)
    }


    // 登录
    fun login(email: String, password: String, callback: Callback) {
        // 创建请求
        val request = Request.Builder()
            .url("$host:$port$LOGIN?email=$email&password=$password")
            .post(FormBody.Builder().build()).build().let {
                Log.d("LoginFragment", "login: $it")
                it
            }
        // 发送请求
        client?.newCall(request)?.enqueue(callback)
    }

    // 获取验证码
    fun requestIndCode(email: String, callback: Callback) {
        val request = Request.Builder()
            .url("$host:$port$IND_CODE?email=$email")
            .get().build().let {
                Log.d("LoginFragment", "requestIndCode: $it")
                it
            }
        client?.newCall(request)?.enqueue(callback)
    }

    // 注册
    fun register(email: String, password: String, code: String, callback: Callback) {
        val request = Request.Builder()
            .url("$host:$port$REGISTER?email=$email&password=$password&code=$code")
            .post(FormBody.Builder().build()).build().let {
                Log.d("LoginFragment", "register: $it")
                it
            }
        client?.newCall(request)?.enqueue(callback)
    }

    // 重置密码
    fun reset(email: String, password: String, code: String, callback: Callback) {
        val request = Request.Builder()
            .url("$host:$port$RESET?email=$email&password=$password&code=$code")
            .put(FormBody.Builder().build()).build().let {
                Log.d("LoginFragment", "reset: $it")
                it
            }
        client?.newCall(request)?.enqueue(callback)
    }

    /***
     * post habits
     */
    fun sendHabits(habitsList: ArrayList<String>, callback: Callback) {
        val habits = gson.toJson(habitsList)
        val body = habits.toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
        val request = Request.Builder()
            .url("$host:$port$SEND_HABITS")
            .post(body).build().let {
                Log.d("LoginFragment", "sendHabits: $it")
                it
            }
        client?.newCall(request)?.enqueue(callback)
    }

    // 更新用户信息
    fun updateUserInfo(value: UserInfo, callback: Callback) {
        //put
        val json = gson.toJson(value)
        val body = json.toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
        val request = Request.Builder().url("$host:$port$USER_INFO").put(body).build()
        client?.newCall(request)?.enqueue(callback)
    }


    //上传头像
    fun uploadAvatar(path: String, callback: Callback) {
        try {
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
                .url("$host:$port$AVATAR")
                .post(body)
                .build()
            client?.newCall(request)?.enqueue(callback)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getUserInfo(callback: Callback) {
        get("$host:$port$GET_USER_INFO", callback)
    }

    fun tokenTest(callback: Callback) {
        get("https://eoh1bkxhotv1atz.m.pipedream.net", callback)
    }

    fun searchMatch(key: String, callback: Callback) {
        get("$host:$search_port$SEARCH_MATCH/$key", callback)
    }

    //搜索
    fun search(key: String, pageIndex: Int, type: Int,callback: Callback) =
        get("$host:$search_port$SEARCH_CONTENT/$key/$pageIndex/30/$type", callback)


    val size = 10
    //推荐直播,用于首页，size为推荐的数量
    fun recommendation(callback: Callback) =
        get("$host:$RECOMMENDATION_PORT$RECOMMENDATION",callback)

}