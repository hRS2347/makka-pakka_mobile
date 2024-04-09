package com.example.makka_pakka.login

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.makka_pakka.HOBBY_TAG_LIST
import com.example.makka_pakka.MAX_NUM_OF_SELECTED_HOBBY
import com.example.makka_pakka.MainActivity
import com.example.makka_pakka.MyApplication
import com.example.makka_pakka.databinding.FragmentInitiationBinding
import com.example.makka_pakka.utils.HttpUtil
import com.example.makka_pakka.utils.ViewUtil
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response

class InitiationFragment : Fragment() {
    private lateinit var bind: FragmentInitiationBinding
    private lateinit var handler: Handler

    private enum class EVENTS {
        SUCCESS, FAILURE
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        bind = FragmentInitiationBinding.inflate(layoutInflater)
        ViewUtil.paddingByStatusBar(bind.root)
        (activity as MainActivity).isHobbySelectedAsk = true
        handler = Handler(
            Handler.Callback {
                when (it.what) {
                    EVENTS.SUCCESS.ordinal -> {
                        Toast.makeText(context, "提交成功", Toast.LENGTH_SHORT).show()
                        MyApplication.instance.getUserInfo()
                        findNavController().popBackStack()
                    }

                    EVENTS.FAILURE.ordinal -> {
                        Toast.makeText(context, "网络错误", Toast.LENGTH_SHORT).show()
                    }
                }
                true
            })

        bind.container.sumTagList = HOBBY_TAG_LIST
        bind.btnRefresh.setOnClickListener {
            bind.container.refreshShow()
        }
        bind.container.funChange = {
            bind.tvSlNum.text = "$it / $MAX_NUM_OF_SELECTED_HOBBY"
        }
        bind.btnSubmit.setOnClickListener {
            HttpUtil.sendHabits(bind.container.getList(), object : Callback {
                override fun onFailure(call: Call, e: java.io.IOException) {
                    handler.obtainMessage(EVENTS.FAILURE.ordinal).sendToTarget()
                }

                override fun onResponse(call: Call, response: Response) {
                    if (response.code == 200) {
                        handler.obtainMessage(EVENTS.SUCCESS.ordinal).sendToTarget()
                    } else {
                        handler.obtainMessage(EVENTS.FAILURE.ordinal, response.message)
                            .sendToTarget()
                    }
                }
            })
        }

        bind.btnSkip.setOnClickListener {
            findNavController().popBackStack()
        }
        return bind.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}

