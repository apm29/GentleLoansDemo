package com.apm29.yjw.demo.ui.list.base

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.apm29.yjw.gentleloansdemo.R
import com.paginate.recycler.LoadingListItemCreator

class DefaultLoadMreCreator: LoadingListItemCreator {
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
        val inflate = LayoutInflater.from(parent?.context).inflate(R.layout.loading_more_item_layout, parent, false)
        return VH(inflate)
    }

    override fun onBindViewHolder(p0: RecyclerView.ViewHolder?, p1: Int) {

    }
    class VH(view: View): RecyclerView.ViewHolder(view)
}