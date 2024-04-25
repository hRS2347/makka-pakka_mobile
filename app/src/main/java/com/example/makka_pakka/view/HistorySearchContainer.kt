package com.example.makka_pakka.view

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import com.example.makka_pakka.utils.ViewUtil

class HistorySearchContainer @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ViewGroup(context, attrs, defStyleAttr) {
    // 间距
    private val perHor = ViewUtil.dpToPx(context, 12f)
    private val perVerLine = ViewUtil.dpToPx(context, 16f)
    private val perVer = ViewUtil.dpToPx(context, 8f)
    var sumChipsList = listOf<String>()
        set(value) {
            field = value
            refreshShow()
        }

    fun refreshShow() {
        //首先，先统计已经选择的标签
        removeAllViews()
        for (i in sumChipsList) {
            val button = Chips(context, text = i)
            button.setOnClickListener {
                onChipClickListener.onChipClick(i)
            }
            addView(button)
        }
        requestLayout()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        // Measure children
        measureChildren(widthMeasureSpec, heightMeasureSpec)

        // Determine this view group's size
        val width = resolveSize(suggestedMinimumWidth, widthMeasureSpec)
        val height = resolveSize(suggestedMinimumHeight, heightMeasureSpec)
        setMeasuredDimension(width, height)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        //切成一个一个尺寸为的方格，每个方格放置一个
        // 初始位置，随机0-30
        var currentLeft = 0
        var currentTop = 0

        //随机间隔 12-30
        for (i in 0 until childCount) {
            val child = getChildAt(i)
            // 在当前位置放置子视图，透明背景
            if (currentLeft + child.measuredWidth > width) { // 如果超出容器宽度
                currentTop += child.measuredHeight + perVerLine // 换行
                currentLeft = 0
                if (currentTop + child.measuredHeight + 2 * perVer > height) { // 如果超出容器高度
                    break
                }
            }
            val bias = 0
            child.layout(
                currentLeft,
                currentTop + bias,
                child.measuredWidth + currentLeft,
                child.measuredHeight + currentTop + bias
            )
            // 更新位置
            currentLeft += child.measuredWidth + perHor
        }
    }

     lateinit var onChipClickListener: OnChipClickListener
    interface OnChipClickListener {
        fun onChipClick(chip: String)
    }
}