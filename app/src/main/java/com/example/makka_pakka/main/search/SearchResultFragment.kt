package com.example.makka_pakka.main.search

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.makka_pakka.databinding.FragmentSearchResultBinding
import com.example.makka_pakka.model.RoomInfo
import com.example.makka_pakka.model.UserInfo
import com.example.makka_pakka.utils.ViewUtil
import com.example.makka_pakka.utils.gson.GsonUtil

class SearchResultFragment : Fragment() {
    private lateinit var bind: FragmentSearchResultBinding
    private val viewModel: SearchResultViewModel by viewModels { SearchResultViewModel.Factory }
    private val args: SearchResultFragmentArgs by navArgs()
    private lateinit var rsLayoutManager: GridLayoutManager
    private val tabAdapter by lazy {
        SearchTabAdapter(SearchResultViewModel.tabList,
            viewModel.searchState.value!!,
            object : SearchTabAdapter.OnItemClickListener {
                override fun onItemClick(pos: Int) {
                    viewModel.searchState.value = SearchResultViewModel.tabList[pos]
                    //nestscroll 滚动到顶部
                    bind.resultRecyclerView.scrollToPosition(0)

                    bind.ivEmpty.visibility = View.INVISIBLE
                    bind.tvEmpty.visibility = View.INVISIBLE
                    bind.resultRecyclerView.visibility = View.VISIBLE



                    when (viewModel.searchState.value) {
                        SearchResultViewModel.SearchState.USER -> {
                            viewModel.search(userResultAdapter.searchPage)
                            roomResultAdapter.searchPage += 1
                        }

                        SearchResultViewModel.SearchState.ROOM -> {
                            viewModel.search(roomResultAdapter.searchPage)
                            roomResultAdapter.searchPage += 1
                        }

                        else -> {}
                    }
                    return

                }
            })
    }

    private val userResultAdapter by lazy {
        UserResultAdapter(
            emptyList()
        )
    }

    private val roomResultAdapter by lazy {
        RoomResultAdapter(
            emptyList()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        bind = FragmentSearchResultBinding.inflate(layoutInflater)
        ViewUtil.paddingByStatusBar(bind.coordinatorLayout)
        initBind()
        bindViewModel()
        userResultAdapter.onItemClickListener = object : UserResultAdapter.OnItemClickListener {
            override fun onItemClick(pos: Int) {
                // 跳转到用户详情页
                val action =
                    SearchResultFragmentDirections.actionSearchResultFragmentToUserInfoFragment(
                        GsonUtil.gson.toJson(userResultAdapter.data[pos])
                    )
                findNavController().navigate(action)
            }
        }
        roomResultAdapter.onItemClickListener = object : RoomResultAdapter.OnItemClickListener {
            override fun onItemClick(pos: Int) {
                // 跳转到房间详情页
                val action =
                    SearchResultFragmentDirections.actionSearchResultFragmentToRoomFragment(
                        roomResultAdapter.data[pos].id.toString()
                    )
                findNavController().navigate(action)
            }
        }
        viewModel.search(1)
        return bind.root
    }

    private fun bindViewModel() {
        viewModel.searchState.observe(viewLifecycleOwner) {
            tabAdapter.setData(it)
            bind.resultRecyclerView.apply {
                adapter = when (it) {
                    SearchResultViewModel.SearchState.USER -> userResultAdapter
                    SearchResultViewModel.SearchState.ROOM -> roomResultAdapter
                    else -> null
                }
                layoutManager = GridLayoutManager(
                    context, when (it) {
                        SearchResultViewModel.SearchState.USER -> 1
                        SearchResultViewModel.SearchState.ROOM -> 2
                        else -> 1
                    }
                ).let { gl ->
                    rsLayoutManager = gl
                    gl
                }
            }
        }

        viewModel.requestResult.observe(viewLifecycleOwner)
        {
            Log.d("SearchResultFragment", "requestResult")
            if (it.isEmpty()) {
                Log.d("SearchResultFragment", "requestResult is empty")
                when (viewModel.searchState.value) {
                    SearchResultViewModel.SearchState.USER -> {
                        if (userResultAdapter.data.isEmpty()) {
                            bind.ivEmpty.visibility = View.VISIBLE
                            bind.tvEmpty.visibility = View.VISIBLE
                            bind.resultRecyclerView.visibility = View.INVISIBLE
                        } else {
                            Toast.makeText(context, "没有更多数据了", Toast.LENGTH_SHORT).show()
                            userResultAdapter.isLoading = false
                        }
                    }

                    SearchResultViewModel.SearchState.ROOM -> {
                        if (roomResultAdapter.data.isEmpty()) {
                            bind.ivEmpty.visibility = View.VISIBLE
                            bind.tvEmpty.visibility = View.VISIBLE
                            bind.resultRecyclerView.visibility = View.INVISIBLE
                        } else {
                            Toast.makeText(context, "没有更多数据了", Toast.LENGTH_SHORT).show()
                            roomResultAdapter.isLoading = false
                        }
                    }

                    else -> {
                    }
                }
                return@observe
            }
            try {
                when (viewModel.searchState.value) {
                    SearchResultViewModel.SearchState.USER -> {
                        userResultAdapter.isLoading = false
                        userResultAdapter.addData(it as List<UserInfo>)
                    }
                    SearchResultViewModel.SearchState.ROOM -> {
                        roomResultAdapter.isLoading = false
                        roomResultAdapter.addData(it as List<RoomInfo>)
                    }
                    else -> {
                    }
                }
            } catch (e: Exception) {
                Log.e("SearchResultFragment", e.message.toString())
            }
        }
        viewModel.searchKey.value = args.searchKey
    }

    private fun initBind() {
        bind.editText.setText(args.searchKey)
        bind.ivBack.setOnClickListener { findNavController().popBackStack() }
        //点击搜索框，直接返回
        bind.editText.setOnClickListener { findNavController().popBackStack() }
        bind.editText.isFocusable = false

        bind.tabRecyclerView.adapter = tabAdapter

        bind.resultRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                //上拉不触发
                if (dy <= 0) {
                    return
                }
                val totalItemCount = rsLayoutManager.itemCount
                // 如果拉到最下面
                //刷新数据
                if (rsLayoutManager.findLastVisibleItemPosition() == totalItemCount - 1) {
                    when (viewModel.searchState.value) {
                        SearchResultViewModel.SearchState.USER -> {
                            if (userResultAdapter.isLoading) {
                                return
                            }
                            userResultAdapter.isLoading = true
                            viewModel.search(userResultAdapter.searchPage)
                            userResultAdapter.searchPage += 1
                        }

                        SearchResultViewModel.SearchState.ROOM -> {
                            if (roomResultAdapter.isLoading) {
                                return
                            }
                            roomResultAdapter.isLoading = true
                            viewModel.search(roomResultAdapter.searchPage)
                            roomResultAdapter.searchPage += 1
                        }

                        else -> {}
                    }
                }
            }
        })
    }
}