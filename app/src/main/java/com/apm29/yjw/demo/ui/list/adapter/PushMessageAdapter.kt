package com.apm29.yjw.demo.ui.list.adapter

import android.view.View
import com.apm29.yjw.demo.model.PushMessage
import com.apm29.yjw.demo.ui.list.base.BaseEmptyAdapter
import com.apm29.yjw.demo.ui.main.PushMessageListFragment
import com.apm29.yjw.gentleloansdemo.R

class PushMessageAdapter(
        list: List<PushMessage>,
        bindOp: (PushMessageListFragment.VH, PushMessage, Int) -> Unit
) : BaseEmptyAdapter<PushMessage, PushMessageListFragment.VH>(
        list,
        bindOp = bindOp
) {
    override fun createEmptyHolder(inflate: View): PushMessageListFragment.VH {
        return PushMessageListFragment.VH(inflate)
    }

    override fun createBaseHolder(viewType: Int, view: View): PushMessageListFragment.VH {
        return PushMessageListFragment.VH(view)
    }

    override fun layout(viewType: Int): Int {
        return R.layout.push_message_item_layout
    }

}