package com.ysered.signatureview.view

import android.content.Context
import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.ysered.signatureview.R
import com.ysered.signatureview.util.getResolvedColor

class SignatureView(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : View(context, attrs, defStyleAttr) {

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    private val DEFAULT_STROKE_WIDTH = 4 * Resources.getSystem().displayMetrics.density

    // resolve colors on creation state
    private val COLOR_BLACK = context.getResolvedColor(R.color.black)
    private val COLOR_RED = context.getResolvedColor(R.color.red)
    private val COLOR_BLUE = context.getResolvedColor(R.color.blue)

    private val blackPaint: Paint
    private val redPaint: Paint
    private val bluePaint: Paint
    private val paints = mutableListOf<Paint>()

    private var currentPath: Path = Path()
    private val paths = mutableListOf<Path>()

    init {
        val array = context.obtainStyledAttributes(attrs, R.styleable.SignatureView, defStyleAttr, 0)
        val signatureStrokeWidth = array.getDimension(R.styleable.SignatureView_strokeWidth, DEFAULT_STROKE_WIDTH)
        array.recycle()

        isDrawingCacheEnabled = true

        val basePaint = Paint().apply {
            isAntiAlias = true
            style = Paint.Style.STROKE
            strokeWidth = signatureStrokeWidth
            strokeJoin = Paint.Join.ROUND
        }
        blackPaint = Paint(basePaint).apply { color = COLOR_BLACK }
        redPaint = Paint(basePaint).apply { color = COLOR_RED }
        bluePaint = Paint(basePaint).apply { color = COLOR_BLUE }

        paths.add(currentPath)
        paints.add(blackPaint)
    }

    var strokeColor: StrokeColor = StrokeColor.BLACK
        set(value) {
            field = value
            initPaintsAndPaths(value)
        }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val result = event?.let {
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    currentPath.moveTo(event.x, event.y)
                    invalidate()
                }
                MotionEvent.ACTION_MOVE -> {
                    currentPath.lineTo(event.x, event.y)
                    invalidate()
                }
            }
            true
        }
        return result ?: super.onTouchEvent(event)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        for (index in 0 until paths.size) {
            canvas?.drawPath(paths[index], paints[index])
        }
    }

    fun clearDrawing() {
        // clear previous strokes
        paths.forEach { it.reset() }
        invalidate()
        // get rid of unused stroke paths
        paths.clear()
        paints.clear()
        initPaintsAndPaths(strokeColor)
    }

    private fun initPaintsAndPaths(strokeColor: StrokeColor) {
        currentPath = Path()
        paths.add(currentPath)
        val paint = when (strokeColor) {
            StrokeColor.BLACK -> blackPaint
            StrokeColor.RED -> redPaint
            StrokeColor.BLUE -> bluePaint
        }
        paints.add(paint)
    }

    /**
     * Defines possible stroke colors with theirs corresponding resources.
     */
    enum class StrokeColor {
        BLACK, RED, BLUE
    }
}