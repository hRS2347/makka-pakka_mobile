package com.example.makka_pakka.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import com.example.makka_pakka.R
import com.example.makka_pakka.TAG_COLOR_LIST
import com.example.makka_pakka.utils.ViewUtil
import kotlin.random.Random

@SuppressLint("Recycle", "ViewConstructor")
class HobbySelectingButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null,
    var text: String = ""//限制最大为120dp
) : View(context, attrs) {
    var isS = false
    private val paint = Paint()

    //圆角
    private val cornerRadius = ViewUtil.dpToPx(context, 30f).toFloat() // 设置圆角半径为 16dp
    private val strokeWidth = ViewUtil.dpToPx(context, 1f).toFloat() // 设置描边宽度为 4dp
    private val textSize = ViewUtil.spToPx(context, 18f).toFloat() // 设置文字大小为 16sp
    private val pad = ViewUtil.dpToPx(context, 16f).toFloat() // 设置内边距为 12dp
    private var width = 0
    private val height = textSize.toInt() + ViewUtil.dpToPx(context, 24f) + strokeWidth.toInt() * 2
    private val minWith = 4 * textSize.toInt() + strokeWidth.toInt() * 2
    private var strokeCol: Int = TAG_COLOR_LIST[Random.nextInt(0, TAG_COLOR_LIST.size)]
    private var textColNon = Color.GRAY
    private var textCol = Color.WHITE
    private val nonSelectedColor = ResourcesCompat.getColor(resources, R.color.white, null)
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
        width = text.length * textSize.toInt() + strokeWidth.toInt() * 2 + pad.toInt() * 2
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        if (width < minWith) {
            width = minWith
        }
        //处理wrap_contentde情况
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
        //描边stroke
        paint.color = if (isS) nonSelectedColor else strokeCol
        paint.style = Paint.Style.FILL
        paint.strokeWidth = strokeWidth
        canvas.drawRoundRect(
            0f,
            0f,
            width.toFloat(),
            height.toFloat(),
            cornerRadius,
            cornerRadius,
            paint
        )

        //check if the button is selected
        if (isS) {
            paint.color =
                ResourcesCompat.getColor(
                    resources,
                    strokeCol,
                    null
                )

        } else {
            paint.color = nonSelectedColor
        }
        //draw the button
        paint.style = Paint.Style.FILL
        canvas.drawRoundRect(
            strokeWidth,
            strokeWidth,
            width.toFloat() - strokeWidth,
            height.toFloat() - strokeWidth,
            cornerRadius,
            cornerRadius,
            paint
        )

        //draw the text
        paint.color = isS.let { if (it) textCol else textColNon }
        paint.textSize = textSize
        paint.textAlign = Paint.Align.CENTER
        val fontMetrics = paint.fontMetrics
        val distance = (fontMetrics.bottom - fontMetrics.top) / 2 - fontMetrics.bottom
        val baseline = height / 2 + distance
        canvas.drawText(text, width / 2.toFloat(), baseline, paint)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_UP -> {
                performClick()
                return true
            }
        }
        return super.onTouchEvent(event)
    }

    override fun performClick(): Boolean {
        isS = !isS
        invalidate()
        return super.performClick()
    }
}