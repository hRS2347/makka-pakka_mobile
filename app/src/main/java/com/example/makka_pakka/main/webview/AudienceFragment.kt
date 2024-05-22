package com.example.makka_pakka.main.webview

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.navArgs
import com.example.makka_pakka.LOGIN
import com.example.makka_pakka.MyApplication

class AudienceFragment : BaseWebviewFragment() {
    private val args: AudienceFragmentArgs by navArgs()
    private var upid = 0 //直播者的uid
    private var live_url = "" //直播地址
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        //获取到lid
        try {
            upid = args.upid
            live_url = args.liveUrl
            Log.i("AudienceFragment", "live_url$live_url  upid=$upid")
        } catch (e: Exception) {
            e.printStackTrace()
        }

        Toast.makeText(context, "观众页面", Toast.LENGTH_SHORT).show()

        bind.webView.loadUrl(
            MyApplication.instance.webViewUrlRepo.BASE_URL +
                    MyApplication.instance.webViewUrlRepo.AUDIENCE
                    + live_url
        )//这里看看需不需要改

        thisWebView = bind.webView

        return bind.root
    }

}

