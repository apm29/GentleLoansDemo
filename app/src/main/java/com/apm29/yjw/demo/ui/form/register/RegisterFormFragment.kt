package com.apm29.yjw.demo.ui.form.register

import android.os.Bundle
import androidx.navigation.fragment.findNavController
import com.apm29.yjw.demo.arch.BaseFragment
import com.apm29.yjw.demo.di.component.AppComponent
import com.apm29.yjw.demo.di.component.DaggerDefaultFragmentComponent
import com.apm29.yjw.demo.di.module.DefaultFragmentModule
import com.apm29.yjw.demo.utils.showToast
import com.apm29.yjw.demo.viewmodel.DefaultFragmentViewModel
import com.apm29.yjw.gentleloansdemo.R
import kotlinx.android.synthetic.main.register_form_fragment.*


/*
 * Defines an array that contains column names to move from
 * the Cursor to the ListView.
 */
class RegisterFormFragment : BaseFragment<DefaultFragmentViewModel>(){
    override fun setupViewLayout(savedInstanceState: Bundle?): Int {
        return R.layout.register_form_fragment
    }


    override fun setupModel(appComponent: AppComponent) {
        DaggerDefaultFragmentComponent.builder()
                .defaultFragmentModule(DefaultFragmentModule(this))
                .appComponent(appComponent)
                .build()
                .inject(this)
    }

    override fun setupViews(savedInstanceState: Bundle?) {
        layoutApplicantInfo.setOnClickListener {
            showToast("info")
            tvApplicantInfo.isEnabled = false
            findNavController().navigate(R.id.action_registerFormFragment_to_applicantInfoFragment)
        }

        layoutAssetsInfo.setOnClickListener {
            showToast("assets")
        }
    }

    override fun initData(savedInstanceState: Bundle?) {

    }
}