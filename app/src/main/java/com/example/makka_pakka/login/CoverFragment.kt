package com.example.makka_pakka.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.makka_pakka.MyApplication
import com.example.makka_pakka.R
import com.example.makka_pakka.databinding.FragmentCoverBinding
import com.example.makka_pakka.utils.ViewUtil

class CoverFragment : Fragment() {
    private lateinit var bind: FragmentCoverBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        bind = FragmentCoverBinding.inflate(layoutInflater)
        ViewUtil.paddingByStatusBar(bind.root)
        bind.btnLogin.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_coverFragment_to_loginFragment)
        }
        bind.btnRegister.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_coverFragment_to_registerFragment)
        }

        //两个按钮1s后浮现
        bind.btnLogin.alpha = 0f
        bind.btnRegister.alpha = 0f
        bind.btnLogin.animate().alpha(1f).setDuration(1000).start()
        bind.btnRegister.animate().alpha(1f).setDuration(1000).start()

        bind.ivLogo.setOnClickListener {
            MyApplication.instance.currentUser.value =MyApplication.instance.testUser
            Navigation.findNavController(it).navigate(R.id.action_coverFragment_to_mainFragment)
        }

        bind.tvEdit.setOnClickListener {
            MyApplication.instance.currentUser.value =MyApplication.instance.testUser
            Navigation.findNavController(it).navigate(R.id.action_coverFragment_to_urlAdaptingFragment)
        }
        return bind.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }


}

