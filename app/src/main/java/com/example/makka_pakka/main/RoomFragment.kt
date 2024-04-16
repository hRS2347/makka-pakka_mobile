package com.example.makka_pakka.main

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.JavascriptInterface
import androidx.fragment.app.Fragment
import com.example.makka_pakka.MyApplication
import com.example.makka_pakka.databinding.FragmentRoomBinding
import com.example.makka_pakka.utils.ViewUtil
import com.example.makka_pakka.view.LoadingPic
import com.google.gson.Gson
import com.tencent.smtt.sdk.WebView

class RoomFragment : Fragment() {
    private lateinit var bind: FragmentRoomBinding
    val loadingPic = LoadingPic.newInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        bind = FragmentRoomBinding.inflate(layoutInflater)
        ViewUtil.paddingByStatusBar(bind.coordinatorLayout)
        bind.webView.addJavascriptInterface(
            JavaScriptInterface(MyApplication.instance),
            "AndroidInterface"
        )
        //先把加载图标显示出来
        childFragmentManager.beginTransaction().add(bind.linearLayout.id, loadingPic).commit()
        bind.webView.webViewClient = object : com.tencent.smtt.sdk.WebViewClient() {
            override fun onPageFinished(p0: WebView?, p1: String?) {
                super.onPageFinished(p0, p1)
                //加载完成后，隐藏加载图标
                childFragmentManager.beginTransaction().remove(loadingPic).commit()
                bind.webView.visibility = View.VISIBLE
                Log.d("RoomFragment", "onPageFinished: ")
            }
        }


        bind.webView.loadUrl("http://bilibili.com")

        return bind.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }


    /*** js调用方法
    var token = AndroidInterface.getToken();
    var user = AndroidInterface.getUser();

    // 使用获取到的 token 和 user 值进行后续处理
    console.log("Token:", token);
    console.log("User:", user)
     */
    class JavaScriptInterface(private val context: Context) {

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
            return Gson().toJson(MyApplication.instance.currentUser)
        }
    }
}

