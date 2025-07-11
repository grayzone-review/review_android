package com.team.common.feature_api.extension

import android.content.Context

val Context.screenHeightPx: Int
    get() = resources.displayMetrics.heightPixels

val Context.screenWidthPx: Int
    get() = resources.displayMetrics.widthPixels

val Context.density: Float
    get() = resources.displayMetrics.density

val Context.screenHeightDp: Int
    get() = (screenHeightPx / density).toInt()

val Context.screenWidthDp: Int
    get() = (screenWidthPx / density).toInt()
