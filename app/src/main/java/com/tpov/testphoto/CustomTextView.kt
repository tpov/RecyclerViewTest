package com.tpov.testphoto

import android.R
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.ViewTreeObserver

class CustomTextView : androidx.appcompat.widget.AppCompatTextView {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    private var customText: String? = null
    var maxLineLength = 0

    init {
        viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                if (width > 0 && height > 0) {
                    viewTreeObserver.removeOnGlobalLayoutListener(this)
                    customText?.let { setText(it) }
                }
            }
        })
    }

    override fun draw(canvas: Canvas) {
        val lineCount = layout.lineCount
        val rect = Rect()
        val paint = Paint()
        paint.color = resources.getColor(R.color.white)
        for (i in 0 until lineCount) {
            rect.top = layout.getLineTop(i)
            rect.left = layout.getLineLeft(i).toInt()
            rect.right = layout.getLineRight(i).toInt()
            rect.bottom =
                (layout.getLineBottom(i) - (if (i + 1 == lineCount) 0 else layout.spacingAdd).toInt())
            canvas.drawRect(rect, paint)
        }
        super.draw(canvas)
    }

    fun setText(text: String) {
        if (width == 0 && height == 0) {
            customText = text
            return
        }

        if (text.length > MAX_CHAR_COUNT || text.contains("\n")) {
            throw IllegalArgumentException("Text must be less than $MAX_CHAR_COUNT characters and contain no line breaks.")
        }

        val availableWidth = width - paddingLeft - paddingRight
        maxLineLength = availableWidth
        val paint = paint

        val words = customText!!.split(" ")
        val lines = mutableListOf<String>()
        var currentLine = StringBuilder()
        var currentWidth = 0f

        for (word in words) {
            val wordWidth = paint.measureText("$word ")
            if (currentWidth + wordWidth <= maxLineLength) {
                currentLine.append("$word ")
                currentWidth += wordWidth
            } else {
                lines.add(currentLine.toString().trim())
                currentLine = StringBuilder("$word ")
                currentWidth = wordWidth
                maxLineLength = ( maxLineLength * LINE_LENGTH_SCALING_FACTOR).toInt()
            }
        }

        if (currentLine.isNotEmpty()) {
            lines.add(currentLine.toString().trim())
        }

        val newText = lines.joinToString("\n")
        super.setText(newText)
    }


}