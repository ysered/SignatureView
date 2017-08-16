package com.ysered.signatureview.util

import android.content.Context
import android.support.v4.content.ContextCompat

fun Context.getResolvedColor(colorResId: Int): Int
        = ContextCompat.getColor(this, colorResId)
