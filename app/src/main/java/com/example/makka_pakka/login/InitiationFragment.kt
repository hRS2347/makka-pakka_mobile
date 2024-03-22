package com.example.makka_pakka.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.makka_pakka.HOBBY_TAG_LIST
import com.example.makka_pakka.MAX_NUM_OF_SELECTED_HOBBY
import com.example.makka_pakka.databinding.FragmentInitiationBinding

class InitiationFragment : Fragment() {
    private lateinit var bind:FragmentInitiationBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        bind = FragmentInitiationBinding.inflate(layoutInflater)
        bind.container.sumTagList = HOBBY_TAG_LIST
        bind.btnRefresh.setOnClickListener {
            bind.container.refreshShow()
        }
        bind.container.funChange = {
            bind.tvSlNum.text = "$it / ${MAX_NUM_OF_SELECTED_HOBBY}"
        }
        bind.btnSubmit.setOnClickListener {
            //todo 提交
            findNavController().popBackStack()
        }

        bind.btnSkip.setOnClickListener {
            //todo 提交
            findNavController().popBackStack()
        }
        return bind.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}

