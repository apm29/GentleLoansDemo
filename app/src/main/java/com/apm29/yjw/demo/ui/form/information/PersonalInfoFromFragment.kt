package com.apm29.yjw.demo.ui.form.information

import android.os.Bundle
import androidx.lifecycle.Observer
import com.apm29.yjw.demo.arch.BaseFragment
import com.apm29.yjw.demo.di.component.AppComponent
import com.apm29.yjw.demo.di.component.DaggerDefaultFragmentComponent
import com.apm29.yjw.demo.di.module.DefaultFragmentModule
import com.apm29.yjw.demo.utils.setupLocationPickerLocal
import com.apm29.yjw.demo.utils.setupLocationWithData
import com.apm29.yjw.demo.utils.showToast
import com.apm29.yjw.demo.viewmodel.InformationFormViewModel
import com.apm29.yjw.gentleloansdemo.R
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


    private fun setupPickerWithLocalData() {
        pickerAddress.setupLocationPickerLocal {
            _, code, _, _ ->
            showToast(code)
        }
        pickerRegResident.setupLocationPickerLocal {
            _, code, _, _ ->
            showToast(code)
        }
    }

    override fun initData(savedInstanceState: Bundle?) {
        mViewModel.loadLocationData{
            setupPickerWithLocalData()
        }
        mViewModel.locationPCACodeData.observe(this, Observer {
            pickerAddress.setupLocationWithData(it){
                _, code, _, _ ->
                showToast(code)
            }
            pickerRegResident.setupLocationWithData(it){
                _, code, _, _ ->
                showToast(code)
            }
        })
    }
}