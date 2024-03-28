package com.example.makka_pakka.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.makka_pakka.databinding.FragmentMainListBinding

class WebViewFragment : Fragment() {
    private lateinit var bind: FragmentMainListBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        bind = FragmentMainListBinding.inflate(layoutInflater)

        return bind.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bind.webView.loadUrl("www.bilibili.com")
    }
}

