package com.example.makka_pakka.main

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.makka_pakka.R
import com.example.makka_pakka.databinding.FragmentMineBinding
import com.example.makka_pakka.view.X5WebView

class MineFragment : Fragment() {
    private lateinit var bind: FragmentMineBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        bind = FragmentMineBinding.inflate(layoutInflater)
        bind.tvMine.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_mineFragment_to_mineDetailFragment)
        }
        return bind.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

}

