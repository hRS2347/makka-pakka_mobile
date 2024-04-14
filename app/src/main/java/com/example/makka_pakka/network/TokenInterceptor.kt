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
        val token: String = MyApplication.instance.currentToken ?: ""
        if (token.isNotBlank()){
            Log.d(TAG, "token: $token")
            val newRequest: Request = request.newBuilder()
                .addHeader("token", token)
                .build()
            Log.d(TAG, "newRequest: $newRequest")
            return chain.proceed(newRequest)
        }else{
            val newRequest: Request = request.newBuilder()
                .build()
            Log.d(TAG, "newRequest: $newRequest")
            return chain.proceed(newRequest)
        }
    }
}