package com.example.makka_pakka.network

import android.util.Log
import com.example.makka_pakka.MyApplication
import kotlinx.coroutines.flow.first
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.io.IOException


class TokenInterceptor : Interceptor {
    companion object {
        const val TAG = "TokenInterceptor"
    }
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request: Request = chain.request()
        val token: String = MyApplication.instance.getToken()
        Log.d(TAG, "token: $token")
        val newRequest: Request = request.newBuilder()
            .addHeader("Token", token)
            .build()
        return chain.proceed(newRequest)
    }
}