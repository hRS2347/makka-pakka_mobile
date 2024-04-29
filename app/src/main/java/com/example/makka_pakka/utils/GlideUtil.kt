package com.example.makka_pakka.utils

import android.content.Context
import android.util.Log
import android.widget.ImageView
import androidx.core.text.isDigitsOnly
import com.bumptech.glide.Glide
import com.example.makka_pakka.R

object GlideUtil {
    fun glideImage( uri: String, view: ImageView) {
        try {
            Glide.with(view.context).load(
                if (uri.isDigitsOnly()) uri.toInt()
                else uri
            ).error(R.drawable.tab_mine).placeholder(R.drawable.tab_mine)
                .into(view)
        } catch (e: Exception) {
            Log.e(
                "GlideUtil",
                "glideAvatar: ${e.message}"
            )
        }
    }

    fun loadGif(context: Context, id: Int, view: ImageView) {
        try {

            Glide.with(context).asGif().load(id).error(R.drawable.tab_mine)
                .placeholder(R.drawable.tab_mine)
                .into(view)
        } catch (e: Exception) {
            Log.e(
                "GlideUtil",
                "glideAvatar: ${e.message}"
            )
        }
    }

    fun loadAvatar(iv: ImageView, avatarUrl: String) {
        Glide.with(iv.context).load(avatarUrl).circleCrop()
            .error(R.drawable.logo)
            .placeholder(R.drawable.logo)
            .into(iv)
    }
}