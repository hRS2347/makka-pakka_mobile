package com.example.makka_pakka.main.webview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.navArgs
import com.example.makka_pakka.MyApplication

class RoomFragment : BaseWebviewFragment() {
    private val args: RoomFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        Toast.makeText(context, args.id, Toast.LENGTH_SHORT).show()

        /***
         * args.id 为房间id
         */

        bind.webView.loadUrl(MyApplication.instance.webViewUrlRepo.BASE_URL + MyApplication.instance.webViewUrlRepo.ROOM + args.id)
        thisWebView = bind.webView
        return bind.root
    }


}

