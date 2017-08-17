package com.ysered.signatureview.util

import android.app.Activity
import android.content.pm.ActivityInfo
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.View

private var systemUiVisibility: Int? = null

/**
 * Forces to change orientation to landscape.
 */
fun Activity.setLandscapeOrientation() {
    requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
}

/**
 * Resets orientation to its initial state if it was changed (e.g. by [setLandscapeOrientation]).
 */
fun Activity.resetOrientation() {
    resources?.configuration?.orientation?.let {
        requestedOrientation = it
    }
}

/**
 * Shows dark transparent overlay on system's status bar and navigation controls to make them less accent.
 */
fun Activity.dimStatusBar() {
    systemUiVisibility = window?.decorView?.systemUiVisibility
    window?.decorView?.systemUiVisibility = View.SYSTEM_UI_FLAG_LOW_PROFILE
}

/**
 * Reset's status bar to its previous state if it was changed (e.g. by [dimStatusBar]).
 */
fun Activity.resetStatusBar() {
    systemUiVisibility?.let {
        window?.decorView?.systemUiVisibility = it
    }
    systemUiVisibility = null
}

/**
 * Show/hide [android.support.v7.app.ActionBar] property.
 */
var Activity.isShowActionBar: Boolean
    get() = (this as? AppCompatActivity)?.supportActionBar?.isShowing ?: false
    set(value) {
        (this as? AppCompatActivity)?.supportActionBar?.let {
            if (value) {
                it.show()
            } else {
                it.hide()
            }
        }
    }

/**
 * Replaces fragment using [android.support.v4.app.FragmentManager].
 */
fun AppCompatActivity.replaceFragment(fragment: Fragment, addToBackStack: Boolean = false) {
    val transaction = supportFragmentManager.beginTransaction()
            .replace(android.R.id.content, fragment, null)
    if (addToBackStack) {
        transaction.addToBackStack(null)
    }
    transaction.commit()
}

fun Activity.replaceFragment(fragment: Fragment, addToBackStack: Boolean = false) {
    (this as? AppCompatActivity)?.replaceFragment(fragment, addToBackStack)
}
