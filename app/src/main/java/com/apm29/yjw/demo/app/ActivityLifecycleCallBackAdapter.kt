package com.apm29.yjw.demo.app

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.util.Log
import com.apm29.yjw.demo.arch.ViewModelContract
import com.apm29.yjw.demo.di.component.AppComponent


/**
 * Activity生命周期监听器
 */
class ActivityLifecycleCallBackAdapter(private val appComponent: AppComponent) : Application.ActivityLifecycleCallbacks {


    private val TAG = ActivityLifecycleCallBackAdapter::class.java.simpleName
    override fun onActivityPaused(activity: Activity?) {
        Log.d(TAG, activity.toString() + ":onActivityPaused")
    }

    override fun onActivityResumed(activity: Activity?) {
        Log.d(TAG, activity.toString() + ":onActivityResumed")
        ActivityManager.currentActivity = activity
    }

    override fun onActivityStarted(activity: Activity?) {
        Log.d(TAG, activity.toString() + ":onActivityStarted")
    }

    override fun onActivityDestroyed(activity: Activity?) {
        Log.d(TAG, activity.toString() + ":onActivityDestroyed")
        if (ActivityManager.currentActivity === activity) {
            ActivityManager.currentActivity = null
            ActivityManager.activities.remove(activity)
            Log.e(TAG, "$activity removed")
            Log.e(TAG, "${ActivityManager.activities}")

        }
    }

    override fun onActivitySaveInstanceState(activity: Activity?, outState: Bundle?) {
        Log.d(TAG, activity.toString() + ":onActivitySaveInstanceState")
    }

    override fun onActivityStopped(activity: Activity?) {
        Log.d(TAG, activity.toString() + ":onActivityStopped")
    }

    override fun onActivityCreated(activity: Activity?, savedInstanceState: Bundle?) {
        Log.d(TAG, activity.toString() + ":onActivityCreated")
        if (activity is ViewModelContract.IView) {
            activity.buildupComponent(appComponent)
        }
        activity?.let {
            ActivityManager.activities.add(activity)
            Log.e(TAG, "$activity added")
            Log.e(TAG, "${ActivityManager.activities}")

        }
    }
}