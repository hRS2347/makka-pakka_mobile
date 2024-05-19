package com.example.makka_pakka.main.webview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.makka_pakka.MyApplication

class AudienceFragment : BaseWebviewFragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)

        Toast.makeText(context, "观众页面", Toast.LENGTH_SHORT).show()

        bind.webView.loadUrl(MyApplication.instance.webViewUrlRepo.BASE_URL+
                MyApplication.instance.webViewUrlRepo.AUDIENCE)

        return bind.root
    }
}

