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
import com.example.makka_pakka.databinding.FragmentRegisterBinding
import com.example.makka_pakka.utils.GlideUtil

class RegisterFragment : Fragment() {
    private lateinit var bind: FragmentRegisterBinding
    private val viewModel: RegisterViewModel by viewModels { RegisterViewModel.Factory }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        bind = FragmentRegisterBinding.inflate(layoutInflater)
//        viewModel.isEmail.observe(viewLifecycleOwner) {
//            if (it) {
//                bind.phoneField.visibility = View.GONE
//                bind.emailField.visibility = View.VISIBLE
//            } else {
//                bind.phoneField.visibility = View.VISIBLE
//                bind.emailField.visibility = View.GONE
//            }
//            bind.btnSwitchToEmail.setInitState(it)
//            bind.btnSwitchToPhone.setInitState(!it)
//        }

        viewModel.isLoading.observe(viewLifecycleOwner) {
        }

        bind.btnSubmit.setOnClickListener {
            viewModel.isLoading.value = !(viewModel.isLoading.value)!!
        }

//        bind.btnSwitchToEmail.setOnClickListener {
//            viewModel.isEmail.value = true
//        }
//        bind.btnSwitchToPhone.setOnClickListener {
//            viewModel.isEmail.value = false
//        }

        bind.tvJumpSwitch.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_registerFragment_to_loginFragment)
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

