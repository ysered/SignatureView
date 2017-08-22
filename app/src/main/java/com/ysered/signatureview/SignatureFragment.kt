package com.ysered.signatureview

import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.RadioGroup
import com.ysered.signatureview.util.*
import com.ysered.signatureview.view.SignatureView

/**
 * Represents screen for signature drawing.
 */
class SignatureFragment : Fragment() {

    companion object {
        val ARG_DESIRED_WIDTH = "arg_desired_width"

        fun newInstance(desiredWidth: Int): SignatureFragment {
            return SignatureFragment().apply {
                arguments = Bundle(1).apply {
                    putInt(ARG_DESIRED_WIDTH, desiredWidth)
                }
            }
        }
    }

    private var callback: Callback? = null
    private var desiredWidth: Int = 0

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        callback = activity as? Callback
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        desiredWidth = arguments.getInt(ARG_DESIRED_WIDTH, 0)
    }

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
                val scaledBitmap = signatureView.signatureBitmap?.let {
                    scaleBitmapByWidth(it, desiredWidth)
                }
                finish()
                callback?.onDrawingDone(scaledBitmap)
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

    /**
     * Defines callback to communicate with parent activity.
     */
    interface Callback {
        /**
         * Should be called when user pressed done button.
         *
         * [signatureBitmap] bitmap image with signature drawing
         */
        fun onDrawingDone(signatureBitmap: Bitmap?)
    }
}
