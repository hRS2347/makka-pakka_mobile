package com.example.makka_pakka.main.webview.bridge

import android.content.Context
import android.os.Handler
import android.util.Log
import android.webkit.JavascriptInterface
import android.widget.Toast
import com.example.makka_pakka.MyApplication
import com.example.makka_pakka.utils.CalendarReminderUtil
import com.example.makka_pakka.utils.ViewUtil
import com.google.gson.Gson

/*** js调用方法
var token = AndroidInterface.getToken();
var user = AndroidInterface.getUser();

// 使用获取到的 token 和 user 值进行后续处理
console.log("Token:", token);
console.log("User:", user)
 */
class JavaScriptInterface(
    private val context: Context,
    private val handler: Handler
) {
    //token
    @JavascriptInterface
    fun getToken(): String {
        // 返回您 的 token 值
        return MyApplication.instance.currentToken
    }

    //json格式的user
    @JavascriptInterface
    fun getUser(): String {
        // 返回您的 user 值
        return Gson().toJson(MyApplication.instance.currentUser.value)
    }

    @JavascriptInterface
    fun quit() {
        handler.sendEmptyMessage(1)
    }

    /**
     * 订阅直播
     */
    @JavascriptInterface
    fun subscribeNextBroadcast(timeInMills: Long, name: String): Unit {
        CalendarReminderUtil.addCalendarEvent(
            context,
            "直播提醒:你订阅的 $name 即将开始",
            timeInMills//开始时间, 单位为毫秒
        )
    }

    /**
     * Toast 提示
     */
    @JavascriptInterface
    fun showToast(msg: String, length: String) {
        Log.i("testShowToast", "showToast: $msg")
        Toast.makeText(context, msg, if (length == "long") Toast.LENGTH_LONG else Toast.LENGTH_SHORT).show();
    }

    /***
     * 返回键盘高度，rem单位
     */
    @JavascriptInterface
    fun getKeyboardHeight(): Float {
        return MyApplication.instance.keyboardHeight
    }


}