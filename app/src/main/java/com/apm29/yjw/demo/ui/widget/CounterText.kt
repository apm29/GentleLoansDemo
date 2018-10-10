package com.apm29.yjw.demo.ui.widget

import android.annotation.SuppressLint
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.widget.TextView
import kotlin.concurrent.fixedRateTimer

class CounterText(context: Context, attributes: AttributeSet?, defStyle: Int) : TextView(context, attributes, defStyle) {

    constructor(context: Context, attributes: AttributeSet) : this(context, attributes, 0)

    constructor(context: Context) : this(context, null, 0)

    /**
     * count time :TimeUnit:Second
     */
    @SuppressLint("SetTextI18n")
    fun count(count: Long) {
        this.isEnabled = false
        time = count
        previousText = text.toString()
        fixedRateTimer(
                name = "CountTimer", initialDelay = 0, daemon = true, period = 1000
        ) {
            mHandler.post {
                text = "($time)Sec"
                time -= 1
                if (time <= 0) {
                    text = previousText
                    isEnabled = true
                    this.cancel()
                }
                invalidate()
            }
        }
    }

    private val mHandler: Handler = Handler(Looper.getMainLooper())
    private var previousText: String = ""
    var time: Long = 0L
}