package com.example.makka_pakka.utils

import android.util.Log
import com.example.makka_pakka.AVATAR
import com.example.makka_pakka.GET_USER_INFO
import com.example.makka_pakka.IND_CODE
import com.example.makka_pakka.LOGIN
import com.example.makka_pakka.REGISTER
import com.example.makka_pakka.RESET
import com.example.makka_pakka.SEND_HABITS
import com.example.makka_pakka.USER_INFO
import com.example.makka_pakka.host
import com.example.makka_pakka.model.UserInfo
import com.example.makka_pakka.port
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
//        .addInterceptor(
//            TokenInterceptor()
//        )
        .cookieJar(
            object : CookieJar {
                override fun loadForRequest(url: HttpUrl): List<Cookie> {
                    return cookieMap[url.host] ?: ArrayList<Cookie>()
                }

                override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
                    cookieMap[url.host] = cookies
                }

            }).build()

//    class TokenInterceptor : Interceptor {
//        @Throws(IOException::class)
//        override fun intercept(chain: Chain): Response {
//            val request = chain.request()
//            val response = chain.proceed(request)
//            Log.d(TAG, "response.code=" + response.code)
//
//            //根据和服务端的约定判断token过期
//            if (isTokenExpired(response)) {
//                Log.d(TAG, "自动刷新Token,然后重新请求数据")
//                //同步请求方式，获取最新的Token
//                val newToken = getNewToken()
//                //使用新的Token，创建新的请求
//                val newRequest = chain.request()
//                    .newBuilder()
//                    .header("Authorization", "Basic $newToken")
//                    .build()
//                //重新请求
//                return chain.proceed(newRequest)
//            }
//            return response
//        }
//
//        /**
//         * 根据Response，判断Token是否失效
//         *
//         * @param response
//         * @return
//         */
//        private fun isTokenExpired(response: Response): Boolean {
//            return if (response.code == 301) {
//                true
//            } else false
//        }
//
//        /**
//         * 同步请求方式，获取最新的Token
//         *
//         * @return
//         */
//        @Throws(IOException::class)
//        private fun getNewToken(): String {
//            // 通过获取token的接口，同步请求接口
//            return ""
//        }
//
//        companion object {
//            private const val TAG = "TokenInterceptor"
//        }
//    }


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
            .url("$host:$port$LOGIN?email=$email&password=$password")
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
            .url("$host:$port$IND_CODE?email=$email")
            .get().build().let {
                Log.d("LoginFragment", "requestIndCode: $it")
                it
            }
        client.newCall(request).enqueue(callback)
    }

    // 注册
    fun register(email: String, password: String, code: String, callback: Callback) {
        val request = Request.Builder()
            .url("$host:$port$REGISTER?email=$email&password=$password&code=$code")
            .post(FormBody.Builder().build()).build().let {
                Log.d("LoginFragment", "register: $it")
                it
            }
        client.newCall(request).enqueue(callback)
    }

    // 重置密码
    fun reset(email: String, password: String, code: String, callback: Callback) {
        val request = Request.Builder()
            .url("$host:$port$RESET?email=$email&password=$password&code=$code")
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
            .url("$host:$port$SEND_HABITS")
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
        val request = Request.Builder().url("$host:$port$USER_INFO").put(body).build()
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
            .url("$host:$port$AVATAR")
            .post(body)
            .build()
        client.newCall(request).enqueue(callback)
    }

    fun refreshUserInfo(callback: Callback) {
        get("$host:$port$GET_USER_INFO", callback)
    }
}