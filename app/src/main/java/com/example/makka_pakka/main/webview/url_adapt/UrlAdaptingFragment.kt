package com.example.makka_pakka.main.webview.url_adapt

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.makka_pakka.databinding.FragmentUrlAdaptingBinding
import com.example.makka_pakka.utils.ViewUtil


class UrlAdaptingFragment : Fragment() {
    private lateinit var bind: FragmentUrlAdaptingBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        bind = FragmentUrlAdaptingBinding.inflate(layoutInflater)
        ViewUtil.paddingByStatusBar(bind.root)

        val adapter = UrlAdaptAdapter(listOf())

        return bind.root
    }

    companion object {
        const val BASE_URL = ""
        const val AUDIENCE = ""
        const val BROADCAST = ""
        const val MY_ROOM = ""
        const val ROOM = ""
    }
}