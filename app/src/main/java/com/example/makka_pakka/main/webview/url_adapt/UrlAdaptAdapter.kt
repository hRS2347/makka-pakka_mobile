package com.example.makka_pakka.main.webview.url_adapt

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.makka_pakka.databinding.ViewItemUrlAdaptBinding

data class UrlKeyValue(
    var key: String = "",
    var value: String = ""
)

class UrlAdaptAdapter(
    var urls: List<UrlKeyValue>
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
        holder.itemBind.et.setText(urls[position].value)
        holder.itemBind.field.hint = urls[position].key
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
            itemBind.btn.setOnClickListener {
                listener.onItemClick(adapterPosition, itemBind.et.text.toString())
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int, url: String)
    }

}