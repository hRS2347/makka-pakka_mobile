package com.example.makka_pakka.boardcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.example.makka_pakka.MainActivity
import com.example.makka_pakka.MyApplication

class ReLoginReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        Log.d("relogin", "ReLoginReceiver:relogin")
        //重新登录
        if (MyApplication.instance.currentToken == "test_token")
            Toast.makeText(context,"请先登录", Toast.LENGTH_SHORT).show()
        else
            Toast.makeText(context, "登录过期，请重新登录", Toast.LENGTH_SHORT).show()
        (context as MainActivity).reLogin()
    }
}