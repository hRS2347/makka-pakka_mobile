package com.example.makka_pakka.boardcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.example.makka_pakka.MainActivity

class ReLoginReceiver: BroadcastReceiver(){
    override fun onReceive(context: Context, intent: Intent) {
        //重新登录
        Toast.makeText(context, "登录过期，请重新登录", Toast.LENGTH_SHORT).show()
        (context as MainActivity).reLogin()
    }
}