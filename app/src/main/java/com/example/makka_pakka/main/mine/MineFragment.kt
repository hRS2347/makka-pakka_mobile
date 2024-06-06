package com.example.makka_pakka.main.mine

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.example.makka_pakka.MainActivity
import com.example.makka_pakka.MyApplication
import com.example.makka_pakka.R
import com.example.makka_pakka.databinding.FragmentMineBinding
import com.example.makka_pakka.sound_flex.GestureControlListener
import com.example.makka_pakka.utils.GlideUtil
import com.example.makka_pakka.utils.ViewUtil
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.util.Calendar

class MineFragment : Fragment() {
    private lateinit var bind: FragmentMineBinding
    private lateinit var handler: Handler
    private val viewModel: MineViewModel by viewModels {
        MineViewModel.Factory
    }

    private lateinit var gestureControlListener: GestureControlListener

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        bind = FragmentMineBinding.inflate(layoutInflater)
        handler = Handler(
            Handler.Callback {
                when (it.what) {
                    MineViewModel.MSG_INDEX_GESTURE_GIF_CHANGE -> {
                        viewModel.changeGestureGif()
                        handler.sendEmptyMessageDelayed(
                            MineViewModel.MSG_INDEX_GESTURE_GIF_CHANGE,
                            MineViewModel.GESTURE_CHANGE_COUNT_IN_SECOND * 1000L
                        )
                    }
                }
                return@Callback true
            })


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

        bind.btnRoom.setOnClickListener {
            Navigation.findNavController(it)
                .navigate(R.id.action_mineFragment_to_roomFragment)
        }

        bind.ivLogout.setOnClickListener {
            //clear token
            MyApplication.instance.logout()

        }
        MyApplication.instance.currentUser.observe(viewLifecycleOwner) { i ->
            if (i == null) {
                Navigation.findNavController(bind.root)
                    .navigate(R.id.action_mineFragment_to_coverFragment)
            }
        }
        viewModel.currentGestureGifIndex.observe(viewLifecycleOwner) { index ->
            //淡入
            bind.ivGesture.alpha = 0f
            bind.ivGesture.animate().alpha(1f).duration = 500
            context?.let {
                GlideUtil.loadGif(
                    it,
                    MineViewModel.gestureGifList[index],
                    bind.ivGesture
                )
            }
        }
        handler.sendEmptyMessage(MineViewModel.MSG_INDEX_GESTURE_GIF_CHANGE)
        viewModel.recordingTime.observe(viewLifecycleOwner) {
            if (it == -1){
                bind.tvCountingTime.text = "准备"
                return@observe
            }
            val time = it / 10F
            bind.tvCountingTime.text = (time).toString()
            if (time > 1.2F) {
                bind.tvCountingTime.setTextColor(
                    ActivityCompat.getColor(
                        requireContext(),
                        R.color.secondary_color
                    )
                )
            } else if (time > 0.6F) {
                bind.tvCountingTime.setTextColor(
                    ActivityCompat.getColor(
                        requireContext(),
                        R.color.primary_color
                    )
                )
            } else {
                bind.tvCountingTime.setTextColor(
                    ActivityCompat.getColor(
                        requireContext(),
                        R.color.warning_red
                    )
                )
            }
        }

        viewModel.guidanceIndex.observe(viewLifecycleOwner) {
            val loadingLayoutVisible = if (it == -1) View.VISIBLE else View.INVISIBLE
            val settingLayoutVisible = if (it in 0..5) View.VISIBLE else View.INVISIBLE
            val finishedLayoutVisible = if (it == 6) View.VISIBLE else View.INVISIBLE

            bind.btnReload.visibility = loadingLayoutVisible

            bind.tvCountingTime.visibility = settingLayoutVisible
            bind.tvCountingTimeSide.visibility = settingLayoutVisible
            bind.btnStart.visibility = settingLayoutVisible
            bind.btnUpload.visibility = settingLayoutVisible
            bind.btnReset.visibility = if (it in 0..6) View.VISIBLE else View.INVISIBLE

            bind.btnOn.visibility = finishedLayoutVisible

            bind.tvFinishedHint.visibility = finishedLayoutVisible


            if (it == -1) {
                //加载
                viewModel.loadConfig()
                bind.tvGuidance.text = "正在加载配置"
                return@observe
            }

            if (it < MineViewModel.guidanceList.size) {
                bind.tvGuidance.text = MineViewModel.guidanceList[it]
            }
            viewModel.recordingTime.value = 20
            viewModel.isRecordDone.value = false
            viewModel.isRecordingOrUploading.value = false
            bind.tvProcessing1.text = "${it + 1}/7"
            bind.tvProcessing2.text = "${it + 1}/7"
        }

