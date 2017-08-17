package com.ysered.signatureview

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.ysered.signatureview.util.replaceFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            replaceFragment(ProfileFragment())
        }
    }
}
