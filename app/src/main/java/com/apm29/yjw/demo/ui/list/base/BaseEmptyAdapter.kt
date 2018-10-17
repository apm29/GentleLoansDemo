package com.apm29.yjw.demo.ui.list.base

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.apm29.yjw.gentleloansdemo.R

const val EMPTY_TYPE = 200

abstract class BaseEmptyAdapter<T, VH : RecyclerView.ViewHolder>(
        val list: List<T>,
        var emptyRes:Int = R.layout.empty_item_layout,
        val bindOp: (VH, T,Int) -> Unit
) : RecyclerView.Adapter<VH>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val layoutInflater = LayoutInflater.from(parent.context)
        if (viewType == EMPTY_TYPE) {
            return createEmptyHolder(layoutInflater.inflate(emptyRes, parent, false))
        }
        val inflate = layoutInflater.inflate(layout(), parent, false)
        return createBaseHolder(viewType, inflate)
    }

    abstract fun createEmptyHolder(inflate: View): VH

    abstract fun createBaseHolder(viewType: Int, view: View): VH
    @LayoutRes
    abstract fun layout(): Int


    override fun getItemCount(): Int {
        return if (list.isEmpty()) {
            1
        } else {
            list.size
        }
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        if (position in 0 until list.size) {
            bindOp(holder, list[holder.adapterPosition],holder.adapterPosition)
        }
    }

    override fun getItemViewType(position: Int): Int {
        if (list.isEmpty()) {
            return EMPTY_TYPE
        }
        return super.getItemViewType(position)
    }

}