        viewModel.isRecordingOrUploading.observe(viewLifecycleOwner) {
            if (it) {
                bind.btnStart.isEnabled = false
                bind.btnUpload.isEnabled = false
                bind.btnStart.backgroundTintList =
                    ActivityCompat.getColorStateList(requireContext(), R.color.light_grey)
                bind.btnReload.isEnabled = false
                bind.btnReload.backgroundTintList =
                    ActivityCompat.getColorStateList(requireContext(), R.color.light_grey)
            } else {
                bind.btnStart.isEnabled = true
                bind.btnUpload.isEnabled = viewModel.isRecordDone.value!!
                bind.btnStart.backgroundTintList =
                    ActivityCompat.getColorStateList(requireContext(), R.color.secondary_color)
                bind.btnReload.isEnabled = true
                bind.btnReload.backgroundTintList =
                    ActivityCompat.getColorStateList(requireContext(), R.color.secondary_color)
            }
            bind.btnUpload.backgroundTintList = if (viewModel.isRecordDone.value!! && !it) {
                ActivityCompat.getColorStateList(requireContext(), R.color.primary_color)
            } else {
                ActivityCompat.getColorStateList(requireContext(), R.color.light_grey)
            }
        }

        bind.btnStart.setOnClickListener {
            viewModel.startRecording()
        }

        bind.btnUpload.setOnClickListener {
            viewModel.uploadGesture()
        }

        viewModel.errorMsg.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
        }

        bind.btnReset.setOnClickListener {
            //如果识别开启，先关掉
            if ((requireActivity() as MainActivity).viewModel.isPredictTabOn.value!!) {
                (requireActivity() as MainActivity).viewModel.switchPredictTabState()
            }
            // 弹出确认框
            MaterialAlertDialogBuilder(requireContext())
                .setTitle("重置手势")
                .setMessage("确定要重置手势吗？")
                .setPositiveButton("确定") { _, _ ->
                    viewModel.resetGesture()
                }
                .setNegativeButton("取消") { _, _ -> }
                .show()
        }

        (requireActivity() as MainActivity).viewModel.isPredictTabOn.observe(viewLifecycleOwner) {
            bind.btnOn.backgroundTintList = if (it) {
                ActivityCompat.getColorStateList(requireContext(), R.color.tertiary_color)
            } else {
                ActivityCompat.getColorStateList(requireContext(), R.color.primary_color)
            }
            bind.btnOn.text = if (it) "正在识别" else "开始识别"
        }

        bind.btnOn.setOnClickListener {
            (requireActivity() as MainActivity).viewModel.switchPredictTabState()
        }

        bind.btnReload.setOnClickListener {
            viewModel.loadConfig()
        }

        viewModel.waveImgUrl.observe(viewLifecycleOwner) {
            Log.d("MineFragment", "onCreateView: $it")
            //强制刷新
            GlideUtil.glideImage(it, bind.ivWave, true)
        }

        gestureControlListener = object : GestureControlListener {
            override fun onGestureControl(gesture: Int) {
                //如果不是在首页，不响应手势
                if (findNavController().currentDestination?.id != R.id.mineFragment) {
                    return
                }
                if (gesture == 5){
                    Log.d("MineFragment", "onGestureControl: gesture 5")
                    //切换页面
                    (activity as MainActivity).switchNav()
                }
            }
        }
        (activity as MainActivity).gestureController = gestureControlListener

        return bind.root
    }

}

