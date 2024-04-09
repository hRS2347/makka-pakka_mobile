package com.example.makka_pakka.network

import android.content.Context
import android.content.Intent
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.makka_pakka.LOGIN
import com.example.makka_pakka.MyApplication
import com.example.makka_pakka.REGISTER
import com.example.makka_pakka.model.MyResponse
import com.google.gson.Gson
import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import java.io.IOException

class ResponseInterceptor: Interceptor {
    var context: Context? = null
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalResponse = chain.proceed(chain.request())
        // 在这里执行你的拦截操作
        val responseBody = originalResponse.body
        val responseBodyString = responseBody?.string()
        val myResponse = Gson().fromJson(responseBodyString, MyResponse::class.java)
        //如果code==403，说明token过期，需要重新登录
        if (myResponse.code == 403) {
            //发送广播通知
            val intent = Intent("com.example.makka_pakka.ACTION_RELOGIN")
            if (context != null) {
                LocalBroadcastManager.getInstance(context!!).sendBroadcast(intent)
            }
        }
        //保存token，从响应头中获取，仅登录、注册会触发
        //通过URL判断是否是登录、注册接口
        val url = originalResponse.request.url.toString()
        if (url.contains(LOGIN) || url.contains(REGISTER)) {
            val token = originalResponse.header("token")
            if (token != null) {
                MyApplication.instance.saveToken(token)
            }
        }

        // 构造一个新的响应，返回给调用方
        return originalResponse.newBuilder()
            .body(responseBody?.byteString()?.toResponseBody(responseBody.contentType()))
            .build()
    }
}