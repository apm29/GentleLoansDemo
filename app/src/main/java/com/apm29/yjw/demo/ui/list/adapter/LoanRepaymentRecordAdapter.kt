package com.apm29.yjw.demo.ui.list.adapter

import android.view.View
import com.apm29.yjw.demo.model.RepaymentRecord
import com.apm29.yjw.demo.model.RepaymentSchedule
import com.apm29.yjw.demo.ui.list.base.BaseEmptyAdapter
import com.apm29.yjw.demo.ui.loan.LoanRepaymentRecordFragment
import com.apm29.yjw.demo.ui.loan.LoanRepaymentScheduleFragment
import com.apm29.yjw.gentleloansdemo.R

class LoanRepaymentRecordAdapter(
        list: List<RepaymentRecord>,
        bindOp: (LoanRepaymentRecordFragment.VH, RepaymentRecord, Int) -> Unit
)
    : BaseEmptyAdapter<RepaymentRecord, LoanRepaymentRecordFragment.VH>(
        list = list,
        bindOp = bindOp
) {
    override fun createEmptyHolder(inflate: View): LoanRepaymentRecordFragment.VH {
        return LoanRepaymentRecordFragment.VH(inflate)
    }

    override fun createBaseHolder(viewType: Int, view: View): LoanRepaymentRecordFragment.VH {
        return LoanRepaymentRecordFragment.VH(view)
    }

    override fun layout(viewType: Int): Int {
        return R.layout.loan_repayment_record_item_layout
    }

}
