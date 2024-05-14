package com.example.makka_pakka.main.search

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.makka_pakka.R
import com.example.makka_pakka.databinding.ViewItemSearchMatchBinding
import com.example.makka_pakka.databinding.ViewItemSearchResultUserBinding
import com.example.makka_pakka.databinding.ViewItemTabBinding
import com.example.makka_pakka.model.UserInfo
import com.example.makka_pakka.utils.GlideUtil

class UserResultAdapter(
     var data: List<UserInfo>,
    ) :
    RecyclerView.Adapter<UserResultAdapter.ViewHolder>() {
    private lateinit var bind: ViewItemSearchResultUserBinding
    lateinit var onItemClickListener: OnItemClickListener
    var searchPage = 1
    var isLoading = false
    var lastInput= listOf<UserInfo>()

    fun clearData() {
        this.data = emptyList()
        notifyDataSetChanged()
    }

    fun addData(data: List<UserInfo>) {
        if (lastInput.isNotEmpty() &&data[0].id == lastInput[0].id) {//重复搜索
            return
        }
        lastInput = data
        this.data += data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        bind = ViewItemSearchResultUserBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(bind, onItemClickListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = data[position]
        holder.itemBind.name.text = user.name
        GlideUtil.loadAvatar(holder.itemBind.avatar, user.avatarUrl ?: "")

    }

    override fun getItemCount(): Int {
        return data.size
    }

    class ViewHolder(
        val itemBind: ViewItemSearchResultUserBinding,
        private val onItemClickListener: OnItemClickListener
    ) :
        RecyclerView.ViewHolder(itemBind.root) {
        init {
            itemBind.btnWatch.setOnClickListener {
                onItemClickListener.onItemClick(adapterPosition)
                Log.d("SearchTabAdapter", "onItemClick: $adapterPosition")
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(pos: Int)
    }
}