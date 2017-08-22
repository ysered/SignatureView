package com.ysered.signatureview.util

import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity


fun Fragment.finish() {
    val compatActivity = activity as? AppCompatActivity
    compatActivity?.supportFragmentManager?.popBackStack()
}
