package com.apm29.yjw.demo.ui.verify

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.apm29.yjw.demo.arch.BaseFragment
import com.apm29.yjw.demo.di.component.AppComponent
import com.apm29.yjw.demo.di.component.DaggerDefaultFragmentComponent
import com.apm29.yjw.demo.di.module.DefaultFragmentModule
import com.apm29.yjw.demo.utils.Verify
import com.apm29.yjw.demo.utils.getTextOrEmpty
import com.apm29.yjw.demo.utils.showToast
import com.apm29.yjw.demo.viewmodel.CommunicateViewModel
import com.apm29.yjw.demo.viewmodel.RealNameVerifyViewModel
import com.apm29.yjw.gentleloansdemo.BuildConfig
import com.apm29.yjw.gentleloansdemo.R
import kotlinx.android.synthetic.main.real_name_verify_fragment.*

class RealNameVerifyFragment : BaseFragment<RealNameVerifyViewModel>() {

    override var observingError: Boolean = true
    override fun setupViewLayout(savedInstanceState: Bundle?): Int {
        return R.layout.real_name_verify_fragment
    }

    override fun setupModel(appComponent: AppComponent) {
        DaggerDefaultFragmentComponent.builder()
                .defaultFragmentModule(DefaultFragmentModule(this))
                .appComponent(appComponent)
                .build()
                .inject(this)
    }

    override fun setupViews(savedInstanceState: Bundle?) {

        bankSpinnerSelector.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override
            fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (view is TextView) {
                    val bankName = view.getTextOrEmpty()
                    bankCode.forEach {
                        if (it.value == bankName) {
                            code = it.key
                        }
                    }
                }
            }

        }
        tvSend.setOnClickListener { _ ->
            verifyInput().apply {
                if (!success) {
                    showToast(error)
                    return@setOnClickListener
                }
            }
            mViewModel.sendBankSms(
                    etCard.getTextOrEmpty(),
                    etId.getTextOrEmpty(),
                    etName.getTextOrEmpty(),
                    etMobile.getTextOrEmpty(),
                    code
            )
        }

        btnSubmit.setOnClickListener {
            Verify.OK().verifyText(etSms).apply {
                if (!success) {
                    showToast(error)
                    return@setOnClickListener
                }
            }
            mViewModel.bindCard(etSms.getTextOrEmpty())
        }

    }

    private fun verifyInput(): Verify {
        return Verify.OK()
                .verifyText(etCard)
                .verifyText(etId)
                .verifyText(etName)
                .verifyText(etMobile)
    }

    lateinit var bankCode: Map<String, String>
    var code: String = ""
    override fun initData(savedInstanceState: Bundle?) {
        super.initData(savedInstanceState)
        mViewModel.bankCode()
        mViewModel.profileVerify()
        mViewModel.bankCodes.observe(this, Observer {
            bankCode = it.peekData()
            val arrays = it.peekData().values.toList()
            val arrayAdapter = ArrayAdapter<String>(requireContext(),
                    android.R.layout.simple_selectable_list_item, arrays)
            bankSpinnerSelector.adapter = arrayAdapter
        })


        mViewModel.smsResult.observe(this, Observer {
            if (it.success()) {
                tvSend.count(60)
                if (BuildConfig.DEBUG) {
                    etSms.setText("000000")
                }
            }
            mViewModel.mErrorData.value = it.msg
        })
        mViewModel.bindResult.observe(this, Observer {
            if (it.success()) {
                tvSend.count(60)
                mViewModel.profileVerify()
            }
            mViewModel.mErrorData.value = it.msg
        })

        mViewModel.profile.observe(this, Observer {
            val isReal = it.success() && it.peekData().is_real
            cardStatus.visibility = if (isReal) View.VISIBLE else View.GONE
            cardForm.visibility = if (!isReal) View.VISIBLE else View.GONE

            textHint.text = if (isReal) getString(R.string.hint_real_auth_pass) else getString(R.string.hint_real_auth)
            btnVerify.text = if (isReal) "完成绑定" else "实名绑定"
            ivWarning.setAnimation(if (isReal) R.raw.success else R.raw.warning)
            ivWarning.playAnimation()
            btnVerify.setOnClickListener { _ ->
                if (isReal) {
                    val viewModel = ViewModelProviders.of(requireActivity()).get(CommunicateViewModel::class.java)
                    viewModel.realAuthSuccess()
                } else {
                    cardForm.visibility = View.VISIBLE
                    cardStatus.visibility = View.GONE
                }
            }
        })

    }
}