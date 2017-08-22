package com.ysered.signatureview

import android.graphics.Bitmap
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import com.ysered.signatureview.util.isShowActionBar
import com.ysered.signatureview.util.replaceFragment
import com.ysered.signatureview.util.resetOrientation
import com.ysered.signatureview.util.resetStatusBar

class ProfileFragment : Fragment() {

    companion object {
        val TAG = "ProfileFragment"
    }

    private var signatureImage: ImageView? = null

    var signatureBitmap: Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View?
            = inflater?.inflate(R.layout.fragment_profile, container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        view?.let {
            signatureImage = it.findViewById<ImageView>(R.id.signatureImage)?.apply {
                setImageBitmap(signatureBitmap)
            }

            it.findViewById<Button>(R.id.signatureViewButton)?.setOnClickListener {
                val fragment = SignatureFragment.newInstance(signatureImage?.width ?: 0)
                activity.replaceFragment(fragment, addToBackStack = true)
            }
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
