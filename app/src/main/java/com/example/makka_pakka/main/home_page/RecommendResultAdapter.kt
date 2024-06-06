package com.example.makka_pakka.main.home_page

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.makka_pakka.databinding.ViewItemSearchResultLiveBinding
import com.example.makka_pakka.model.LiveInfo
import com.example.makka_pakka.utils.GlideUtil

class RecommendResultAdapter(
    var data: List<LiveInfo>,
) :
    RecyclerView.Adapter<RecommendResultAdapter.ViewHolder>() {
    private lateinit var bind: ViewItemSearchResultLiveBinding
    lateinit var onItemClickListener: OnItemClickListener
    var selectedIndex = 0
    var lastInput = listOf<LiveInfo>()

    fun clearData() {
        this.data = emptyList()
        notifyDataSetChanged()
    }

    fun addData(data: List<LiveInfo>) {
        lastInput = data
        //塞到最前面 保证最新的在最前面
        this.data = data + this.data
        //不加重复的
        this.data = this.data.distinctBy { it.uid }
        //大于20，清空最早的
        if (this.data.size > 12) {
            this.data = this.data.subList(0, 12)
        }
        notifyDataSetChanged()
    }

    fun changeSelectedIndex(index: Int) {
        selectedIndex = index
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        bind = ViewItemSearchResultLiveBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(bind, onItemClickListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val live = data[position]
        holder.itemBind.tvName.text = live.name
        holder.itemBind.tvTitle.text = live.title
        GlideUtil.glideImage(live.cover_url, holder.itemBind.ivCover)
        if (selectedIndex == position) {
            holder.itemBind.selectedLayout.visibility = ViewGroup.VISIBLE
        } else {
            holder.itemBind.selectedLayout.visibility = ViewGroup.GONE
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    class ViewHolder(
        val itemBind: ViewItemSearchResultLiveBinding,
        private val onItemClickListener: OnItemClickListener
    ) :
        RecyclerView.ViewHolder(itemBind.root) {
        init {
            itemBind.root.setOnClickListener {
                onItemClickListener.onItemClick(adapterPosition)
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(pos: Int)
    }
}