package com.apm29.yjw.demo.ui.form.information

import android.os.Bundle
import androidx.lifecycle.Observer
import com.apm29.yjw.demo.arch.BaseFragment
import com.apm29.yjw.demo.di.component.AppComponent
import com.apm29.yjw.demo.di.component.DaggerDefaultFragmentComponent
import com.apm29.yjw.demo.di.module.DefaultFragmentModule
import com.apm29.yjw.demo.utils.showToast
import com.apm29.yjw.demo.viewmodel.InformationFormViewModel
import com.apm29.yjw.gentleloansdemo.R
import com.bigkoo.pickerview.builder.OptionsPickerBuilder
import kotlinx.android.synthetic.main.personal_info_form_fragment.*

class PersonalInfoFromFragment:BaseFragment<InformationFormViewModel>() {
    override fun setupViewLayout(savedInstanceState: Bundle?): Int {
        return R.layout.personal_info_form_fragment
    }

    override fun setupModel(appComponent: AppComponent) {
        DaggerDefaultFragmentComponent.builder()
                .appComponent(appComponent)
                .defaultFragmentModule(DefaultFragmentModule(this))
                .build()
                .inject(this)
    }

    override fun setupViews(savedInstanceState: Bundle?) {

    }

    override fun initData(savedInstanceState: Bundle?) {
        mViewModel.loadLocationData()

        mViewModel.locationData.observe(this, Observer {
            val (component1, component2, component3) = it
            val pickerOptions = OptionsPickerBuilder(requireContext()){
                p1,p2,p3,_->
                showToast("${component1[p1]} - ${component2[p1][p2]} -  ${component3[p1][p2][p3]}")
            }
            val build = pickerOptions.build<String>()
            build.setPicker(component1,component2,component3)
            //户口地址
            pickerRegResident.setOnClickListener {
                _->
                        build.show()
            }

        })
    }
}