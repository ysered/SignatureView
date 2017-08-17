package com.ysered.signatureview

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.ysered.signatureview.util.isShowActionBar
import com.ysered.signatureview.util.replaceFragment
import com.ysered.signatureview.util.resetOrientation
import com.ysered.signatureview.util.resetStatusBar

class ProfileFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View?
            = inflater?.inflate(R.layout.fragment_profile, container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        view?.findViewById<Button>(R.id.signatureViewButton)?.setOnClickListener {
            activity.replaceFragment(SignatureFragment(), addToBackStack = true)
        }
    }

    override fun onStart() {
        activity.apply {
            resetOrientation()
            resetStatusBar()
            isShowActionBar = true
        }
        super.onStart()
    }
}