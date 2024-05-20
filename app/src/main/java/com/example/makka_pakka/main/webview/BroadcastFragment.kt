package com.example.makka_pakka.main.webview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import com.example.makka_pakka.MyApplication

class BroadcastFragment : BaseWebviewFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(
            this,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    Toast.makeText(context, "即将停止", Toast.LENGTH_SHORT).show()
                }
            })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)

        bind.webView.webChromeClient = MyApplication.instance.webChromeClient
        bind.webView.loadUrl(MyApplication.instance.webViewUrlRepo.BASE_URL + MyApplication.instance.webViewUrlRepo.BROADCAST)
//        bind.webView.loadUrl("https://www.baidu.com")
        thisWebView = bind.webView

        return bind.root
    }



}

