package com.ysered.signatureview

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.RadioGroup
import com.ysered.signatureview.util.dimStatusBar
import com.ysered.signatureview.util.isShowActionBar
import com.ysered.signatureview.util.setLandscapeOrientation
import com.ysered.signatureview.view.SignatureView

/**
 * Represents screen for signature drawing.
 */
class SignatureFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View?
            = inflater?.inflate(R.layout.fragment_signature, container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        view?.let {
            val signatureView = it.findViewById<SignatureView>(R.id.signatureView)

            it.findViewById<RadioGroup>(R.id.radioGroup)?.setOnCheckedChangeListener { radioGroup, _ ->
                signatureView?.strokeColor = when (radioGroup.checkedRadioButtonId) {
                    R.id.redButton -> SignatureView.StrokeColor.RED
                    R.id.blueButton -> SignatureView.StrokeColor.BLUE
                    else -> SignatureView.StrokeColor.BLACK
                }
            }

            it.findViewById<ImageButton>(R.id.buttonClose)?.setOnClickListener {
                activity.onBackPressed()
            }
            it.findViewById<ImageView>(R.id.buttonDone)?.setOnClickListener {
                val bitmap = signatureView?.signatureBitmap
                bitmap
            }

            it.findViewById<Button>(R.id.clearButton)?.setOnClickListener {
                signatureView?.clearDrawing()
            }
        }

    }

    override fun onStart() {
        activity.apply {
            setLandscapeOrientation()
            dimStatusBar()
            isShowActionBar = false
        }
        super.onStart()
    }
}
