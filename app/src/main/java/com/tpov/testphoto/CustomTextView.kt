package com.tpov.testphoto

import android.R
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.text.Html
import android.util.AttributeSet
import android.util.Log
import android.view.ViewTreeObserver
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

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

    override fun onDraw(canvas: Canvas) {
        val lineCount = layout.lineCount
        val rect = Rect()
        val paint = Paint()
        paint.color = resources.getColor(R.color.white)

        for (i in 0 until lineCount) {
            rect.top = if (i == 0) layout.getLineTop(i) + 3 else layout.getLineTop(i) - 1
            rect.left = layout.getLineLeft(i).toInt() - PADDING_OF_THE_HIGHLIGHT
            rect.right = layout.getLineRight(i).toInt() + PADDING_OF_THE_HIGHLIGHT * 2 // 2 - this is left padding compensation of 6px
            rect.bottom = (layout.getLineBottom(i) - (if (i + 1 == lineCount) 1 else layout.spacingAdd).toInt())

            canvas.drawRect(rect, paint)
        }
        super.onDraw(canvas)
    }

    fun setText(text: String) {
        val textUpper = text.uppercase()
        if (width == 0 && height == 0) {
            customText = textUpper
            return
        }

        val availableWidth = width - paddingLeft - paddingRight - PADDING_OF_THE_HIGHLIGHT * 2
        val paint = paint

        maxLineLength = availableWidth
        customText = if (textUpper.length > MAX_CHAR_COUNT) textUpper.substring(0, MAX_CHAR_COUNT) else textUpper

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
                maxLineLength = (currentWidth * LINE_LENGTH_SCALING_FACTOR).toInt()
                currentWidth = wordWidth
            }
        }

        if (currentLine.isNotEmpty()) {
            lines.add(currentLine.toString().trim())
        }

        val newText = lines.joinToString("<br>")
        val htmlText = Html.fromHtml(newText, Html.FROM_HTML_MODE_LEGACY)
        super.setText(htmlText)
    }
}