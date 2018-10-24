package com.apm29.yjw.demo.ui.form.information

import android.os.Bundle
import androidx.navigation.findNavController
import com.apm29.yjw.demo.app.ActivityManager
import com.apm29.yjw.demo.arch.BaseFragment
import com.apm29.yjw.demo.di.component.AppComponent
import com.apm29.yjw.demo.di.component.DaggerDefaultFragmentComponent
import com.apm29.yjw.demo.di.module.DefaultFragmentModule
import com.apm29.yjw.demo.utils.navigateErrorHandled
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
            navigateErrorHandled(R.id.personalInfoFromFragment)
        }
        layoutContactsInfo.setOnClickListener {
            navigateErrorHandled(R.id.contactsInfoFormFragment)
        }
        layoutLoanUsageInfo.setOnClickListener {
            navigateErrorHandled(R.id.loanUsageInfoFromFragment)
        }
        layoutAssetsInfo.setOnClickListener {
            navigateErrorHandled(R.id.assetsInfoFromFragment)
        }
        layoutJobInfo.setOnClickListener {
            navigateErrorHandled(R.id.jobInfoFromFragment)
        }
        layoutPhotoInfo.setOnClickListener {
            navigateErrorHandled(R.id.imageInfoFromFragment)
        }
        btnSubmit.setOnClickListener {
            showToast("submit")
        }
    }
}