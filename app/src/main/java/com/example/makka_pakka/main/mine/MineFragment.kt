package com.example.makka_pakka.main.mine

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.makka_pakka.MyApplication
import com.example.makka_pakka.R
import com.example.makka_pakka.databinding.FragmentMineBinding
import com.example.makka_pakka.utils.GlideUtil
import com.example.makka_pakka.utils.ViewUtil
import com.example.makka_pakka.view.X5WebView

class MineFragment : Fragment() {
    private lateinit var bind: FragmentMineBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        bind = FragmentMineBinding.inflate(layoutInflater)
        ViewUtil.paddingByStatusBar(bind.coordinatorLayout)
        bind.btnEdit.setOnClickListener {
            Navigation.findNavController(it)
                .navigate(R.id.action_mineFragment_to_mineDetailFragment)
        }

        MyApplication.instance.currentUser.observe(viewLifecycleOwner) {
            if (it == null) {//不可能为空
                return@observe
            }
            bind.tvName.text = it.name
            bind.tvId.text = it.id.toString()
            bind.tvRegion.text = it.region
            bind.tvDesc.text = it.description

            if (!it.avatarUrl.isNullOrEmpty()) {
                GlideUtil.loadAvatar(bind.ivAvatar, it.avatarUrl!!)
            }
        }
        return bind.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

}

