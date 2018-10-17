package com.apm29.yjw.demo.app

import android.annotation.SuppressLint
import android.app.Activity
import com.apm29.yjw.demo.ui.HostActivity

@SuppressLint("StaticFieldLeak")
object ActivityManager {
    var activities:HashSet<Activity> = hashSetOf()
    var currentActivity: Activity? = null

    fun findHostActivity(): HostActivity? {
        activities.forEach {
            if (it is HostActivity){
                return it
            }
        }
        return null
    }

    fun containsActivity(clazz:Class<out Activity>): Boolean {
        activities.forEach {
            if (it.javaClass == clazz){
                return true
            }
        }
        return false
    }

    fun getHostActivity(): HostActivity?{
        activities.forEach {
            if (it is HostActivity){
                return it
            }
        }
        return  null
    }
}