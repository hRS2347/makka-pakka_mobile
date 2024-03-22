package com.example.makka_pakka.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.example.makka_pakka.R
import com.example.makka_pakka.databinding.FragmentResetBinding

class ResetFragment : Fragment() {
    private lateinit var bind: FragmentResetBinding
    private val viewModel: LoginViewModel by viewModels { LoginViewModel.Factory }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        bind = FragmentResetBinding.inflate(layoutInflater)

        viewModel.isLoading.observe(viewLifecycleOwner) {

        }

        bind.btnSubmit.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_loginFragment_to_mainFragment)
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

