package com.bakharaalief.learnuikit.uikit

import android.content.Context
import android.content.res.Resources
import androidx.core.content.ContextCompat

val Float.px: Float
    get() = Resources.getSystem().displayMetrics.density.times(this)

fun getColorCompat(context: Context, color: Int) = ContextCompat.getColor(context, color)