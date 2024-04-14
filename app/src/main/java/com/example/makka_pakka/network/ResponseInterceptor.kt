package com.example.makka_pakka.network

import android.content.Intent
import android.util.Log
import com.example.makka_pakka.LOGIN
import com.example.makka_pakka.MyApplication
import com.example.makka_pakka.REGISTER
import com.example.makka_pakka.model.MyResponse
import com.example.makka_pakka.utils.gson.GsonUtil
import com.google.gson.Gson
import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import java.io.IOException
import kotlin.reflect.KFunction0

class ResponseInterceptor(private var callLogin: (Unit) -> Unit) : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        //发送广播通知
        val originalResponse = chain.proceed(chain.request())
        // 在这里执行你的拦截操作
        val responseBody = originalResponse.body
        val responseBodyString = responseBody?.string()

        Log.d("relogin", "ResponseInterceptor:response: $responseBodyString")
        try {
            val myResponse = Gson().fromJson(responseBodyString, MyResponse::class.java)
            //如果code==403，说明token过期，需要重新登录
            if (myResponse.code == 403) {
                //发送广播通知
                callLogin(Unit)
            }
            //保存token，从响应头中获取，仅登录、注册会触发
            //通过URL判断是否是登录、注册接口
            if (myResponse.code == 200){
                val url = originalResponse.request.url.toString()
                if (url.contains(LOGIN) ) {
                    GsonUtil.fromJson(responseBodyString, String::class.java).data?.let {
                        val token = it.toString()
                        Log.d("NEW_TOKEN",token.toString())
                        MyApplication.instance.saveToken(token)
                    }
                }
            }

            // 构造一个新的响应，返回给调用方
            return originalResponse.newBuilder()
                .body(responseBodyString?.toResponseBody(responseBody.contentType()))
                .build()
        } catch (e: Exception) {
            Log.e("ResponseInterceptor", "error: ${e.message}")
            return originalResponse.newBuilder()
                .body(responseBodyString?.toResponseBody(responseBody.contentType()))
                .build()
        }

    }
}