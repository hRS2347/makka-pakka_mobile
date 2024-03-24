package com.example.makka_pakka.login

import android.os.Bundle
import android.os.Handler
import android.util.Log
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
import com.example.makka_pakka.utils.HttpUtil
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException

class LoginFragment : Fragment() {
    private lateinit var bind: FragmentLoginBinding
    private lateinit var handler: Handler

    private enum class EVENTS {
        SUCCESS, FAILURE

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        bind = FragmentLoginBinding.inflate(layoutInflater)
        handler = Handler(
            Handler.Callback {
                when (it.what) {
                    EVENTS.SUCCESS.ordinal -> {
                        Navigation.findNavController(bind.btnSubmit)
                            .navigate(R.id.action_loginFragment_to_mainFragment)
                    }

                    EVENTS.FAILURE.ordinal -> {
                        bind.emailField.error = it.obj as String
                        bind.progressBar.visibility = View.INVISIBLE
                    }
                }
                true
            })

        bind.btnSubmit.setOnClickListener {
            val email = bind.etEmail.text.toString()
            val password = bind.etPassword.text.toString()

            val call = object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    Log.d("LoginFragment", "onFailure: ${e.message}")
                    handler.sendMessage(handler.obtainMessage(EVENTS.FAILURE.ordinal, e.message))
                }

                override fun onResponse(call: Call, response: Response) {
                    if (response.code == 200)
                        handler.sendMessage(handler.obtainMessage(EVENTS.SUCCESS.ordinal))
                    else
                        handler.sendMessage(
                            handler.obtainMessage(
                                EVENTS.FAILURE.ordinal,
                                response.body?.string()
                            )
                        )
                }
            }

            bind.progressBar.visibility = View.VISIBLE
            HttpUtil.login(email, password, call)
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

