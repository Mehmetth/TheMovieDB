package com.mehmetpetek.themoviedb.common.extensions

import android.app.Activity
import android.content.pm.ActivityInfo
import android.util.DisplayMetrics
import android.view.View
import android.view.ViewGroup

fun Activity.calculateScreenHeightPercentage(percentage: Int): Int {
    val displayMetrics = DisplayMetrics()
    windowManager.defaultDisplay.getMetrics(displayMetrics)
    return displayMetrics.heightPixels / (percentage * 1000)
}

fun Activity.fullScreen(view: View) {
    window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_FULLSCREEN
            or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
            or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)
    if (actionBar != null) {
        actionBar?.hide()
    }
    requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

    val params = view.layoutParams
    params.width = ViewGroup.LayoutParams.MATCH_PARENT
    params.height = ViewGroup.LayoutParams.MATCH_PARENT
    view.layoutParams = params
}

fun Activity.exitFullScreen(view: View) {
    window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE

    if (actionBar != null) {
        actionBar?.show()
    }

    requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED

    val params = view.layoutParams
    params.width = ViewGroup.LayoutParams.MATCH_PARENT
    params.height = calculateScreenHeightPercentage(40)
    view.layoutParams = params
}