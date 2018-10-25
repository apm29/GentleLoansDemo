package com.apm29.yjw.demo.third

import android.graphics.*
import android.graphics.drawable.Drawable

class TextDrawable(private val mText:String, private val mTextColor:Int, private val mTextSize:Float = 32f): Drawable() {

    private val paint:Paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        this.textSize = mTextSize
        this.color = mTextColor
    }

    private val textWidth = paint.measureText(mText)



    override fun draw(canvas: Canvas) {
        canvas.drawText(mText,(bounds.left+bounds.right-textWidth)/2f,(bounds.top+bounds.bottom)/2f,paint)
    }

    override fun setAlpha(alpha: Int) {
        paint.alpha = alpha
    }

    override fun getOpacity(): Int {
        return PixelFormat.OPAQUE
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
        paint.colorFilter = colorFilter
    }

}