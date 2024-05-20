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
    var lastInput = listOf<LiveInfo>()

    fun clearData() {
        this.data = emptyList()
        notifyDataSetChanged()
    }

    fun addData(data: List<LiveInfo>) {
        lastInput = data
        this.data += data
        //大于20，清空最早的
        if (this.data.size > 20) {
            this.data = this.data.subList(this.data.size - 20, this.data.size)
        }
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
        GlideUtil.glideImage(live.url, holder.itemBind.ivCover)
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