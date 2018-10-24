package com.apm29.yjw.demo.ui.main

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.apm29.yjw.demo.di.component.AppComponent
import com.apm29.yjw.demo.di.component.DaggerDefaultFragmentComponent
import com.apm29.yjw.demo.di.module.DefaultFragmentModule
import com.apm29.yjw.demo.model.PushMessage
import com.apm29.yjw.demo.third.PushMessageHandler
import com.apm29.yjw.demo.ui.list.adapter.PushMessageAdapter
import com.apm29.yjw.demo.ui.list.base.BaseListFragment
import com.apm29.yjw.demo.utils.findHostNavController
import com.apm29.yjw.gentleloansdemo.R

class PushMessageListFragment : BaseListFragment<PushMessage, PushMessageListFragment.VH, PushMessageAdapter>() {


    override fun setupAdapter(): PushMessageAdapter {
        return PushMessageAdapter(data, ::bindData)
    }

    private fun bindData(holder: VH, data: PushMessage, position: Int) {
        holder.tvContent?.text = data.content
        holder.tvMessageTime?.text = data.createTime
        holder.ivRedDot?.visibility = if (data.isRead) View.INVISIBLE else View.VISIBLE

        holder.itemView.setOnClickListener {
            PushMessageHandler.handleMessage(findHostNavController(),data)
            PushMessageHandler.markMessageAsRead(data)
        }
    }

    override fun load(refresh: Boolean, lists: ArrayList<PushMessage>) {
        mViewModel.loadPushMessage(refresh, lists)
    }

    override fun setupModel(appComponent: AppComponent) {
        DaggerDefaultFragmentComponent.builder()
                .appComponent(appComponent)
                .defaultFragmentModule(DefaultFragmentModule(this))
                .build()
                .inject(this)
    }


    class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvMessageTime: TextView? = itemView.findViewById(R.id.tvMessageTime)
        val tvContent: TextView? = itemView.findViewById(R.id.tvContent)
        val ivRedDot: ImageView? = itemView.findViewById(R.id.ivRedDot)
    }

}

