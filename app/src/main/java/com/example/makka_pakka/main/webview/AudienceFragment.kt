package com.example.makka_pakka.main.webview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.navArgs
import com.example.makka_pakka.MyApplication

class AudienceFragment : BaseWebviewFragment() {
    private val args: AudienceFragmentArgs by navArgs()
    private var lid = 0 //直播id
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        //获取到lid
        try {
            lid = args.lid
        } catch (e: Exception) {
            e.printStackTrace()
        }

        Toast.makeText(context, "观众页面", Toast.LENGTH_SHORT).show()

        bind.webView.loadUrl(
            MyApplication.instance.webViewUrlRepo.BASE_URL +
                    MyApplication.instance.webViewUrlRepo.AUDIENCE
        )

        thisWebView = bind.webView

        return bind.root
    }

}

