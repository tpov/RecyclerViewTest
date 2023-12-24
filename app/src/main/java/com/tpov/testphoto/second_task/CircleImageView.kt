package com.tpov.testphoto.second_task

import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.VectorDrawable
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView

class CircleImageView : AppCompatImageView {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun onDraw(canvas: Canvas) {
        val drawable = drawable ?: return

        val bitmap = when (drawable) {
            is BitmapDrawable -> drawable.bitmap
            is VectorDrawable -> {
                Bitmap.createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight, Bitmap.Config.ARGB_8888).apply {
                    val canvas = Canvas(this)
                    drawable.setBounds(0, 0, canvas.width, canvas.height)
                    drawable.draw(canvas)
                }
            }
            else -> return
        }

        val scaledBitmap = Bitmap.createScaledBitmap(bitmap, width, height, false)
        val shader = BitmapShader(scaledBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)

        val paint = Paint().apply {
            isAntiAlias = true
            setShader(shader)
        }

        val radius = width / 2f
        canvas.drawCircle(radius, radius, radius, paint)
    }
}
