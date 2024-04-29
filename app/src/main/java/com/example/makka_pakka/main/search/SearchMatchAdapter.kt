package com.example.makka_pakka.main.search

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.makka_pakka.databinding.ViewItemSearchMatchBinding

class SearchMatchAdapter(
    private var data: List<String>,
    private var targetInput: String,
    private var onItemClickListener: OnItemClickListener
) :
    RecyclerView.Adapter<SearchMatchAdapter.ViewHolder>() {
    private lateinit var bind: ViewItemSearchMatchBinding

    @SuppressLint("NotifyDataSetChanged")
    fun setData(data: List<String>, targetInput: String) {
        this.data = data
        this.targetInput = targetInput
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        bind =
            ViewItemSearchMatchBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(bind, onItemClickListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val str = data[position]
        val matchPos = str.indexOf(targetInput)
        if (matchPos == -1) {
            holder.itemBind.tvPreMatch.text = str
            holder.itemBind.tvMatch.text = ""
            holder.itemBind.tvPostMatch.text = ""
            return
        }
        holder.itemBind.tvPreMatch.text = str.substring(0, matchPos)
        holder.itemBind.tvMatch.text = targetInput
        holder.itemBind.tvPostMatch.text = str.substring(matchPos + targetInput.length)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    class ViewHolder(
        val itemBind: ViewItemSearchMatchBinding,
        private val onItemClickListener: OnItemClickListener
    ) :
        RecyclerView.ViewHolder(itemBind.root) {
        init {
            itemBind.root.setOnClickListener {
                onItemClickListener.onItemClick(itemBind.tvPreMatch.text.toString() + itemBind.tvMatch.text + itemBind.tvPostMatch.text)
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(str: String)
    }
}