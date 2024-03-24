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
import com.example.makka_pakka.databinding.FragmentResetBinding
import com.example.makka_pakka.utils.HttpUtil
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException

class ResetFragment : Fragment() {
    private lateinit var bind: FragmentResetBinding
    private lateinit var handler: Handler

    private enum class EVENTS {
        COUNTING_TIME, FAILURE, SUCCESS
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        bind = FragmentResetBinding.inflate(layoutInflater)
        handler = Handler(
            Handler.Callback {
                when (it.what) {
                    EVENTS.COUNTING_TIME.ordinal -> {
                        val counter = it.obj as Int
                        if (counter == 0) {
                            bind.btnIndCode.text = "获取验证码"
                            bind.btnIndCode.isEnabled = true
                            bind.btnIndCode.setBackgroundColor(
                                ResourcesCompat.getColor(
                                    resources,
                                    R.color.primary_color,
                                    null
                                )
                            )
                        } else {
                            bind.btnIndCode.text = "重新获取($counter)"
                            bind.btnIndCode.isEnabled = false
                            bind.btnIndCode.setBackgroundColor(
                                ResourcesCompat.getColor(
                                    resources,
                                    R.color.stroke_grey,
                                    null
                                )
                            )
                            handler.sendMessageDelayed(
                                handler.obtainMessage(
                                    EVENTS.COUNTING_TIME.ordinal,
                                    counter - 1
                                ), 1000
                            )
                        }
                    }

                    EVENTS.FAILURE.ordinal -> {
                        bind.emailField.error = it.obj as String
                        bind.progressBar.visibility = View.INVISIBLE
                    }

                    EVENTS.SUCCESS.ordinal -> {
                        bind.progressBar.visibility = View.INVISIBLE
                        Navigation.findNavController(bind.btnSubmit)
                            .popBackStack()
                    }
                }

                true
            })
//        bind.btnSubmit.setOnClickListener {
//            Navigation.findNavController(it).navigate(R.id.action_loginFragment_to_mainFragment)
//        }

        bind.btnIndCode.setOnClickListener {
            handler.sendMessage(handler.obtainMessage(EVENTS.COUNTING_TIME.ordinal, 60))
            HttpUtil.requestIndCode(bind.etEmail.text.toString(), object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    handler.removeMessages(EVENTS.COUNTING_TIME.ordinal)
                    handler.sendMessage(handler.obtainMessage(EVENTS.COUNTING_TIME.ordinal, 0))
                    handler.sendMessage(handler.obtainMessage(EVENTS.FAILURE.ordinal, e.message))
                    Log.e("RegisterFragment", "onFailure: ${e.message}")
                }

                override fun onResponse(call: Call, response: Response) {
                    Log.d("RegisterFragment", "onResponse: ${response.body?.string()}")
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
            })
        }

        bind.btnSubmit.setOnClickListener {
            HttpUtil.reset(
                bind.etEmail.text.toString(),
                bind.etPassword.text.toString(),
                bind.etCode.text.toString(),
                object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        handler.sendMessage(
                            handler.obtainMessage(
                                EVENTS.FAILURE.ordinal,
                                e.message
                            )
                        )
                        Log.e("RegisterFragment", "onFailure: ${e.message}")
                    }

                    override fun onResponse(call: Call, response: Response) {
                        val body = response.body?.string()
                        Log.d("RegisterFragment", "onResponse: ${response.body?.string()}")
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
                })
            bind.progressBar.visibility = View.VISIBLE
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

