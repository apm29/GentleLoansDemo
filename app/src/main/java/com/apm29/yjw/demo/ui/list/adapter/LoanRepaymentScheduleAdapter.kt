package com.apm29.yjw.demo.ui.list.adapter

import android.view.View
import com.apm29.yjw.demo.model.RepaymentSchedule
import com.apm29.yjw.demo.ui.list.base.BaseEmptyAdapter
import com.apm29.yjw.demo.ui.loan.LoanRepaymentScheduleFragment
import com.apm29.yjw.gentleloansdemo.R

class LoanRepaymentScheduleAdapter(
        list: List<RepaymentSchedule>,
        bindOp: (LoanRepaymentScheduleFragment.VH, RepaymentSchedule, Int) -> Unit
)
    : BaseEmptyAdapter<RepaymentSchedule, LoanRepaymentScheduleFragment.VH>(
        list = list,
        bindOp = bindOp
) {
    override fun createEmptyHolder(inflate: View): LoanRepaymentScheduleFragment.VH {
        return LoanRepaymentScheduleFragment.VH(inflate)
    }

    override fun createBaseHolder(viewType: Int, view: View): LoanRepaymentScheduleFragment.VH {
        return LoanRepaymentScheduleFragment.VH(view)
    }

    override fun layout(viewType: Int): Int {
        return R.layout.loan_repayment_schedule_item_layout
    }

}
