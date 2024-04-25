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
import com.example.makka_pakka.utils.GlideUtil
import com.example.makka_pakka.utils.ViewUtil


class MainFragment : Fragment() {
    private lateinit var bind: FragmentMainBinding
    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        bind = FragmentMainBinding.inflate(layoutInflater)
        ViewUtil.paddingByStatusBar(bind.coordinatorLayout)
        bind.searchBar.setOnClickListener{
            findNavController().navigate(R.id.action_mainFragment_to_searchFragment)
        }

        bind.tvToAudi.setOnClickListener {
            findNavController().navigate(R.id.action_global_audienceFragment)
        }

        bind.tvToRoom.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_roomFragment)
        }
        bind.tvToUserInfo.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_userInfoFragment)
        }
        MyApplication.instance.currentUser.observe(viewLifecycleOwner) {
            if (it != null) {
                GlideUtil.loadAvatar(bind.ivAvatar, it.avatarUrl?:"")
            }
        }
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

