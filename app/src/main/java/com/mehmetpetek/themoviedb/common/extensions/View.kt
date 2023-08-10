package com.mehmetpetek.themoviedb.common.extensions

import android.view.View

fun View.visible() {
    visibility = View.VISIBLE
}

fun View.gone() {
    visibility = View.GONE
}

fun View.visibleIf(isVisible: Boolean) {
    this.visibility = if (isVisible) View.VISIBLE else View.GONE
}