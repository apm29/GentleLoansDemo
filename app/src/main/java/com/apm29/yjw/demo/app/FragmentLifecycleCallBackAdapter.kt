package com.apm29.yjw.demo.app

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.apm29.yjw.demo.arch.ViewModelContract
import com.apm29.yjw.demo.di.component.AppComponent


/**
 * Fragment生命周期监听器
 */
class FragmentLifecycleCallBackAdapter(private val appComponent: AppComponent): FragmentManager.FragmentLifecycleCallbacks() {



    private val TAG = FragmentLifecycleCallBackAdapter::class.java.simpleName

    override fun onFragmentViewCreated(fm: FragmentManager, f: Fragment, v: View, savedInstanceState: Bundle?) {
        //println("FragmentLifecycleCallBackAdapter.onFragmentViewCreated  ${f::class.java.simpleName}")
        super.onFragmentViewCreated(fm, f, v, savedInstanceState)
    }

    override fun onFragmentCreated(fm: FragmentManager, f: Fragment, savedInstanceState: Bundle?) {
        println("FragmentLifecycleCallBackAdapter.onFragmentCreated   ${f::class.java.simpleName}")
        super.onFragmentCreated(fm, f, savedInstanceState)
    }

    override fun onFragmentAttached(fm: FragmentManager, f: Fragment, context: Context) {
        //println("FragmentLifecycleCallBackAdapter.onFragmentAttached  ${f::class.java.simpleName}")
        super.onFragmentAttached(fm, f, context)
        if (f is ViewModelContract.IView){
            f.buildupComponent(appComponent)
        }
    }

    override fun onFragmentViewDestroyed(fm: FragmentManager, f: Fragment) {
        //println("FragmentLifecycleCallBackAdapter.onFragmentViewDestroyed  ${f::class.java.simpleName}")
        super.onFragmentViewDestroyed(fm, f)
    }

    override fun onFragmentDetached(fm: FragmentManager, f: Fragment) {
        //println("FragmentLifecycleCallBackAdapter.onFragmentDetached  ${f::class.java.simpleName}")
        super.onFragmentDetached(fm, f)
    }

    override fun onFragmentActivityCreated(fm: FragmentManager, f: Fragment, savedInstanceState: Bundle?) {
        //println("FragmentLifecycleCallBackAdapter.onFragmentActivityCreated   ${f::class.java.simpleName}")
        super.onFragmentActivityCreated(fm, f, savedInstanceState)
    }

    override fun onFragmentStopped(fm: FragmentManager, f: Fragment) {
        //println("FragmentLifecycleCallBackAdapter.onFragmentStopped  ${f::class.java.simpleName}")
        super.onFragmentStopped(fm, f)
    }

    override fun onFragmentResumed(fm: FragmentManager, f: Fragment) {
        //println("FragmentLifecycleCallBackAdapter.onFragmentResumed  ${f::class.java.simpleName}")
        super.onFragmentResumed(fm, f)
    }

    override fun onFragmentPreAttached(fm: FragmentManager, f: Fragment, context: Context) {
        //println("FragmentLifecycleCallBackAdapter.onFragmentPreAttached  ${f::class.java.simpleName}")
        super.onFragmentPreAttached(fm, f, context)
    }

    override fun onFragmentDestroyed(fm: FragmentManager, f: Fragment) {
        //println("FragmentLifecycleCallBackAdapter.onFragmentDestroyed  ${f::class.java.simpleName}")
        super.onFragmentDestroyed(fm, f)
    }

    override fun onFragmentSaveInstanceState(fm: FragmentManager, f: Fragment, outState: Bundle) {
        //println("FragmentLifecycleCallBackAdapter.onFragmentSaveInstanceState  ${f::class.java.simpleName}")
        super.onFragmentSaveInstanceState(fm, f, outState)
    }

    override fun onFragmentStarted(fm: FragmentManager, f: Fragment) {
        //println("FragmentLifecycleCallBackAdapter.onFragmentStarted  ${f::class.java.simpleName}")
        super.onFragmentStarted(fm, f)
    }

    override fun onFragmentPreCreated(fm: FragmentManager, f: Fragment, savedInstanceState: Bundle?) {
        //println("FragmentLifecycleCallBackAdapter.onFragmentPreCreated   ${f::class.java.simpleName}")
        super.onFragmentPreCreated(fm, f, savedInstanceState)
    }

    override fun onFragmentPaused(fm: FragmentManager, f: Fragment) {
        //println("FragmentLifecycleCallBackAdapter.onFragmentPaused  ${f::class.java.simpleName}")
        super.onFragmentPaused(fm, f)
    }
}