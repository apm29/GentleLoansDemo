package com.apm29.yjw.demo.ui.list.adapter

import android.view.View
import com.apm29.yjw.demo.model.LoanLog
import com.apm29.yjw.demo.ui.list.VH
import com.apm29.yjw.demo.ui.list.base.BaseEmptyAdapter
import com.apm29.yjw.gentleloansdemo.R

class LoanLogAdapter(list: List<LoanLog>, bindOp: (VH, LoanLog,Int) -> Unit) : BaseEmptyAdapter<LoanLog, VH>(list = list,bindOp = bindOp) {
    override fun createEmptyHolder(inflate: View): VH {
        return VH(inflate)
    }

    override fun createBaseHolder(viewType: Int, view: View): VH {
        return VH(view)
    }

    override fun layout(viewType: Int): Int {
        return R.layout.loan_item_layout
    }

}
