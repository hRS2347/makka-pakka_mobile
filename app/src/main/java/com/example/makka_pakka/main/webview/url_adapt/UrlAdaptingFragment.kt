package com.example.makka_pakka.main.webview.url_adapt

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.makka_pakka.MyApplication
import com.example.makka_pakka.databinding.FragmentUrlAdaptingBinding
import com.example.makka_pakka.utils.ViewUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.coroutineContext
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


class UrlAdaptingFragment : Fragment() {
    private lateinit var bind: FragmentUrlAdaptingBinding
    private lateinit var adapter: UrlAdaptAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        bind = FragmentUrlAdaptingBinding.inflate(layoutInflater)
        ViewUtil.paddingByStatusBar(bind.root)

        adapter = UrlAdaptAdapter(listOf())
        adapter.listener = object : UrlAdaptAdapter.OnItemClickListener {
            override fun onItemClick(position: Int, url: String) {
                Toast.makeText(context, url, Toast.LENGTH_SHORT).show()
                lifecycleScope.launch {
                    withContext(Dispatchers.IO) {
                        MyApplication.instance.webViewUrlRepo.setUrl(
                            adapter.urls[position].key,
                            url
                        )
                    }
                }
            }
        }
        bind.rv.adapter = adapter
        refresh()

        return bind.root
    }

    private fun refresh() {
        lifecycleScope.launch {
            val list = mutableListOf<UrlKeyValue>()
            MyApplication.instance.webViewUrlRepo.list.forEach() {
                val url = MyApplication.instance.webViewUrlRepo.getUrl(it)
                list.add(UrlKeyValue(it, url))
            }
            withContext(Dispatchers.Main) {
                adapter.urls = list
            }
        }
    }


}