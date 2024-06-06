package com.example.makka_pakka.main.webview

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.makka_pakka.MainActivity
import com.example.makka_pakka.MyApplication
import com.example.makka_pakka.R
import com.example.makka_pakka.sound_flex.GestureControlListener

class AudienceFragment : BaseWebviewFragment() {
    private val args: AudienceFragmentArgs by navArgs()
    private var upid = 0 //直播者的uid
    private var live_url = "" //直播地址
    private lateinit var gestureControlListener: GestureControlListener
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
        Log.d(
            "AudienceFragment",
            "url: ${MyApplication.instance.webViewUrlRepo.BASE_URL + MyApplication.instance.webViewUrlRepo.AUDIENCE + live_url}"
        )
        bind.webView.webChromeClient = MyApplication.instance.webChromeClient
        bind.webView.loadUrl(
            MyApplication.instance.webViewUrlRepo.BASE_URL +
                    MyApplication.instance.webViewUrlRepo.AUDIENCE
                    + live_url
        )//这里看看需不需要改
        thisWebView = bind.webView
//        (activity as MainActivity).volumeUp()
//        (activity as MainActivity).volumeDown()
//        (activity as MainActivity).brightnessUp()
//        (activity as MainActivity).brightnessDown()

        gestureControlListener = object : GestureControlListener {
            override fun onGestureControl(gesture: Int) {
                if (findNavController().currentDestination?.id != R.id.audienceFragment) {
                    return
                }
                when (gesture) {
                    1 -> {
                        //增大系统音量
                        (activity as MainActivity).volumeUp()
                    }

                    2 -> {
                        //减小系统音量
                        (activity as MainActivity).volumeDown()
                    }

                    3 -> {
                        //返回
                        doWhenBackPressed()
                    }

                    4 -> {
                        //提高亮度
                        (activity as MainActivity).brightnessUp()
                    }

                    5 -> {
                        //降低亮度
                        (activity as MainActivity).brightnessDown()
                    }
                }
            }
        }
        (activity as MainActivity).gestureController = gestureControlListener

        return bind.root
    }
}

