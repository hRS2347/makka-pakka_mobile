package com.example.makka_pakka.utils

import android.content.Context
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
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
        params.width = dpToPx(context, value)
        params.height = dpToPx(context, value)
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

    fun getScreenWidth(context: Context): Int {
        return context.resources.displayMetrics.widthPixels
    }

    fun getStatusBarHeight(context: Context): Int {
        val resId = context.resources.getIdentifier(
            "status_bar_height", "dimen", "android"
        )
        return context.resources.getDimensionPixelSize(resId)
    }

    fun fixStatusBarMargin(vararg views: View) {
        views.forEach { view ->
            (view.layoutParams as? ViewGroup.MarginLayoutParams)?.let { lp ->
                lp.topMargin = lp.topMargin + getStatusBarHeight(view.context)
                view.requestLayout()
            }
        }
    }

    fun paddingByStatusBar(view: View) {
        view.setPadding(
            view.paddingLeft,
            view.paddingTop + getStatusBarHeight(view.context),
            view.paddingRight,
            view.paddingBottom
        )
    }

    fun pxToRem(keypadHeight: Int): Float {
        return keypadHeight.toFloat() / 16
    }

}