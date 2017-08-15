package com.ysered.signatureview.view

import android.content.Context
import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.ysered.signatureview.R

class SignatureView(context: Context, attrs: AttributeSet?, defStyleAttr: Int): View(context, attrs, defStyleAttr) {

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    private val DEFAULT_STROKE_WIDTH = 4 * Resources.getSystem().displayMetrics.density

    private val signatureStrokePaint: Paint

    private val path = Path()

    var strokeColor: Int = Color.BLACK

    init {
        val array = context.obtainStyledAttributes(attrs, R.styleable.SignatureView, defStyleAttr, 0)
        val signatureStrokeWidth = array.getDimension(R.styleable.SignatureView_strokeWidth, DEFAULT_STROKE_WIDTH)
        array.recycle()

        isDrawingCacheEnabled = true

        signatureStrokePaint = Paint().apply {
            isAntiAlias = true
            color = strokeColor
            style = Paint.Style.STROKE
            strokeWidth = signatureStrokeWidth
            strokeJoin = Paint.Join.ROUND
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val result = event?.let {
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    path.moveTo(event.x, event.y)
                    invalidate()
                }
                MotionEvent.ACTION_MOVE -> {
                    path.lineTo(event.x, event.y)
                    invalidate()
                }
            }
            true
        }
        return result ?: super.onTouchEvent(event)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.drawPath(path, signatureStrokePaint)
    }
}