package com.example.makka_pakka.view

import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import com.example.makka_pakka.MyApplication
import com.example.makka_pakka.R
import com.example.makka_pakka.databinding.ComponentLoadingPicBinding

//上面一个图片加上下面一个文字的加载图标
//只要一显示，图标就会闪烁
class LoadingPic :Fragment(){
    private lateinit var bind: ComponentLoadingPicBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        bind = ComponentLoadingPicBinding.inflate(layoutInflater)
        return bind.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val image = bind.image
        //设置动画
        image.startAnimation(animation)
    }

    companion object {
        fun newInstance() = LoadingPic()

        //闪烁动画，不断重复，3s一来回
        var animation: Animation = AnimationUtils.loadAnimation(MyApplication.instance, R.anim.fade_in_and_out).apply {
            repeatCount = Animation.INFINITE
            duration = 3000
        }
    }

}