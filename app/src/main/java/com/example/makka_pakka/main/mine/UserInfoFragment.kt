package com.example.makka_pakka.main.mine

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.example.makka_pakka.R
import com.example.makka_pakka.databinding.FragmentMineBinding
import com.example.makka_pakka.model.UserInfo
import com.example.makka_pakka.utils.GlideUtil
import com.example.makka_pakka.utils.ViewUtil
import com.example.makka_pakka.utils.gson.GsonUtil
import java.util.Calendar

class UserInfoFragment : Fragment() {
    private lateinit var bind: FragmentMineBinding
    private val args: UserInfoFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
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


        val user = GsonUtil.gson.fromJson(args.json, UserInfo::class.java)
        user.let {
            bind.tvName.text = it.name
            bind.tvId.text = "MKID：${it.id.toString()}"
            bind.tvRegion.text = it.region.let { s ->
                if (s.isNullOrEmpty()) {
                    "未知"
                } else {
                    s
                }
            }
            bind.tvDesc.text = it.description.let { s ->
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

