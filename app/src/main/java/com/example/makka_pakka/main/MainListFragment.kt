package com.example.makka_pakka.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.makka_pakka.R
import com.example.makka_pakka.databinding.FragmentCoverBinding
import com.example.makka_pakka.databinding.FragmentMainListBinding
import com.example.makka_pakka.databinding.FragmentMineBinding

class MainListFragment : Fragment() {
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

    }
}

