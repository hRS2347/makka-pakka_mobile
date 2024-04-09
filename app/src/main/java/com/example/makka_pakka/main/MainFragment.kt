package com.example.makka_pakka.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.makka_pakka.MainActivity
import com.example.makka_pakka.MyApplication
import com.example.makka_pakka.R
import com.example.makka_pakka.databinding.FragmentMainBinding


class MainFragment : Fragment() {
    private lateinit var bind: FragmentMainBinding
    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        bind = FragmentMainBinding.inflate(layoutInflater)

        return bind.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        if (!(activity as MainActivity).isHobbySelectedAsk &&
            MyApplication.instance.currentUser.value!=null &&
            MyApplication.instance.currentUser.value!!.isHobbySelected != 1
        ) {//未选择兴趣
            findNavController().navigate(R.id.action_mainFragment_to_initiationFragment)
        }
    }
}

