package com.apm29.yjw.demo.ui.loan

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.transition.Explode
import androidx.transition.Slide
import com.apm29.yjw.demo.arch.BaseFragment
import com.apm29.yjw.demo.di.component.AppComponent
import com.apm29.yjw.demo.di.component.DaggerDefaultFragmentComponent
import com.apm29.yjw.demo.di.module.DefaultFragmentModule
import com.apm29.yjw.demo.model.LoanLog
import com.apm29.yjw.demo.viewmodel.LoanDetailViewModel
import com.apm29.yjw.gentleloansdemo.R
import kotlinx.android.synthetic.main.loan_detail_fragment.*
import kotlinx.android.synthetic.main.loan_detail_header_layout.*
import java.lang.IllegalArgumentException

class LoanDetailFragment : BaseFragment<LoanDetailViewModel>() {
    override fun setupViewLayout(savedInstanceState: Bundle?): Int {
        return R.layout.loan_detail_fragment
    }

    override fun setupModel(appComponent: AppComponent) {
        DaggerDefaultFragmentComponent.builder()
                .appComponent(appComponent)
                .defaultFragmentModule(DefaultFragmentModule(this))
                .build()
                .inject(this)
    }

    private val mLoanLog: LoanLog? by lazy {
        LoanDetailFragmentArgs.fromBundle(arguments)
                .loanLog
    }


    override fun setupViews(savedInstanceState: Bundle?) {
        mLoanLog?.let {
            textTotal.text = it.restAmount?.toString()
            tvDetail.text = getString(R.string.text_loan_detail, it.totalInterest, it.totalOverdue, it.totalFine)
            tvBankHint.text = getString(R.string.text_bank_hint, it.bankName)
        }

        viewPager.adapter = object : FragmentPagerAdapter(childFragmentManager) {
            override fun getItem(position: Int): Fragment {
                return when (position) {
                    0 -> LoanRepaymentScheduleFragment.getInstance(mLoanLog?.id ?: 0)
                    1 -> LoanRepaymentRecordFragment.getInstance(mLoanLog?.id ?: 0)
                    else -> throw IllegalArgumentException("无效的pager index")
                }
            }

            override fun getCount(): Int = 2

            override fun getPageTitle(position: Int): CharSequence? {
                return when (position) {
                    0 -> "还款计划"
                    1 -> "还款记录"
                    else -> throw IllegalArgumentException("无效的pager index")
                }
            }
        }
        tabPagerHeader.setupWithViewPager(viewPager, false)

    }

}