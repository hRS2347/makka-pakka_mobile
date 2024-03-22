package com.example.makka_pakka.utils

import android.content.Context
import android.util.TypedValue
import android.widget.ImageView
import com.example.makka_pakka.MainActivity

object ViewUtil {
    fun dpToPx(context: Context, dp: Float): Int {
        val density = context.resources.displayMetrics.density
        return (dp * density + 0.5f).toInt()
    }

    fun spToPx(context: Context, sp: Float): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP,
            sp,
            context.resources.displayMetrics
        ).toInt()
    }

    fun setSquareSize(context: Context, value: Float, view: ImageView) {
        val params = view.layoutParams
        params.width = dpToPx(context, value)  // 设置宽度为100dp
        params.height = dpToPx(context, value) // 设置高度为200dp
        view.layoutParams = params
    }

    fun sp2px(context: Context, spValue: Int): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP,
            spValue.toFloat(),
            context.resources.displayMetrics
        ).toInt()
    }

    fun hideKeyboard(mainActivity: MainActivity) {
        val imm = mainActivity.getSystemService(Context.INPUT_METHOD_SERVICE) as android.view.inputmethod.InputMethodManager
        imm.hideSoftInputFromWindow(mainActivity.window.decorView.windowToken, 0)
        //清空焦点
        mainActivity.window.decorView.clearFocus()
    }


    fun calculateSize(context: Context){

    }

}