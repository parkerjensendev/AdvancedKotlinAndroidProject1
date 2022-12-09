package com.example.android.advancedandroidkotlinpart1

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.core.content.withStyledAttributes

class SickCustomButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
    }

    private var waitingText = ""
    private var actionText = ""
    private var circleColor = Color.WHITE
    private var rectangle: RectF? = null
    var arcProportion: Float = 0f
    var isWaiting = true

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        textAlign = Paint.Align.CENTER
        textSize = 55.0f
        typeface = Typeface.create( "", Typeface.BOLD)
    }

    init {
        isClickable = true
        setBackgroundColor(Color.DKGRAY)
        textAlignment = TEXT_ALIGNMENT_CENTER
        context.withStyledAttributes(attrs, R.styleable.SickCustomButton) {
            waitingText = getString(R.styleable.SickCustomButton_waitingText).toString()
            actionText = getString(R.styleable.SickCustomButton_actionText).toString()
            circleColor = getColor(R.styleable.SickCustomButton_circleColor, Color.WHITE)
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        paint.color = Color.DKGRAY
        canvas.drawRect(left.toFloat(), top.toFloat(), right.toFloat(), bottom.toFloat(), paint)

        paint.color = Color.WHITE
        if(isWaiting){
            canvas.drawText(waitingText, (width/2).toFloat(), (height/1.75).toFloat(), paint)
        } else {
            canvas.drawText(actionText, (width/2).toFloat(), (height/1.75).toFloat(), paint)

            paint.color = circleColor
            if (rectangle == null) {
                rectangle = RectF((width * 3/4).toFloat(), (height / 4).toFloat(), (width * 3/4 + height / 2).toFloat(),  (height / 4 + height / 2).toFloat())
            }
            canvas.drawArc(rectangle!!, -90f, arcProportion * 360, false, paint)
        }

    }

    override fun performClick(): Boolean {
        if(super.performClick()) return true

        invalidate()
        return true
    }
}