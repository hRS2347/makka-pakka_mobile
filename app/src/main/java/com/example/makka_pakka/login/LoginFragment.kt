package com.example.makka_pakka.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.example.makka_pakka.R
import com.example.makka_pakka.databinding.FragmentLoginBinding
import com.example.makka_pakka.utils.GlideUtil

class LoginFragment : Fragment() {
    private lateinit var bind: FragmentLoginBinding
    private val viewModel: LoginViewModel by viewModels { LoginViewModel.Factory }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        bind = FragmentLoginBinding.inflate(layoutInflater)

        viewModel.isLoading.observe(viewLifecycleOwner) {

        }

        bind.btnSubmit.setOnClickListener {
//            viewModel.isLoading.value = !(viewModel.isLoading.value)!!
            Navigation.findNavController(it).navigate(R.id.action_loginFragment_to_mainFragment)
        }

        bind.tvJumpSwitch.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_loginFragment_to_registerFragment)
        }
        bind.tvJumpReset.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_loginFragment_to_resetFragment)
        }
        bind.topAppBar.setNavigationOnClickListener {
            Navigation.findNavController(it).navigateUp()
        }
        return bind.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}

