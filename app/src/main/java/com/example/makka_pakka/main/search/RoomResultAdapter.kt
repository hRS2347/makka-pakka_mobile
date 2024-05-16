package com.example.makka_pakka.main.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.makka_pakka.databinding.ViewItemSearchResultRoomBinding
import com.example.makka_pakka.model.RoomInfo
import com.example.makka_pakka.utils.GlideUtil

class RoomResultAdapter(
    var data: List<RoomInfo>
) :
    RecyclerView.Adapter<RoomResultAdapter.ViewHolder>() {
    private lateinit var bind: ViewItemSearchResultRoomBinding
    lateinit var onItemClickListener: OnItemClickListener
    var searchPage = 1
    var isLoading = false
    var lastInput= listOf<RoomInfo>()

    fun clearData() {
        this.data = emptyList()
        notifyDataSetChanged()
    }


    fun addData(data: List<RoomInfo>) {
        if (lastInput.isNotEmpty() && data[0].id == lastInput[0].id) {//重复搜索
            return
        }
        lastInput = data
        this.data += data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        bind = ViewItemSearchResultRoomBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(bind, onItemClickListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val roomInfo = data[position]
        if(roomInfo.url!=null)
            GlideUtil.glideImage(roomInfo.url, holder.itemBind.ivCover)
        holder.itemBind.tvTitle.text = roomInfo.name
        holder.itemBind.tvDescription.text = roomInfo.detail
    }

    override fun getItemCount(): Int {
        return data.size
    }

    class ViewHolder(
        val itemBind: ViewItemSearchResultRoomBinding,
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