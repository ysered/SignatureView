package com.ysered.signatureview.view

import android.content.Context
import android.content.res.Resources
import android.graphics.*
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
    private val DEFAULT_STROKE_WIDTH = 4f * ONE_DP
    private val DEFAULT_BASELINE_COLOR = context.getResolvedColor(R.color.baselineDashColor)
    private val DEFAULT_BASELINE_STROKE_WIDTH = 3f * ONE_DP
    private val DEFAULT_BASELINE_STROKE_MARGIN = 16f * ONE_DP
    private val DEFAULT_BASELINE_DASH_WIDTH = 30f
    private val DEFAULT_BASELINE_DASH_GAP = 20f

    private val COLOR_BLACK = context.getResolvedColor(R.color.black)
    private val COLOR_RED = context.getResolvedColor(R.color.red)
    private val COLOR_BLUE = context.getResolvedColor(R.color.blue)

    // paints
    private val blackPaint: Paint
    private val redPaint: Paint
    private val bluePaint: Paint
    private val paints = mutableListOf<Paint>()
    private val baselinePaint: Paint

    // paths
    private val baselinePath = Path()
    private var currentPath = Path()
    private val paths = mutableListOf<Path>()

    private var signatureBitmapWidth = 0
    private var signatureBitmapHeight = 0
    private var signatureCanvas: Canvas? = null

    init {
        val array = context.obtainStyledAttributes(attrs, R.styleable.SignatureView, defStyleAttr, 0)
        val signatureStrokeWidth = array.getDimension(R.styleable.SignatureView_strokeWidth, DEFAULT_STROKE_WIDTH)
        val baselineColor = array.getColor(R.styleable.SignatureView_baselineColor, DEFAULT_BASELINE_COLOR)
        val baselineStrokeWidth = array.getDimension(R.styleable.SignatureView_baselineStrokeWidth, DEFAULT_BASELINE_STROKE_WIDTH)
        val baselineDashWidth = array.getDimension(R.styleable.SignatureView_baselineDashWidth, DEFAULT_BASELINE_DASH_WIDTH)
        val baselineDashGap = array.getDimension(R.styleable.SignatureView_baselineDashWidth, DEFAULT_BASELINE_DASH_GAP)
        array.recycle()

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

        baselinePaint = Paint().apply {
            isAntiAlias = true
            style = Paint.Style.STROKE
            color = baselineColor
            strokeWidth = baselineStrokeWidth
            pathEffect = DashPathEffect(floatArrayOf(baselineDashWidth, baselineDashGap), 0f)
        }
    }

    /**
     * Changes line color.
     */
    var strokeColor: StrokeColor = StrokeColor.BLACK
        set(value) {
            field = value
            initDrawing(value)
        }

    /**
     * Returns bitmap with signature drawing.
     */
    var signatureBitmap: Bitmap? = null
        private set

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        baselinePath.reset()
        val stopY = bottom / 5f * 4f
        baselinePath.moveTo(DEFAULT_BASELINE_STROKE_MARGIN, stopY)          // first point (x, y)
        baselinePath.lineTo(right - DEFAULT_BASELINE_STROKE_MARGIN, stopY)  // connect with second point (x, y)
        signatureBitmapWidth = width
        signatureBitmapHeight = height
    }

    /**
     * Overrides touch events to remember paths of user's finger movements.
     */
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

    /**
     * Draws colored lines on view and result signature bitmap canvases.
     */
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.drawPath(baselinePath, baselinePaint)
        for (index in 0 until paths.size) {
            val path = paths[index]
            val paint = paints[index]
            canvas?.drawPath(path, paint)
            signatureCanvas?.drawPath(path, paint)
        }
    }

    /**
     * Clears everything drawn on the view.
     */
    fun clearDrawing() {
        paths.forEach { it.reset() }
        invalidate()
        paths.clear()
        paints.clear()
        initDrawing(strokeColor)
    }

    /**
     * Resets paths, paints and bitmap to initial state.
     */
    private fun initDrawing(strokeColor: StrokeColor) {
        currentPath = Path()
        paths.add(currentPath)
        val paint = when (strokeColor) {
            StrokeColor.BLACK -> blackPaint
            StrokeColor.RED -> redPaint
            StrokeColor.BLUE -> bluePaint
        }
        paints.add(paint)
        signatureBitmap = Bitmap.createBitmap(signatureBitmapWidth, signatureBitmapHeight, Bitmap.Config.ARGB_8888)
        signatureCanvas = Canvas(signatureBitmap)
    }

    /**
     * Defines possible stroke colors with theirs corresponding resources.
     */
    enum class StrokeColor {
        BLACK, RED, BLUE
    }
}
