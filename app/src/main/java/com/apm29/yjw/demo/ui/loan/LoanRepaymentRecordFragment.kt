package com.apm29.yjw.demo.ui.loan

import android.annotation.SuppressLint
import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.Group
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.Explode
import com.apm29.yjw.demo.di.component.AppComponent
import com.apm29.yjw.demo.di.component.DaggerDefaultFragmentComponent
import com.apm29.yjw.demo.di.module.DefaultFragmentModule
import com.apm29.yjw.demo.model.RepaymentRecord
import com.apm29.yjw.demo.ui.list.adapter.LoanRepaymentRecordAdapter
import com.apm29.yjw.demo.ui.list.base.BaseListFragment
import com.apm29.yjw.demo.utils.hasNonZeroValue
import com.apm29.yjw.demo.utils.toStringDot2F
import com.apm29.yjw.gentleloansdemo.R
import kotlinx.android.synthetic.main.loan_repayment_record_item_layout.*

class LoanRepaymentRecordFragment:BaseListFragment<RepaymentRecord,LoanRepaymentRecordFragment.VH, LoanRepaymentRecordAdapter>() {

    override val useToolBar: Boolean = false
    companion object {
        private const val BUNDLE_KEY_ID = "id"

        fun getInstance(id:Int):LoanRepaymentRecordFragment{
            val fragment = LoanRepaymentRecordFragment()
            fragment.arguments = bundleOf(
                    BUNDLE_KEY_ID to id
            )
            return fragment
        }
    }

    override fun setupAdapter(): LoanRepaymentRecordAdapter = LoanRepaymentRecordAdapter(
            data,::bindData
    )

    @SuppressLint("SetTextI18n")
    private fun bindData(holder:VH, data:RepaymentRecord, position:Int){
        holder.tvTotalAmount?.text = data.total_amount.toStringDot2F()
        holder.tvTerm?.text = data.term
        holder.tvExpectTime?.text = "${data.actual_time_Ymd}\n${data.actual_time_His}"

        holder.tvPrincipal?.text = data.loans_amount.toStringDot2F()
        holder.tvInterest?.text =data.interest_amount.toStringDot2F()

        holder.tvFine?.text = data.fine_amount.toStringDot2F()
        holder.tvOverdueInterest?.text = data.overdue_amount.toStringDot2F()

        holder.tvRestPrincipal?.text = ""
        holder.tvInterestTerm?.text = ""
        holder.tvServiceCharge?.text = ""

        //有逾期或者罚息
        if (data.overdue_amount.hasNonZeroValue()||data.fine_amount.hasNonZeroValue()){
            grpInterest?.visibility = View.GONE
            grpOverdueInterest?.visibility = View.VISIBLE
            grpFineInterest?.visibility = View.VISIBLE
        }else{
            grpInterest?.visibility = View.VISIBLE
            grpOverdueInterest?.visibility = View.GONE
            grpFineInterest?.visibility = View.GONE
        }
    }

    override fun load(refresh: Boolean, lists: ArrayList<RepaymentRecord>) {
        mViewModel.loadRepaymentRecord(refresh,arguments?.getInt(BUNDLE_KEY_ID)?:0,lists)
    }

    override fun setupModel(appComponent: AppComponent) {
        DaggerDefaultFragmentComponent.builder()
                .appComponent(appComponent)
                .defaultFragmentModule(DefaultFragmentModule(this))
                .build()
                .inject(this)
    }


    class VH(itemView:View):RecyclerView.ViewHolder(itemView){
        val tvTotalAmount  = itemView.findViewById<TextView?>(R.id.tvTotalAmount)
        val tvTerm = itemView.findViewById<TextView?>(R.id.tvTerm)
        val tvExpectTime = itemView.findViewById<TextView?>(R.id.tvExpectTime)

        /***正常还款**/
        //本金
        val tvPrincipal = itemView.findViewById<TextView?>(R.id.tvPrincipal)
        //利息
        val tvInterest = itemView.findViewById<TextView?>(R.id.tvInterest)

        /**逾期还款**/
        //逾期利息
        val tvOverdueInterest = itemView.findViewById<TextView?>(R.id.tvOverdue)
        //罚息
        val tvFine = itemView.findViewById<TextView?>(R.id.tvFineInterest)


        /**提前还款***/
        //剩余本金
        val tvRestPrincipal = itemView.findViewById<TextView>(R.id.textRestPrincipal)
        //当期利息
        val tvInterestTerm = itemView.findViewById<TextView>(R.id.tvInterestTerm)
        //提前还款手续费
        val tvServiceCharge = itemView.findViewById<TextView?>(R.id.tvServiceCharge)

        val grpPrincial = itemView.findViewById<Group?>(R.id.grpPrincipal)
        val grpInterest = itemView.findViewById<Group?>(R.id.grpInterest)
        val grpOverdue = itemView.findViewById<Group?>(R.id.grpOverdueInterest)
        val grpFine = itemView.findViewById<Group?>(R.id.grpFineInterest)
        val grpRestPrincipal = itemView.findViewById<Group?>(R.id.grpRestPrincipal)
        val grpInterestTerm = itemView.findViewById<Group?>(R.id.grpInterestTerm)
        val grpServiceCharge = itemView.findViewById<Group?>(R.id.grpServiceCharge)
    }
}