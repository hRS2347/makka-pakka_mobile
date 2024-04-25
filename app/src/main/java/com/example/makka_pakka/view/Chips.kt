package com.example.makka_pakka.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import com.example.makka_pakka.R
import com.example.makka_pakka.utils.ViewUtil

@SuppressLint("Recycle", "ViewConstructor")
class Chips @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null,
    var text: String = ""
) : View(context, attrs) {
    private val paint = Paint()

    //圆角
    private val cornerRadius = ViewUtil.dpToPx(context, 4f).toFloat() // 设置圆角半径
    private val textSize = ViewUtil.spToPx(context, 12f).toFloat() // 设置文字大小
    private val pad = ViewUtil.dpToPx(context, 2f).toFloat() // 设置内边距
    private var width = 0
    private val height = textSize.toInt() + ViewUtil.dpToPx(context, 8f)*2// 设置高度
    private val minWith = 1 * textSize.toInt()
    private var textColNon = ResourcesCompat.getColor(resources, R.color.stroke_grey, null)
    private var textCol = Color.WHITE
    private val bgColor = ResourcesCompat.getColor(resources, R.color.white, null)
    //计算整体宽高，处理wrap_content的情况
    //如果宽高都是wrap_content，则宽高为字数*textSize+padding*2
    //如果宽度是wrap_content，则宽度为字数*textSize+padding*2
    //如果高度是wrap_content，则高度为textSize+padding*2

    init {
        layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

    //wrap_content
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        width = text.length * textSize.toInt() + pad.toInt() * 2
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        if (width < minWith) {
            width = minWith
        }
        //处理wrap_content情况
        if (widthMode == MeasureSpec.AT_MOST && heightMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(width, height)
        } else if (widthMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(width, heightSize)
        } else if (heightMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(widthSize, height)
        }
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        paint.color = bgColor
        //draw the button
        paint.style = Paint.Style.FILL
        canvas.drawRoundRect(
            0f,
            0f,
            width.toFloat(),
            height.toFloat(),
            cornerRadius,
            cornerRadius,
            paint
        )

        //draw the text
        paint.color = textColNon
        paint.textSize = textSize
        paint.textAlign = Paint.Align.CENTER
        val fontMetrics = paint.fontMetrics
        val distance = (fontMetrics.bottom - fontMetrics.top) / 2 - fontMetrics.bottom
        val baseline = height / 2 + distance
        canvas.drawText(text, width / 2.toFloat(), baseline, paint)
    }
}