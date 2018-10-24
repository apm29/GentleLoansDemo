package com.apm29.yjw.demo.ui.loan

import android.view.View
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.RecyclerView
import com.apm29.yjw.demo.di.component.AppComponent
import com.apm29.yjw.demo.di.component.DaggerDefaultFragmentComponent
import com.apm29.yjw.demo.di.module.DefaultFragmentModule
import com.apm29.yjw.demo.model.RepaymentSchedule
import com.apm29.yjw.demo.ui.list.adapter.LoanRepaymentScheduleAdapter
import com.apm29.yjw.demo.ui.list.base.BaseListFragment
import com.apm29.yjw.gentleloansdemo.R

class LoanRepaymentScheduleFragment:BaseListFragment<RepaymentSchedule,LoanRepaymentScheduleFragment.VH, LoanRepaymentScheduleAdapter>() {

    override val useToolBar: Boolean = false

    companion object {
        private const val BUNDLE_KEY_ID = "id"

        fun getInstance(id:Int):LoanRepaymentScheduleFragment{
            val fragment = LoanRepaymentScheduleFragment()
            fragment.arguments = bundleOf(
                    BUNDLE_KEY_ID to id
            )
            return fragment
        }
    }

    override fun setupAdapter(): LoanRepaymentScheduleAdapter = LoanRepaymentScheduleAdapter(
            data,::bindData
    )

    private fun bindData(holder:VH, data:RepaymentSchedule, position:Int){
        holder.tvAmount?.text = data.total_amount
        holder.tvExpectTime?.text = data.expect_time
        holder.tvInterest?.text = getString(R.string.text_repayment_interest_normal,data.interest_amount)
        holder.tvTerm?.text = data.term

        if (data.fine_amount!=null && data.fine_amount>0){
            holder.tvInterest?.text = getString(R.string.text_repayment_interest_overdue,data.overdue_amount,data.fine_amount)
        }
    }

    override fun load(refresh: Boolean, lists: ArrayList<RepaymentSchedule>) {
        mViewModel.loadLoanSchedule(refresh,arguments?.getInt(BUNDLE_KEY_ID)?:0,lists)
    }

    override fun setupModel(appComponent: AppComponent) {
        DaggerDefaultFragmentComponent.builder()
                .appComponent(appComponent)
                .defaultFragmentModule(DefaultFragmentModule(this))
                .build()
                .inject(this)
    }


    class VH(itemView:View):RecyclerView.ViewHolder(itemView){
        val tvTerm = itemView.findViewById<TextView?>(R.id.textTerm)
        val tvExpectTime = itemView.findViewById<TextView?>(R.id.textExpectedTime)
        val tvAmount = itemView.findViewById<TextView?>(R.id.textTotal)
        val tvInterest = itemView.findViewById<TextView?>(R.id.tvInterest)
    }
}