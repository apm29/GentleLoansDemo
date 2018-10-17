package com.apm29.yjw.demo.ui.form.information

import android.os.Bundle
import androidx.navigation.findNavController
import com.apm29.yjw.demo.app.ActivityManager
import com.apm29.yjw.demo.arch.BaseFragment
import com.apm29.yjw.demo.di.component.AppComponent
import com.apm29.yjw.demo.di.component.DaggerDefaultFragmentComponent
import com.apm29.yjw.demo.di.module.DefaultFragmentModule
import com.apm29.yjw.demo.utils.showToast
import com.apm29.yjw.demo.viewmodel.InformationFormViewModel
import com.apm29.yjw.gentleloansdemo.R
import kotlinx.android.synthetic.main.applicant_info_manager_fragment.*

class ApplicantInfoManagerFragment : BaseFragment<InformationFormViewModel>() {
    override fun setupViewLayout(savedInstanceState: Bundle?): Int {
        return R.layout.applicant_info_manager_fragment
    }

    override fun setupModel(appComponent: AppComponent) {
        DaggerDefaultFragmentComponent.builder()
                .appComponent(appComponent)
                .defaultFragmentModule(DefaultFragmentModule(this))
                .build()
                .inject(this)
    }

    override fun setupViews(savedInstanceState: Bundle?) {
        val navController = ActivityManager.findHostActivity()?.findNavController(R.id.app_host_fragment)
        layoutPersaonalInfo.setOnClickListener {
            navController?.navigate(R.id.action_applicantInfoManagerFragment_to_personalInfoFromFragment)
        }
        layoutContactsInfo.setOnClickListener {
            navController?.navigate(R.id.action_applicantInfoManagerFragment_to_contactsInfoFormFragment)
        }
        layoutLoanUsageInfo.setOnClickListener {
            navController?.navigate(R.id.action_applicantInfoManagerFragment_to_loanUsageInfoFromFragment)
        }
        layoutAssetsInfo.setOnClickListener {
            navController?.navigate(R.id.action_applicantInfoManagerFragment_to_assetsInfoFromFragment)
        }
        layoutJobInfo.setOnClickListener {
            navController?.navigate(R.id.action_applicantInfoManagerFragment_to_jobInfoFromFragment)
        }
        layoutPhotoInfo.setOnClickListener {
            navController?.navigate(R.id.action_applicantInfoManagerFragment_to_imageInfoFromFragment)
        }
        btnSubmit.setOnClickListener {
            showToast("submit")
        }
    }
}