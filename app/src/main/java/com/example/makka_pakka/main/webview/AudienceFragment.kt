package com.example.makka_pakka.main.webview

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.JavascriptInterface
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.makka_pakka.MyApplication
import com.example.makka_pakka.databinding.FragmentWebviewBinding
import com.example.makka_pakka.main.webview.url_adapt.UrlAdaptingFragment
import com.example.makka_pakka.utils.ViewUtil
import com.example.makka_pakka.view.LoadingPic
import com.google.gson.Gson
import com.tencent.smtt.sdk.WebView

class AudienceFragment : Fragment() {
    private lateinit var bind: FragmentWebviewBinding
    val loadingPic = LoadingPic.newInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        bind = FragmentWebviewBinding.inflate(layoutInflater)
        ViewUtil.paddingByStatusBar(bind.coordinatorLayout)

        val handler = Handler(Handler.Callback {
            when (it.what) {
                1 -> {
                    findNavController().navigateUp()
                }
            }
            true
        })

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

        Toast.makeText(context, "观众页面", Toast.LENGTH_SHORT).show()


        bind.webView.loadUrl(MyApplication.instance.webViewUrlRepo.BASE_URL+
                MyApplication.instance.webViewUrlRepo.AUDIENCE)

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
    class JavaScriptInterface(
        private val context: Context, private val handler: Handler
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
    }
}

