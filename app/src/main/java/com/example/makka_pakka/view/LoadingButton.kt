package com.example.makka_pakka.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.widget.Button
import androidx.appcompat.widget.AppCompatButton
import com.example.makka_pakka.R

@SuppressLint("Recycle")
class LoadingButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null,
) : AppCompatButton(context, attrs) {
    //圆
    private val cornerRadius: Float = 300f
    private var loadingState:Boolean?
    private val rectF: RectF = RectF()
    private val backgroundPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)// 背景画笔
    private var image: Int = 0

    init {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.LoadingButton)
        loadingState = typedArray.getBoolean(R.styleable.LoadingButton_loading_state, false)
        image = typedArray.getResourceId(R.styleable.LoadingButton_image, 0)
    }

    override fun onDraw(canvas: Canvas) {
        canvas.let {
            // 绘制圆角矩形背景
            rectF.set(0f, 0f, width.toFloat(), height.toFloat())
            canvas.drawRoundRect(rectF, cornerRadius, cornerRadius, backgroundPaint)
            // 绘制图标
            if (loadingState == true) {
                // 绘制加载中图标
                // 绘制加载中图标
                val drawable = resources.getDrawable(image, null)
                drawable.setBounds(0, 0, width, height)
                drawable.draw(canvas)
            }
            // 绘制按钮文本
            super.onDraw(canvas)
        }
    }

    //点击事件，切换选中状态
    override fun performClick(): Boolean {
        loadingState = !loadingState!!
        invalidate()
        return super.performClick()
    }
}