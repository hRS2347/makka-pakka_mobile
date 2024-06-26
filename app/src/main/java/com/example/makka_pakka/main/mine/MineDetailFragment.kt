package com.example.makka_pakka.main.mine

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.makka_pakka.MainActivity
import com.example.makka_pakka.MyApplication
import com.example.makka_pakka.R
import com.example.makka_pakka.databinding.FragmentMineDetailBinding
import com.example.makka_pakka.utils.GlideUtil
import com.example.makka_pakka.utils.ViewUtil
import com.google.android.material.datepicker.MaterialDatePicker
import com.lljjcoder.Interface.OnCityItemClickListener
import com.lljjcoder.bean.DistrictBean
import com.lljjcoder.bean.ProvinceBean
import com.lljjcoder.style.cityjd.JDCityConfig
import com.lljjcoder.style.cityjd.JDCityPicker
import com.lljjcoder.style.citythreelist.CityBean
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MineDetailFragment : Fragment() {
    private lateinit var bind: FragmentMineDetailBinding
    private lateinit var pickMultipleMedia: ActivityResultLauncher<PickVisualMediaRequest>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        bind = FragmentMineDetailBinding.inflate(layoutInflater)
        MyApplication.instance.getUserInfo()
        ViewUtil.paddingByStatusBar(bind.coordinatorLayout)
        pickMultipleMedia =
            registerForActivityResult(ActivityResultContracts.PickMultipleVisualMedia()) {
                if (it.size > 1) {
                    Toast.makeText(requireContext(), "只能选择一张图片", Toast.LENGTH_SHORT).show()
                }
                //申请永久访问权限
                requireActivity().contentResolver.takePersistableUriPermission(
                    it[0], Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
                MyApplication.instance.avatarChange(it[0])
                Toast.makeText(requireContext(), "正在上传", Toast.LENGTH_SHORT).show()
            }

        MyApplication.instance.currentUser.observe(viewLifecycleOwner) {
            if (it == null) {//不可能为空
                return@observe
            }
            bind.tvName.text = it.name
            bind.tvSex.text = if (it.sex == 0) "男" else "女"
            bind.tvRegion.text = it.region
            bind.tvBirth.text = it.birthday.toString()
            bind.tvCreateTime.text = it.createTime.toString()
            bind.tvDescription.text = it.description

            if (!it.avatarUrl.isNullOrEmpty()) {
                GlideUtil.loadAvatar(bind.ivAvatar, it.avatarUrl!!)
            }
        }

        bind.ivBack.setOnClickListener {
            Navigation.findNavController(it).navigateUp()
        }

        bind.ivName.setOnClickListener {
            InfoEditDialog(requireActivity() , InfoEditDialog.EditType.NAME).show()
        }

        bind.ivDescription.setOnClickListener {
            InfoEditDialog(requireActivity(), InfoEditDialog.EditType.DESCRIPTION).show()
        }

        bind.ivRegion.setOnClickListener {
//            InfoEditDialog(requireActivity(), InfoEditDialog.EditType.REGION).show()
            val cityPicker = JDCityPicker()
            val jdCityConfig = JDCityConfig.Builder().build()

            jdCityConfig.showType = JDCityConfig.ShowType.PRO_CITY_DIS
            cityPicker.init(requireActivity())
            cityPicker.setConfig(jdCityConfig)
            cityPicker.setOnCityItemClickListener(object : OnCityItemClickListener() {
                override fun onSelected(
                    province: ProvinceBean?,
                    city: com.lljjcoder.bean.CityBean?,
                    district: DistrictBean?
                ) {
                    bind.tvRegion.setText(
                        "${province!!.name} ${city!!.name} ${district!!.name}"
                .trimIndent()
                    )
                }

                override fun onCancel() {}
            })
            cityPicker.showCityPicker()
        }

        bind.ivSex.setOnClickListener {
            InfoEditDialog(requireActivity(), InfoEditDialog.EditType.SEX).show()
        }

        bind.ivAvatar.setOnClickListener {
            pickMultipleMedia.launch(PickVisualMediaRequest())
        }
        bind.textView6.setOnClickListener {
            pickMultipleMedia.launch(PickVisualMediaRequest())
        }
        bind.ivBirth.setOnClickListener {
            val datePicker =
                MaterialDatePicker.Builder.datePicker()
                    .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                    .setTitleText("修改生日")
                    .build()
            datePicker.show(childFragmentManager, "datePicker")
            datePicker.addOnPositiveButtonClickListener {
                //convert to sql date
                val date = Date(it)
                val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val format = sdf.format(date)

                MyApplication.instance.currentUser.value?.copy(
                    birthday = format
                ).let { info ->
                    MyApplication.instance.userInfoChange(info)
                }
                Toast.makeText(requireContext(), "正在上传", Toast.LENGTH_SHORT).show()
            }

        }
        bind.ivSelfSetting.setOnClickListener(View.OnClickListener {
            Navigation.findNavController(it)
                .navigate(R.id.action_mineDetailFragment_to_resetFragment)
        }
        )
        return bind.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}

