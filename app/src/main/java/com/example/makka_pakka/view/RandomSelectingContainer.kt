package com.example.makka_pakka.view

import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import com.example.makka_pakka.MAX_NUM_OF_SELECTED_HOBBY
import com.example.makka_pakka.utils.ViewUtil
import java.util.ArrayList
import kotlin.random.Random

class RandomSelectingContainer @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ViewGroup(context, attrs, defStyleAttr) {
    // 间距
    private val perHor = ViewUtil.dpToPx(context, 12f)
    private val perVerLine = ViewUtil.dpToPx(context, 16f)
    private val perVer = ViewUtil.dpToPx(context, 8f)

    var funChange:(Int)->Unit= {}
        set(value) {
            field = value
            onSelectedNumChanged()
        }
    private fun onSelectedNumChanged(){
        funChange(selectedNum)
    }

    var sumTagList = listOf<String>()
        set(value) {
            field = value
            for (i in sumTagList) {
                selectedMap[i] = false
            }
            selectedNum = 0
            refreshShow()
        }
    var selectedNum = 0

    //初始值全部为false
    private var selectedMap = mutableMapOf<String, Boolean>()

    fun refreshShow() {
        //首先，先统计已经选择的标签
        var nextList = mutableListOf<String>()
        for (i in 0 until childCount) {
            val child = getChildAt(i) as HobbySelectingButton
            if (child.isS) {
                if (!selectedMap[child.text]!!) {
                    selectedMap[child.text] = false//如果不在sumTagList中，就不显示
                    continue
                }
                nextList.add(child.text)
            }
        }
        removeAllViews()
        //如果不足40个，就随机添加
        while (nextList.size < 40) {
            //产生随机数
            val next = sumTagList[Random.nextInt(0, sumTagList.size)]
            if (!nextList.contains(next)) {
                nextList.add(next)
            }
        }

        for (i in nextList) {
            val button = HobbySelectingButton(context, text = i)
            button.isS = selectedMap[i]!!
            button.setOnClickListener {
                if(selectedNum==MAX_NUM_OF_SELECTED_HOBBY && button.isS){
                    button.isS = false
                    return@setOnClickListener
                }
                if (button.isS) {
                    selectedNum++
                } else {
                    selectedNum--
                }
                onSelectedNumChanged()
                selectedMap[i] = button.isS
            }
            addView(button)
            animateFadeIn(button)
        }
        requestLayout()
    }

    private fun animateFadeIn(view: View) {
        val fadeInAnimator = ObjectAnimator.ofFloat(view, View.ALPHA, 0f, 1f)
        fadeInAnimator.duration = 500
        fadeInAnimator.start()
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
        //切成一个一个尺寸为 180dp * 60dp的方格，每个方格放置一个
        // 初始位置，随机0-30
        var currentLeft = perHor * Random.nextInt(1, 2)
        var currentTop = 0

        //随机间隔 12-30
        for (i in 0 until childCount) {
            val child = getChildAt(i)
            // 在当前位置放置子视图，透明背景
            if (currentLeft + child.measuredWidth > width) { // 如果超出容器宽度
                currentTop += child.measuredHeight + perVerLine // 换行
                currentLeft = perHor * Random.nextInt(0, 2)
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
            currentLeft += child.measuredWidth + perHor * Random.nextInt(1, 4)
        }
    }

    fun getList(): ArrayList<String> {
        val list = ArrayList<String>()
        for (i in 0 until childCount) {
            val child = getChildAt(i) as HobbySelectingButton
            if (child.isS) {
                list.add(child.text)
            }
        }
        return list
    }
}