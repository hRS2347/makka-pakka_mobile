package com.example.makka_pakka.main.search

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.makka_pakka.R
import com.example.makka_pakka.databinding.ViewItemSearchMatchBinding
import com.example.makka_pakka.databinding.ViewItemTabBinding

class SearchTabAdapter(
    private var data: List<SearchResultViewModel.SearchState>,
    private var selectedType: SearchResultViewModel.SearchState,
    private var onItemClickListener: OnItemClickListener
) :
    RecyclerView.Adapter<SearchTabAdapter.ViewHolder>() {
    private lateinit var bind: ViewItemTabBinding

    @SuppressLint("NotifyDataSetChanged")
    fun setData(selectedType:  SearchResultViewModel.SearchState) {
        this.selectedType = selectedType
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        bind = ViewItemTabBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(bind, onItemClickListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val tab = data[position]
        holder.itemBind.tv.text = tab.value
        if (tab == selectedType) {
            holder.itemBind.tv.setTextColor(holder.itemBind.root.resources.getColor(R.color.white))
            holder.itemBind.tv.background = ResourcesCompat.getDrawable(holder.itemBind.root.resources, R.drawable.round_card_primary, null)
        } else {
            holder.itemBind.tv.setTextColor(holder.itemBind.root.resources.getColor(R.color.primary_color))
            holder.itemBind.tv.background = ResourcesCompat.getDrawable(holder.itemBind.root.resources, R.drawable.round_card, null)
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    class ViewHolder(
        val itemBind: ViewItemTabBinding,
        private val onItemClickListener: OnItemClickListener
    ) :
        RecyclerView.ViewHolder(itemBind.root) {
        init {
            itemBind.root.setOnClickListener {
                onItemClickListener.onItemClick(adapterPosition)
                Log.d("SearchTabAdapter", "onItemClick: $adapterPosition")
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(pos: Int)
    }
}