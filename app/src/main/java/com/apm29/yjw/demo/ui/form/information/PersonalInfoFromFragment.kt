package com.apm29.yjw.demo.ui.form.information

import android.os.Bundle
import androidx.lifecycle.Observer
import com.apm29.yjw.demo.arch.BaseFragment
import com.apm29.yjw.demo.di.component.AppComponent
import com.apm29.yjw.demo.di.component.DaggerDefaultFragmentComponent
import com.apm29.yjw.demo.di.module.DefaultFragmentModule
import com.apm29.yjw.demo.utils.setupLocationPicker
import com.apm29.yjw.demo.utils.showToast
import com.apm29.yjw.demo.viewmodel.InformationFormViewModel
import com.apm29.yjw.gentleloansdemo.R
import com.bigkoo.pickerview.builder.OptionsPickerBuilder
import com.contrarywind.interfaces.IPickerViewData
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
        pickerAddress.setupLocationPicker (defaultSelect = Triple(10,0,0)){ triple, code, str, view ->
            showToast(code)
        }
        pickerRegResident.setupLocationPicker(defaultSelect = Triple(10,0,0)) { triple, code, str, view ->
            showToast(code)
        }
    }

    override fun initData(savedInstanceState: Bundle?) {

    }
}