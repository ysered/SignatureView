package com.ysered.signatureview

import android.graphics.Bitmap
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.ysered.signatureview.util.findFragmentByTag
import com.ysered.signatureview.util.replaceFragment

class MainActivity : AppCompatActivity(), SignatureFragment.Callback {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            replaceFragment(ProfileFragment(), ProfileFragment.TAG)
        }
    }

    override fun onDrawingDone(signatureBitmap: Bitmap?) {
        val fragment = findFragmentByTag<ProfileFragment>(ProfileFragment.TAG)
        fragment?.signatureBitmap = signatureBitmap
    }
}
