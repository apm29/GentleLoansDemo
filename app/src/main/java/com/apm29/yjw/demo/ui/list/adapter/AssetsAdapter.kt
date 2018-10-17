package com.apm29.yjw.demo.ui.list.adapter

import android.view.View
import androidx.annotation.LayoutRes
import com.apm29.yjw.demo.ui.form.register.FamilyAssetsFragment
import com.apm29.yjw.demo.ui.list.base.BaseEmptyAdapter
import com.apm29.yjw.demo.ui.list.base.EMPTY_TYPE
import com.apm29.yjw.gentleloansdemo.R

class AssetsAdapter<T>(
        list: List<T>,
        private val editable: Boolean = true,
        @LayoutRes private val layoutRes: Int,
        private val clickAdd: (View) -> T,
        private val clickRemove: ((View, Int) -> Unit)? = null,
        bindOp: (FamilyAssetsFragment.AssetsHolder, T, Int) -> Unit
) : BaseEmptyAdapter<T, FamilyAssetsFragment.AssetsHolder>(
        list = list,
        emptyRes = R.layout.add_item_layout,
        bindOp = bindOp
) {
    override fun createEmptyHolder(inflate: View): FamilyAssetsFragment.AssetsHolder {
        val addView: View? = inflate.findViewById(R.id.add)
        val hintAdd: View? = inflate.findViewById(R.id.hintAdd)
        if (editable) {
            addView?.setOnClickListener {
                hintAdd?.requestFocus()
                val startSize = list.size
                val add = clickAdd(it)
                if (list is MutableList) {
                    list.add(add)
                }
                val endSize = list.size
                if (startSize < endSize) {
                    notifyItemRangeChanged(startSize, endSize)
                }
            }
        }
        return FamilyAssetsFragment.AssetsHolder(inflate)
    }

    override fun createBaseHolder(viewType: Int, view: View): FamilyAssetsFragment.AssetsHolder {
        return FamilyAssetsFragment.AssetsHolder(view)
    }

    override fun onBindViewHolder(holder: FamilyAssetsFragment.AssetsHolder, position: Int) {
        if (position in 0 until list.size) {
            val data = list[position]
            bindOp(holder, data, holder.adapterPosition)
            val removeView: View? = holder.itemView.findViewById(R.id.remove)
            if (editable) {
                holder.removeIcon?.text = removeView?.context?.getString(R.string.减去)
                removeView?.setOnClickListener {
                    if (list is MutableList) {
                        val currentIndex = holder.adapterPosition
                        list.removeAt(currentIndex)
                        println("$currentIndex item removed")
                        notifyItemRemoved(currentIndex)
                        clickRemove?.invoke(it, currentIndex)
                    }
                }
            } else {
                holder.removeIcon?.text = ""
            }
        }
    }

    override fun layout(): Int {
        return layoutRes
    }

    override fun getItemCount(): Int {
        return if (editable) list.size + 1 else list.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (editable) {
            when {
                list.isEmpty() -> EMPTY_TYPE
                position == list.size -> EMPTY_TYPE
                else -> super.getItemViewType(position)
            }
        } else {
            0
        }
    }
}