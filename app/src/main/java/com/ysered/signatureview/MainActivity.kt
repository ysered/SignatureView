package com.ysered.signatureview

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.RadioGroup
import com.ysered.signatureview.view.SignatureView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val signatureView = findViewById<SignatureView>(R.id.signatureView)

        findViewById<RadioGroup>(R.id.radioGroup).setOnCheckedChangeListener { radioGroup, _ ->
            signatureView.strokeColor = when (radioGroup.checkedRadioButtonId) {
                R.id.redButton -> SignatureView.StrokeColor.RED
                R.id.blueButton -> SignatureView.StrokeColor.BLUE
                else -> SignatureView.StrokeColor.BLACK
            }
        }

        findViewById<Button>(R.id.clearButton).setOnClickListener {
            signatureView.clearDrawing()
        }
    }
}
