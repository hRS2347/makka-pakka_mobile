package com.example.makka_pakka.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.makka_pakka.R
import com.example.makka_pakka.databinding.FragmentCoverBinding

class CoverFragment : Fragment() {
    private lateinit var bind: FragmentCoverBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
//        requireActivity().window.statusBarColor =
//            ResourcesCompat.getColor(resources, R.color.background_color, null)
        bind = FragmentCoverBinding.inflate(layoutInflater)
        bind.btnLogin.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_coverFragment_to_loginFragment)
        }
        bind.btnRegister.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_coverFragment_to_registerFragment)
        }
        return bind.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onDestroyView() {
        super.onDestroyView()
//        requireActivity().window.statusBarColor =
//            ResourcesCompat.getColor(resources, R.color.white, null)

    }
}

