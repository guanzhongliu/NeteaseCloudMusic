package com.twt.lgz.neteasecloudmusic.common

import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.support.annotation.Nullable
import android.support.v7.widget.AppCompatImageView
import android.util.AttributeSet
import android.view.animation.LinearInterpolator

class RotateCircleImageView @JvmOverloads constructor(
    context: Context, @Nullable attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr) {
    private var radius: Float = 0.toFloat()
    private val paint: Paint = Paint()
    var state: Int = 0//0停止  1进行　2暂停
    private var objectAnimator: ObjectAnimator = ObjectAnimator.ofFloat(this, "rotation", 0f, 360f)

    init {
        paint.isAntiAlias = true
        init()
    }

    override fun onDraw(canvas: Canvas) {
        val drawable = drawable
        if (drawable == null) {
            super.onDraw(canvas)
            return
        }
        radius = (Math.max(width, height) / 2).toFloat()
        if (drawable is BitmapDrawable) {
            paint.shader = initBitmapShader(drawable)
            canvas.drawCircle(width / 2f, height / 2f, radius, paint)
            return
        }
        super.onDraw(canvas)
    }

    private fun initBitmapShader(drawable: BitmapDrawable): BitmapShader {
        val bitmap = drawable.bitmap
        val scale = Math.max(width.toFloat() / bitmap.width.toFloat(), height.toFloat() / bitmap.height.toFloat())
        val matrix = Matrix()
        matrix.postScale(scale, scale)
        val newBitMap = Bitmap.createBitmap(
            bitmap, 0, 0, bitmap.width,
            bitmap.height, matrix, true
        )
        return BitmapShader(newBitMap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
    }

    private fun init() {
        state = 0
        objectAnimator.duration = 30000
        objectAnimator.interpolator = LinearInterpolator()
        objectAnimator.repeatCount = ObjectAnimator.INFINITE
        objectAnimator.repeatMode = ObjectAnimator.RESTART
    }

    fun pause() {
        if (state != 1)
            return
        objectAnimator.pause()
        state = 2
    }

    fun start() {
        if (state == 1)
            return
        if (state == 0)
            objectAnimator.start()//0停止，需要开始播放
        else
            objectAnimator.resume()//暂停时就是继续播放
        state = 1
    }
}