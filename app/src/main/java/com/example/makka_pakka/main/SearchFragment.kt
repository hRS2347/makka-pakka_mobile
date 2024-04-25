package com.example.makka_pakka.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.makka_pakka.databinding.FragmentSearchBinding
import com.example.makka_pakka.main.MainViewModel
import com.example.makka_pakka.utils.ViewUtil
import com.example.makka_pakka.view.HistorySearchContainer

class SearchFragment : Fragment() {
    private lateinit var bind: FragmentSearchBinding
    private val viewModel: SearchViewModel by viewModels { SearchViewModel.Factory }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        bind = FragmentSearchBinding.inflate(layoutInflater)
        ViewUtil.paddingByStatusBar(bind.coordinatorLayout)
        viewModel.historySearch.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), it.toString(), Toast.LENGTH_SHORT).show()
            bind.historySearchContainer.sumChipsList = it
        }
        bind.historySearchContainer.onChipClickListener = object:HistorySearchContainer.OnChipClickListener{
            override fun onChipClick(chip: String) {
                bind.editText.setText(chip)
                bind.tvSearch.performClick()
            }
        }
        bind.tvSearch.setOnClickListener {
            bind.editText.text.toString().let { s ->
                if (s.isBlank()) {
                    Toast.makeText(requireContext(), "请输入搜索内容", Toast.LENGTH_SHORT).show()
                } else
                    //TODO:SEARCH
                    viewModel.saveHistorySearch(s)
            }
        }

        bind.ivClearHistory.setOnClickListener {
            viewModel.clearHistorySearch()
        }
        bind.ivBack.setOnClickListener {
            findNavController().popBackStack()
        }

        bind.ivClear.setOnClickListener{
            bind.editText.text.clear()
        }

        bind.editText.doOnTextChanged { text, _, _, _ ->
            //调用时机：在文本变化时，会回调该方法
            //text：变化后的文本内容
            //如果为空
            if (text.isNullOrBlank()) {
                bind.matchLayout.visibility = View.GONE
                bind.initSearchLayout.visibility = View.VISIBLE
                bind.ivClear.visibility = View.GONE
            } else {
                bind.matchLayout.visibility = View.VISIBLE
                bind.initSearchLayout.visibility = View.GONE
                bind.ivClear.visibility = View.VISIBLE
            }
        }
        return bind.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}