package com.apm29.yjw.demo.ui.loan

import android.view.View
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.view.ViewCompat
import androidx.navigation.Navigator
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.recyclerview.widget.RecyclerView
import com.apm29.yjw.demo.di.component.AppComponent
import com.apm29.yjw.demo.di.component.DaggerDefaultFragmentComponent
import com.apm29.yjw.demo.di.module.DefaultFragmentModule
import com.apm29.yjw.demo.model.LoanLog
import com.apm29.yjw.demo.ui.list.adapter.LoanLogAdapter
import com.apm29.yjw.demo.ui.list.base.BaseListFragment
import com.apm29.yjw.demo.utils.navigateErrorHandled
import com.apm29.yjw.gentleloansdemo.R


class LoanLogListFragment: BaseListFragment<LoanLog, LoanLogListFragment.VH, LoanLogAdapter>() {
    override fun load(refresh: Boolean, lists: ArrayList<LoanLog>) {
        mViewModel.loadLoanLogs(refresh,lists)
    }

    override fun setupAdapter(): LoanLogAdapter {
        return LoanLogAdapter(data){
            holder,data,_->

            holder.itemView.setOnClickListener {
                val args = LoanDetailFragmentArgs.Builder()
                        .setLoanLog(data)
                        .build()
                var extra:Navigator.Extras? = null
                holder.cardFrame?.let {
                    card->
                    val transitionName = getString(R.string.app_icon)
                    extra = FragmentNavigatorExtras(
                            card to transitionName
                    )
                    ViewCompat.setTransitionName(card, transitionName)
                }

                navigateErrorHandled(R.id.loanDetailFragment,args.toBundle(),null,extra = extra)
            }
            holder.tvTime?.text = data.actualTime
            holder.tvPayType?.text = data.repaymentTypeComment
            holder.tvPayTypeComment?.text = getString(R.string.text_pay_type_comment,data.term,data.interestRate)
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
    class VH(itemView: View) : RecyclerView.ViewHolder(itemView){
        val tvPayType = itemView.findViewById<TextView?>(R.id.tvRepaymentType)
        val tvPayTypeComment = itemView.findViewById<TextView?>(R.id.tvRepaymentTypeComment)
        val tvAmount = itemView.findViewById<TextView?>(R.id.tvAmount)
        val tvTime = itemView.findViewById<TextView?>(R.id.tvTime)
        val cardFrame = itemView.findViewById<CardView?>(R.id.cardFrame)
    }
}

