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
            bind.tvId.text = "MKID：${it.id.toString()}"
            bind.tvRegion.text = it.region.let {s->
                if (s.isNullOrEmpty()) {
                    "未知"
                } else {
                    s
                }
            }
            bind.tvDesc.text = it.description.let { s->
                if (s.isNullOrEmpty()) {
                    "这个人很懒，什么都没留下"
                } else {
                    s
                }
            }

            if (it.sex == 0) {
                bind.ivSex.setImageResource(R.drawable.male)
            } else {
                bind.ivSex.setImageResource(R.drawable.female)
            }

            bind.tvAge.text = "".let { s ->
                // use current time - birthday
                if (it.birthday.isNullOrEmpty()) {
                    ""
                } else {
                    val birth = it.birthday!!.split("-")
                    val year = birth[0].toInt()
                    val month = birth[1].toInt()
                    val day = birth[2].toInt()
                    val calendar = Calendar.getInstance()
                    val currentYear = calendar.get(Calendar.YEAR)
                    val currentMonth = calendar.get(Calendar.MONTH) + 1
                    val currentDay = calendar.get(Calendar.DAY_OF_MONTH)
                    var age = currentYear - year
                    if (currentMonth < month || (currentMonth == month && currentDay < day)) {
                        age--
                    }
                    "$age 岁"
                }

            }.toString()

            if (!it.avatarUrl.isNullOrEmpty()) {
                GlideUtil.loadAvatar(bind.ivAvatar, it.avatarUrl!!)
            }
        }

        bind.btnRoom.setOnClickListener{
            Navigation.findNavController(it)
                .navigate(R.id.action_mineFragment_to_roomFragment)
        }

        bind.ivLogout.setOnClickListener {
           //clear token
            MyApplication.instance.logout()

        }
        MyApplication.instance.currentUser.observe(viewLifecycleOwner) {i->
            if (i == null) {
                Navigation.findNavController(bind.root)
                    .navigate(R.id.action_mineFragment_to_coverFragment)
            }
        }
        return bind.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

}

