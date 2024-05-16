package com.example.makka_pakka.main.webview.url_adapt

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.makka_pakka.databinding.ViewItemUrlAdaptBinding

class UrlAdaptAdapter(
    var urls: List<String>
) :
    RecyclerView.Adapter<UrlAdaptAdapter.ViewHolder>() {
    private lateinit var bind: ViewItemUrlAdaptBinding
    lateinit var listener: OnItemClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        bind = ViewItemUrlAdaptBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(bind, listener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemBind.et.setText(urls[position])
    }

    override fun getItemCount(): Int {
        return urls.size
    }

    class ViewHolder(
        val itemBind: ViewItemUrlAdaptBinding,
        val listener: OnItemClickListener
    ) :
        RecyclerView.ViewHolder(itemBind.root) {
        init {
            itemBind.root.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

}