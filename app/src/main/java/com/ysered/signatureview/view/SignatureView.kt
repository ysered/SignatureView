package com.ysered.signatureview.view

import android.content.Context
import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.DashPathEffect
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

    private val ONE_DP = Resources.getSystem().displayMetrics.density

    //defaults
    private val DEFAULT_STROKE_WIDTH = 4 * ONE_DP
    private val DEFAULT_BASELINE_COLOR = context.getResolvedColor(R.color.baselineDashColor)
    private val DEFAULT_BASELINE_STROKE_WIDTH = 4 * ONE_DP
    private val DEFAULT_BASELINE_STROKE_MARGIN = 16 * ONE_DP

    private val COLOR_BLACK = context.getResolvedColor(R.color.black)
    private val COLOR_RED = context.getResolvedColor(R.color.red)
    private val COLOR_BLUE = context.getResolvedColor(R.color.blue)

    // paints
    private val blackPaint: Paint
    private val redPaint: Paint
    private val bluePaint: Paint
    private val paints = mutableListOf<Paint>()
    private val dashPaint: Paint

    // paths
    private val dashPath = Path()
    private var currentPath = Path()
    private val paths = mutableListOf<Path>()

    init {
        val array = context.obtainStyledAttributes(attrs, R.styleable.SignatureView, defStyleAttr, 0)
        val signatureStrokeWidth = array.getDimension(R.styleable.SignatureView_strokeWidth, DEFAULT_STROKE_WIDTH)
        val baselineColor = array.getColor(R.styleable.SignatureView_baselineColor, DEFAULT_BASELINE_COLOR)
        val baselineStrokeWidth = array.getDimension(R.styleable.SignatureView_baselineStrokeWidth, DEFAULT_BASELINE_STROKE_WIDTH)
        val baselineDashWidth = array.getDimension(R.styleable.SignatureView_baselineDashWidth, 30f)
        val baselineDashGap = array.getDimension(R.styleable.SignatureView_baselineDashWidth, 20f)
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

        dashPaint = Paint().apply {
            isAntiAlias = true
            style = Paint.Style.STROKE
            color = baselineColor
            strokeWidth = baselineStrokeWidth
            pathEffect = DashPathEffect(floatArrayOf(baselineDashWidth, baselineDashGap), 0f)
        }
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

    override fun onSizeChanged(width: Int, height: Int, oldWidth: Int, oldHeight: Int) {
        super.onSizeChanged(width, height, oldWidth, oldHeight)
        dashPath.reset()
        dashPath.moveTo(DEFAULT_BASELINE_STROKE_MARGIN, width / 4f * 3f)
        dashPath.lineTo(width.toFloat() - DEFAULT_BASELINE_STROKE_MARGIN, width / 4f * 3f)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.let {
            it.drawPath(dashPath, dashPaint)
            for (index in 0 until paths.size) {
                it.drawPath(paths[index], paints[index])
            }
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
