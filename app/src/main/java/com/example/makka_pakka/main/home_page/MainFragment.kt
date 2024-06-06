package com.example.makka_pakka.main.home_page

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.makka_pakka.MainActivity
import com.example.makka_pakka.MyApplication
import com.example.makka_pakka.R
import com.example.makka_pakka.databinding.FragmentMainBinding
import com.example.makka_pakka.sound_flex.GestureControlListener
import com.example.makka_pakka.utils.GlideUtil
import com.example.makka_pakka.utils.ViewUtil


class MainFragment : Fragment() {
    private lateinit var bind: FragmentMainBinding
    private val viewModel: MainViewModel by viewModels { MainViewModel.Factory }
    private val recommendResultAdapter by lazy {
        RecommendResultAdapter(
            emptyList(),
            (activity as MainActivity).viewModel.selectedIndex
        )
    }
    private lateinit var gestureControlListener: GestureControlListener

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        bind = FragmentMainBinding.inflate(layoutInflater)

        ViewUtil.paddingByStatusBar(bind.coordinatorLayout)

        bind.searchBar.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_searchFragment)
        }

        bind.tvToAudi.setOnClickListener {
            findNavController().navigate(R.id.action_global_audienceFragment)
        }

        bind.tvToRoom.setOnClickListener {
            val action =
                MainFragmentDirections.actionMainFragmentToRoomFragment(
                    MyApplication.instance.testRoom.id.toString()
                )
            findNavController().navigate(action)
        }

        bind.tvToUserInfo.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_userInfoFragment)
        }

        MyApplication.instance.currentUser.observe(viewLifecycleOwner) {
            if (it != null) {
                GlideUtil.loadAvatar(bind.ivAvatar, it.avatarUrl ?: "")
            }
        }

        bind.ivAvatar.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_mineFragment)
        }

        bind.swipeRefreshLayout.setColorSchemeColors(
            ResourcesCompat.getColor(
                resources,
                R.color.primary_color,
                null
            )
        )

        bind.swipeRefreshLayout.setOnRefreshListener {
            viewModel.refresh()
        }

        recommendResultAdapter.onItemClickListener =
            object : RecommendResultAdapter.OnItemClickListener {
                override fun onItemClick(pos: Int) {
                    //get lid to the audienceFragment
                    val action =
                        MainFragmentDirections.actionGlobalAudienceFragment(
                            upid = recommendResultAdapter.data[pos].uid,
                            liveUrl = recommendResultAdapter.data[pos].live_url
                        )
                    findNavController().navigate(action)
                }
            }

        bind.rv.adapter = recommendResultAdapter
        bind.rv.layoutManager = GridLayoutManager(context, 2)
        if (!(activity as MainActivity).isHobbySelectedAsk &&
            MyApplication.instance.currentUser.value != null &&
            MyApplication.instance.currentUser.value!!.isHobbySelected != 1
        ) {//未选择兴趣
            findNavController().navigate(R.id.action_mainFragment_to_initiationFragment)
        }
        viewModel.recommendListLoading.observe(viewLifecycleOwner) {
            if (!it) {
                bind.swipeRefreshLayout.isRefreshing = false
            }
        }
        viewModel.recommendList.observe(viewLifecycleOwner) {
            recommendResultAdapter.changeSelectedIndex(
                (activity as MainActivity).viewModel.isPredictTabOn.value.let { isPredictTabOn ->
                    if (isPredictTabOn == true) 0 else -1
                })
            recommendResultAdapter.addData(it)
        }
        if (recommendResultAdapter.data.isEmpty()) {
            viewModel.refresh()
        }
        (activity as MainActivity).viewModel.isPredictTabOn.observe(viewLifecycleOwner) {
            if (it) {
                recommendResultAdapter.changeSelectedIndex(0)
            } else {
                recommendResultAdapter.changeSelectedIndex(-1)
            }
        }
        gestureControlListener = object : GestureControlListener {
            override fun onGestureControl(gesture: Int) {
                //如果不是在首页，不响应手势
                if (findNavController().currentDestination?.id != R.id.mainFragment) {
                    return
                }
                when (gesture) {
                    1 -> {//选择下一个
                        if (recommendResultAdapter.selectedIndex < recommendResultAdapter.data.size - 1) {
                            recommendResultAdapter.changeSelectedIndex(recommendResultAdapter.selectedIndex + 1)
                        }
                    }

                    2 -> {
                        if (recommendResultAdapter.selectedIndex > 0) {
                            recommendResultAdapter.changeSelectedIndex(recommendResultAdapter.selectedIndex - 1)
                        }
                    }

                    3 -> {//刷新
                        viewModel.refresh()
                    }

                    4 -> {
                        try {
                            (activity as MainActivity).viewModel.selectedIndex = recommendResultAdapter.selectedIndex
                            //进入直播间
                            val action =
                                MainFragmentDirections.actionGlobalAudienceFragment(
                                    upid = recommendResultAdapter.data[recommendResultAdapter.selectedIndex].uid,
                                    liveUrl = recommendResultAdapter.data[recommendResultAdapter.selectedIndex].live_url
                                )
                            findNavController().navigate(action)
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }

                    5 -> {
                        //切换页面
                        (activity as MainActivity).viewModel.selectedIndex = recommendResultAdapter.selectedIndex
                        (activity as MainActivity).switchNav()
                    }
                }
            }
        }
        (activity as MainActivity).gestureController = gestureControlListener



        return bind.root
    }

    override fun onResume() {
        super.onResume()
//        viewModel.refresh()
    }
}

