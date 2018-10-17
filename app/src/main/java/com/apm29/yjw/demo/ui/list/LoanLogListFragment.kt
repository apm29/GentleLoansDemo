package com.apm29.yjw.demo.ui.list

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.apm29.yjw.demo.di.component.AppComponent
import com.apm29.yjw.demo.di.component.DaggerDefaultFragmentComponent
import com.apm29.yjw.demo.di.module.DefaultFragmentModule
import com.apm29.yjw.demo.model.LoanLog
import com.apm29.yjw.demo.ui.list.adapter.LoanLogAdapter
import com.apm29.yjw.demo.ui.list.base.BaseListFragment
import com.apm29.yjw.gentleloansdemo.R


class LoanLogListFragment: BaseListFragment<LoanLog, VH, LoanLogAdapter>() {
    override fun load(refresh: Boolean, lists: ArrayList<LoanLog>) {
        mViewModel.loadLoanLogs(refresh,lists)
    }

    override fun setupAdapter(): LoanLogAdapter {
        return LoanLogAdapter(data){
            holder,data->
            holder.tvTime?.text = data.actualTime
            holder.tvPayType?.text = if (data.repaymentType==0)"等额本息" else "先息后本"
            holder.tvPayTypeComment?.text = data.repaymentTypeComment
            holder.tvAmount?.text = data.totalAmount
        }
    }

    override fun setupModel(appComponent: AppComponent) {
        DaggerDefaultFragmentComponent.builder()
                .appComponent(appComponent)
                .defaultFragmentModule(DefaultFragmentModule(this))
                .build()
                .inject(this)
    }
}

class VH(itemView: View) : RecyclerView.ViewHolder(itemView){
    val tvPayType = itemView.findViewById<TextView?>(R.id.tvRepaymentType)
    val tvPayTypeComment = itemView.findViewById<TextView?>(R.id.tvRepaymentTypeComment)
    val tvAmount = itemView.findViewById<TextView?>(R.id.tvAmount)
    val tvTime = itemView.findViewById<TextView?>(R.id.tvTime)
}