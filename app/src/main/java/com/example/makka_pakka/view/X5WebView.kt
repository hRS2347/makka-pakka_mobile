package com.example.makka_pakka.view

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import com.tencent.smtt.sdk.WebSettings
import com.tencent.smtt.sdk.WebView
import com.tencent.smtt.sdk.WebViewClient


class X5WebView : WebView {
    private val client: WebViewClient = object : WebViewClient() {
        /**
         * 防止加载网页时调起系统浏览器
         */
        override fun shouldOverrideUrlLoading(view: WebView, url: String?): Boolean {
            if (url != null) {
                return if (url.startsWith("bilibili://video" )) {
                    true
                } else {
                    super.shouldOverrideUrlLoading(view, url)
                }
            }
            view.loadUrl(url)
            return true
        }
        override fun onReceivedError(
            view: WebView?,
            errorCode: Int,
            description: String?,
            failingUrl: String?
        ) {
           Log.e("X5WebView", "onReceivedError: $errorCode, $description, $failingUrl")
            super.onReceivedError(view, errorCode, description, failingUrl)
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    constructor(arg0: Context?, arg1: AttributeSet?) : super(arg0, arg1) {
        this.webViewClient = client
        // this.setWebChromeClient(chromeClient);
// WebStorage webStorage = WebStorage.getInstance();
        initWebViewSettings()
        this.view.isClickable = true
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun initWebViewSettings() {
        val webSetting: WebSettings = this.settings
        webSetting.javaScriptEnabled = true
        webSetting.javaScriptCanOpenWindowsAutomatically = true
        webSetting.allowFileAccess = true
        webSetting.layoutAlgorithm = WebSettings.LayoutAlgorithm.NARROW_COLUMNS
        webSetting.setSupportZoom(true)
        webSetting.builtInZoomControls = true
        webSetting.useWideViewPort = true
        webSetting.setSupportMultipleWindows(true)
        // webSetting.setLoadWithOverviewMode(true);
        webSetting.setAppCacheEnabled(true)
        // webSetting.setDatabaseEnabled(true);
        webSetting.domStorageEnabled = true
        webSetting.setGeolocationEnabled(true)
        webSetting.setAppCacheMaxSize(Long.MAX_VALUE)
        // webSetting.setPageCacheCapacity(IX5WebSettings.DEFAULT_CACHE_CAPACITY);
        webSetting.pluginState = WebSettings.PluginState.ON_DEMAND
        // webSetting.setRenderPriority(WebSettings.RenderPriority.HIGH);
        webSetting.cacheMode = WebSettings.LOAD_NO_CACHE
        webSetting.javaScriptEnabled = true // 启用 JavaScript
        webSetting.domStorageEnabled = true // 启用本地存储
        webSetting.allowFileAccess = true // 允许访问文件
        webSetting.allowContentAccess = true // 允许访问内容
        webSetting.setGeolocationEnabled(true) // 启用地理位置（可选）
        webSetting.mediaPlaybackRequiresUserGesture = false // 允许自动播放媒体（可选）
        //支持通过js打开新窗口
        webSetting.loadsImagesAutomatically = true; //支持自动加载图片
        //camera
// this.getSettingsExtension().setPageCacheCapacity(IX5WebSettings.DEFAULT_CACHE_CAPACITY);//extension
// settings 的设计
    }

    constructor(arg0: Context?) : super(arg0) {
        setBackgroundColor(85621)
    }

}


