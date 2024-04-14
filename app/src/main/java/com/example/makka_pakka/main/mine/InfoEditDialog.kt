package com.example.makka_pakka.main.mine

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import com.example.makka_pakka.MainActivity
import com.example.makka_pakka.MyApplication
import com.example.makka_pakka.R
import com.example.makka_pakka.databinding.DialogInfoEditBinding
import com.example.makka_pakka.utils.ViewUtil
import com.lljjcoder.Interface.OnCityItemClickListener
import com.lljjcoder.bean.DistrictBean
import com.lljjcoder.bean.ProvinceBean
import com.lljjcoder.style.cityjd.JDCityConfig
import com.lljjcoder.style.cityjd.JDCityPicker
import com.lljjcoder.style.citythreelist.CityBean

class InfoEditDialog(val preContext: Context, private val mode: EditType) :
    Dialog(preContext, R.style.myDialog) {
    private lateinit var bind: DialogInfoEditBinding
    private var user = MyApplication.instance.currentUser.value?.copy()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = DialogInfoEditBinding.inflate(layoutInflater)
        setContentView(bind.root)
        initView()
        bind.etName.setText(user?.name)
        bind.etDescription.setText(user?.description)
        bind.etRegion.setText(user?.region)
        if (user?.sex == 0) {
            bind.ivMan.visibility = View.VISIBLE
            bind.ivWoman.visibility = View.GONE
        } else {
            bind.ivMan.visibility = View.GONE
            bind.ivWoman.visibility = View.VISIBLE
        }
        bind.tvCancel.setOnClickListener {
            dismiss()
        }

        when (mode) {
            EditType.NAME -> {
                bind.tvTitle.text = "修改昵称"
                bind.clDescription.visibility = View.GONE
                bind.clRegion.visibility = View.GONE
                bind.clSex.visibility = View.GONE
                bind.clName.visibility = View.VISIBLE
            }

            EditType.DESCRIPTION -> {
                bind.tvTitle.text = "修改简介"
                bind.clName.visibility = View.GONE
                bind.clRegion.visibility = View.GONE
                bind.clSex.visibility = View.GONE
                bind.clDescription.visibility = View.VISIBLE
            }

            EditType.REGION -> {
                bind.tvTitle.text = "修改地区"
                bind.clName.visibility = View.GONE
                bind.clDescription.visibility = View.GONE
                bind.clSex.visibility = View.GONE
                bind.clRegion.visibility = View.VISIBLE

            }

            EditType.SEX -> {
                bind.tvTitle.text = "修改性别"
                bind.clName.visibility = View.GONE
                bind.clDescription.visibility = View.GONE
                bind.clRegion.visibility = View.GONE
                bind.clSex.visibility = View.VISIBLE
            }
        }
        bind.clMan.setOnClickListener {
            user?.sex = 0
            bind.ivMan.visibility = View.VISIBLE
            bind.ivWoman.visibility = View.GONE
        }

        bind.clWoman.setOnClickListener {
            user?.sex = 1
            bind.ivMan.visibility = View.GONE
            bind.ivWoman.visibility = View.VISIBLE
        }

        bind.tvSubmit.setOnClickListener {
            user?.name = bind.etName.text.toString()
            user?.region = bind.etRegion.text.toString()
            user?.description = bind.etDescription.text.toString()
            user.let { info -> MyApplication.instance.userInfoChange(info) }
            Toast.makeText(context, "修改成功,已上传服务器", Toast.LENGTH_SHORT).show()
            dismiss()
        }
    }

    private fun initView() {
        window?.attributes = window?.attributes?.apply {
            height = WindowManager.LayoutParams.WRAP_CONTENT
            width = ViewUtil.getScreenWidth(context) - ViewUtil.dpToPx(context, 40f)
        }

    }

    enum class EditType {
        NAME, DESCRIPTION,
        REGION, SEX
    }
}