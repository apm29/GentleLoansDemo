package com.apm29.yjw.demo.ui.form.register

import android.os.Bundle
import androidx.lifecycle.Observer
import com.apm29.yjw.demo.arch.BaseFragment
import com.apm29.yjw.demo.di.component.AppComponent
import com.apm29.yjw.demo.di.component.DaggerDefaultFragmentComponent
import com.apm29.yjw.demo.di.module.DefaultFragmentModule
import com.apm29.yjw.demo.model.ApplicantInfo
import com.apm29.yjw.demo.model.PersonalInfo
import com.apm29.yjw.demo.utils.*
import com.apm29.yjw.demo.viewmodel.RegisterFormViewModel
import com.apm29.yjw.gentleloansdemo.R
import kotlinx.android.synthetic.main.applicant_info_fragment_constraint.*


class ApplicantInfoFragment : BaseFragment<RegisterFormViewModel>() {
    override fun setupViewLayout(savedInstanceState: Bundle?): Int {
        return R.layout.applicant_info_fragment_constraint
    }

    override fun setupModel(appComponent: AppComponent) {
        DaggerDefaultFragmentComponent.builder()
                .appComponent(appComponent)
                .defaultFragmentModule(DefaultFragmentModule(this))
                .build()
                .inject(this)
    }

    override fun setupViews(savedInstanceState: Bundle?) {
        //条件选择器
        pickerGender.setupOneOptPicker(genderList)
        pickerMarital.setupOneOptPicker(maritalList)
        pickerStaff.setupOneOptPicker(staffList)
        pickerPayType.setupOneOptPicker(payTypeList)

        btnSave.setOnClickListener {
            val (error, success) = verifyInput()
            if (!success) {
                showToast(error)
                return@setOnClickListener
            }
            val personalInfo = PersonalInfo(
                    etName.getTextOrEmpty(),
                    etIdCard.getTextOrEmpty(),
                    pickerGender.getIndexByText(genderList),
                    pickerMarital.getIndexByText(maritalList),
                    etCompany.getTextOrEmpty(),
                    etDepartment.getTextOrEmpty(),
                    etLevel.getTextOrEmpty(),
                    pickerStaff.getIndexByText(staffList),
                    etYearIncome.getTextOrEmpty(),
                    etGjjMonth.getTextOrEmpty(),
                    pickerPayType.getIndexByText(payTypeList),
                    etTerm.getTextOrEmpty(),
                    etZxAccount.getTextOrEmpty(),
                    etZxPass.getTextOrEmpty(),
                    etZxVerify.getTextOrEmpty(),
                    etGjjAccount.getTextOrEmpty(),
                    etGjjPass.getTextOrEmpty(),
                    etZwAccount.getTextOrEmpty(),
                    etZwPass.getTextOrEmpty(),
                    etAgent.getTextOrEmpty()
            )
            mViewModel.postApplicantInfo(personalInfo)
        }
    }

    override fun initData(savedInstanceState: Bundle?) {
        mViewModel.applicantInfo()
        mViewModel.applicantData.observe(this, Observer {
            setInitValues(it)
        })

    }

    private fun verifyInput(): Verify {
        return Verify.ok()
                .verifyText(etName)
                .verifyText(etIdCard)
                .verifyPicker(pickerGender)
                .verifyPicker(pickerMarital)
                .verifyEdit(etCompany)
                .verifyEdit(etDepartment)
                .verifyEdit(etLevel)
                .verifyPicker(pickerStaff)
                .verifyEdit(etYearIncome)
                .verifyEdit(etGjjMonth)
                .verifyPicker(pickerPayType)
                .verifyEdit(etTerm)
                .verifyEdit(etZxAccount)
                .verifyEdit(etZxPass)
                .verifyEdit(etZxVerify)
                .verifyEdit(etGjjAccount)
                .verifyEdit(etGjjPass)
                .verifyEdit(etZwAccount)
                .verifyEdit(etZxPass)
                .verifyEdit(etAgent)
    }

    private fun setInitValues(applicantInfo: ApplicantInfo?) {
        applicantInfo?.let {
            etName.text = it.name
            etIdCard.text = it.idCard
            pickerGender.setTextByIndex(it.gender, genderList)
            pickerMarital.setTextByIndex(it.maritalStatus, maritalList)

            etCompany.setText(it.company)
            etDepartment.setText(it.department)
            etLevel.setText(it.level)

            pickerStaff.setTextByIndex(it.staff, staffList)

            etYearIncome.setText(it.yearIncome)
            etGjjMonth.setText(it.gjjMonth)

            pickerPayType.setTextByIndex(it.payType, payTypeList)

            etTerm.setText(it.term)
            etZxAccount.setText(it.zxAccount)
            etZxPass.setText(it.zxPass)
            etZxVerify.setText(it.zxVerify)

            etGjjAccount.setText(it.gjjAccount)
            etGjjPass.setText(it.gjjPass)

            etZwAccount.setText(it.zwAccount)
            etZwPass.setText(it.zxPass)
        }
    }
}