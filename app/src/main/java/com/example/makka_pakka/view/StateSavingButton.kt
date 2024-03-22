package com.example.makka_pakka.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatButton
import com.example.makka_pakka.R

@SuppressLint("Recycle")
class StateSavingButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null,
) : AppCompatButton(context, attrs) {
    private var cornerRadius: Float?
    private var selectedColor: Int?// 选中状态颜色
    private var nonSelectedColor: Int?// 选中状态颜色
    private var initState:Boolean?
    private val rectF: RectF = RectF()
    private val backgroundPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)// 背景画笔

    init {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.StateSavingButton)
        selectedColor = typedArray.getColor(R.styleable.StateSavingButton_selected_color, 0)
        nonSelectedColor = typedArray.getColor(R.styleable.StateSavingButton_non_selected_color, 0)
        initState = typedArray.getBoolean(R.styleable.StateSavingButton_init_state, false)
        cornerRadius = typedArray.getDimension(R.styleable.StateSavingButton_corner_radius, 0f)
    }

    override fun onDraw(canvas: Canvas) {
        canvas.let {
            // 绘制圆角矩形背景
            rectF.set(0f, 0f, width.toFloat(), height.toFloat())
            backgroundPaint.color = if (initState == true) selectedColor!! else nonSelectedColor!!
            canvas.drawRoundRect(rectF, cornerRadius!!, cornerRadius!!, backgroundPaint)
            //文字颜色，根据选中状态设置
            setTextColor(if (initState == true) nonSelectedColor!! else selectedColor!!)
            // 绘制按钮文本
            super.onDraw(canvas)
        }
    }

    fun setInitState(initState: Boolean){
        this.initState = initState
        invalidate()
    }

    fun getInitState(): Boolean?{
        return initState
    }
}