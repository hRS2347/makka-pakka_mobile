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
import java.util.Calendar

class UserInfoFragment : Fragment() {
    private lateinit var bind: FragmentMineBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        bind = FragmentMineBinding.inflate(layoutInflater)
        ViewUtil.paddingByStatusBar(bind.coordinatorLayout)
        bind.btnRoom.visibility = View.INVISIBLE
        bind.btnEdit.visibility = View.INVISIBLE
        bind.ivShare.visibility = View.INVISIBLE
        bind.ivMore.visibility = View.INVISIBLE
        bind.ivBack.visibility = View.VISIBLE
        bind.ivBack.setOnClickListener {
            Navigation.findNavController(it).navigateUp()
        }

        return bind.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }


    override fun onDestroy() {
        super.onDestroy()
        bind.ivBack.visibility = View.INVISIBLE
    }
}

