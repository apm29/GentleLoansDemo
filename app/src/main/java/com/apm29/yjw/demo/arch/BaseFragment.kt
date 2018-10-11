package com.apm29.yjw.demo.arch

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.view.animation.BounceInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.transition.AutoTransition
import com.apm29.yjw.demo.di.component.AppComponent
import com.apm29.yjw.demo.ui.dialog.WarningDialog
import com.apm29.yjw.gentleloansdemo.R
import kotlinx.android.synthetic.main.host_activity.*
import javax.inject.Inject

abstract class BaseFragment<VM : ViewModelContract.IViewModel> : Fragment(), ViewModelContract.IView {

    val tagFragment: String
        get() = this::class.java.simpleName

    open var observingError: Boolean = false
    open var showToolBar: Boolean = true

    @Inject
    lateinit var mViewModel: VM

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(setupViewLayout(savedInstanceState), container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (showToolBar) {
            val activity = requireActivity()
            if (activity is AppCompatActivity) {
                val toolBar = view.findViewById<Toolbar>(R.id.toolBar)
                if (toolBar!=null) {
                    activity.setSupportActionBar(toolBar)
                    activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
                    NavigationUI.setupWithNavController(toolBar,findNavController())
                }
            }
        }
        setupViews(savedInstanceState)
    }

    fun hideToolBarArrow(){
        val activity = requireActivity()
        if(activity is AppCompatActivity){
            activity.supportActionBar?.setDisplayHomeAsUpEnabled(false)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (observingError) {
            mViewModel.mErrorData.observe(this, Observer {
                WarningDialog.getInstance(getString(R.string.app_name), it)
                        .show(requireFragmentManager())
            })
        }
        setupShareElementTransition()
        initData(savedInstanceState)
    }

    /**
     * called just after onCreate
     * 1. liveData should be observed here
     * 2. network init request should be placed here for the sake of avoiding being the request called twice while returning to this fragment
     */
    protected open fun initData(savedInstanceState: Bundle?) {

    }

    private fun setupShareElementTransition() {
        sharedElementReturnTransition = AutoTransition().also {
            it.duration = 800
            it.interpolator = BounceInterpolator()
        }
        sharedElementEnterTransition = AutoTransition().also {
            it.duration = 800
            it.interpolator = BounceInterpolator()
        }
    }

    /**
     * called on fragment created
     */
    override fun buildupComponent(appComponent: AppComponent) {
        setupModel(appComponent)
        if (!::mViewModel.isInitialized) {
            throw IllegalAccessException("mViewModel hasn't been  initialized")
        }
        mViewModel.buildupComponent(appComponent)
    }

    override fun showLoading() {
        WarningDialog.getInstance(getString(R.string.app_name), getString(R.string.app_loading_text))
                .show(requireFragmentManager())
    }

    override fun hideLoading() {
        WarningDialog.getInstance()?.dismiss()
    }
}