package com.example.makka_pakka.main.webview

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.makka_pakka.MainActivity
import com.example.makka_pakka.MyApplication
import com.example.makka_pakka.OnPressBackListener
import com.example.makka_pakka.databinding.FragmentWebviewBinding
import com.example.makka_pakka.main.webview.bridge.JavaScriptInterface
import com.example.makka_pakka.utils.ViewUtil
import com.example.makka_pakka.view.LoadingPic
import com.tencent.smtt.sdk.WebView

abstract class BaseWebviewFragment : Fragment(), OnPressBackListener {
    lateinit var bind: FragmentWebviewBinding
    val loadingPic = LoadingPic.newInstance()
    lateinit var thisWebView: WebView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        bind = FragmentWebviewBinding.inflate(layoutInflater)
        ViewUtil.paddingByStatusBar(bind.coordinatorLayout)

        val handler = Handler {
            when (it.what) {
                1 -> {
                    findNavController().navigateUp()
                }
            }
            true
        }

        bind.webView.addJavascriptInterface(
            JavaScriptInterface(MyApplication.instance, handler),
            "AndroidInterface"
        )
        //先把加载图标显示出来
        childFragmentManager.beginTransaction().add(bind.linearLayout.id, loadingPic).commit()
        bind.webView.webViewClient = object : com.tencent.smtt.sdk.WebViewClient() {
            override fun onPageFinished(p0: WebView?, p1: String?) {
                super.onPageFinished(p0, p1)
                try {
                    //加载完成后，隐藏加载图标
                    childFragmentManager.beginTransaction().remove(loadingPic).commit()
                    bind.webView.visibility = View.VISIBLE
                } catch (e: Exception) {
                    Log.e("onPageFinished", e.message.toString())
                }

            }
        }

        return bind.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        try {
            (requireActivity() as MainActivity).addOnPressBackListener(this)
        } catch (e: Exception) {
            Log.e("onViewCreated", e.message.toString())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        try {
            (requireActivity() as MainActivity).removeOnPressBackListener(this)
        } catch (e: Exception) {
            Log.e("onDestroyView", e.message.toString())
        }

        //销毁webview，如果有需要
        thisWebView.destroy()
    }

    override fun doWhenBackPressed(){
        sendEventToWebPage("onClientBackPressed", "")
    }

    private fun sendEventToWebPage(eventName: String, eventData: String) {
        val jsCode = """
            (function() {
                var event = new CustomEvent('$eventName', { detail: '$eventData' });
                window.dispatchEvent(event);
            })();
        """.trimIndent()
        thisWebView.evaluateJavascript(jsCode, null)
    }
}