package com.apm29.yjw.demo.ui.widget

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.widget.TextView

class IconFontTextView(context: Context,attributes: AttributeSet?,defStyle:Int): TextView(context,attributes,defStyle) {
    init {
        typeface = Typeface.createFromAsset(context.assets,"fonts/iconfont.ttf")
    }

    constructor(context: Context,attributes: AttributeSet):this(context,attributes,0)

    constructor(context: Context):this(context,null,0)
}