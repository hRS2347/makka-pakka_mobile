package com.example.makka_pakka.main.webview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.makka_pakka.MyApplication
class MyRoomFragment : BaseWebviewFragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)

        Toast.makeText(context, "我的直播", Toast.LENGTH_SHORT).show()

        bind.webView.loadUrl(MyApplication.instance.webViewUrlRepo.BASE_URL + MyApplication.instance.webViewUrlRepo.MY_ROOM)
        thisWebView = bind.webView

        return bind.root
    }



}

