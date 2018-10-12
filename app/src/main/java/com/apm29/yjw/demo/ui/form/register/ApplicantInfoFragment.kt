package com.apm29.yjw.demo.ui.form.register

import android.os.Bundle
import androidx.lifecycle.Observer
import com.apm29.yjw.demo.arch.BaseFragment
import com.apm29.yjw.demo.di.component.AppComponent
import com.apm29.yjw.demo.di.component.DaggerDefaultFragmentComponent
import com.apm29.yjw.demo.di.module.DefaultFragmentModule
import com.apm29.yjw.demo.model.ApplicantInfo
import com.apm29.yjw.demo.utils.error
import com.apm29.yjw.demo.utils.genderList
import com.apm29.yjw.demo.viewmodel.RegisterFormViewModel
import com.apm29.yjw.gentleloansdemo.R
import com.bigkoo.pickerview.builder.OptionsPickerBuilder
import com.bigkoo.pickerview.listener.OnOptionsSelectListener
import kotlinx.android.synthetic.main.applicant_info_fragment.*


class ApplicantInfoFragment : BaseFragment<RegisterFormViewModel>() {
    override fun setupViewLayout(savedInstanceState: Bundle?): Int {
        return R.layout.applicant_info_fragment
    }

    override fun setupModel(appComponent: AppComponent) {
        DaggerDefaultFragmentComponent.builder()
                .appComponent(appComponent)
                .defaultFragmentModule(DefaultFragmentModule(this))
                .build()
                .inject(this)
    }

    override fun setupViews(savedInstanceState: Bundle?) {
        layoutName.setOnClickListener {
            okName.error()
        }
        //条件选择器
        val pvOptions = OptionsPickerBuilder(requireContext(), OnOptionsSelectListener { options1, _, _, _ ->
            val tx = (genderList[options1])
            tvGender.text = tx
        }).build<String>()
        pvOptions.setPicker(genderList)

        layoutGender.setOnClickListener {
            pvOptions.show()
        }
    }

    override fun initData(savedInstanceState: Bundle?) {
        mViewModel.applicantInfo()
        mViewModel.applicantData.observe(this, Observer {
            setInitValues(it)
        })

    }

    private fun setInitValues(applicantInfo: ApplicantInfo?) {

    }
}