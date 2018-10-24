package com.apm29.yjw.demo.ui.list.adapter

import android.view.View
import com.apm29.yjw.demo.model.*
import com.apm29.yjw.demo.model.ImageData
import com.apm29.yjw.demo.ui.form.information.ImageInfoFromFragment
import com.apm29.yjw.demo.ui.list.base.BaseEmptyAdapter
import com.apm29.yjw.gentleloansdemo.R



class ImageAdapter<T : ImageData>(
        private val editable: Boolean,
        private val maxCount: Int,
        list: List<T>,
        emptyRes: Int,
        val addOp: () -> T,
        bindOp: (ImageInfoFromFragment.ImageHolder, T, Int) -> Unit
) : BaseEmptyAdapter<T, ImageInfoFromFragment.ImageHolder>(
        list, emptyRes, bindOp
) {
    override fun createEmptyHolder(inflate: View): ImageInfoFromFragment.ImageHolder {
        return ImageInfoFromFragment.ImageHolder(inflate)
    }

    override fun createBaseHolder(viewType: Int, view: View): ImageInfoFromFragment.ImageHolder {
        if (viewType == ADD_TYPE) {
            view.setOnClickListener {
                if (list is MutableList) {
                    if (list.add(addOp())) {
                        notifyItemInserted(list.size)
                    }
                }
            }
        }
        return ImageInfoFromFragment.ImageHolder(view)
    }

    override fun layout(viewType: Int): Int {
        return when (viewType) {
            ADD_TYPE -> R.layout.add_item_layout
            EMPTY_TYPE -> R.layout.empty_item_layout
            else -> R.layout.image_captured_item_layout
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (editable) {
            if (position == list.size) {
                ADD_TYPE
            } else {
                0
            }
        } else {
            if (position == 0 && list.isEmpty()) {
                EMPTY_TYPE
            } else {
                0
            }
        }
    }

    override fun getItemCount(): Int {
        return if (editable) {
            if (list.size < maxCount) {
                list.size + 1
            } else {
                maxCount
            }
        } else {
            when {
                list.isEmpty() -> 1
                list.size <= maxCount -> list.size
                else -> maxCount
            }
        }
    }

    override fun onBindViewHolder(holder: ImageInfoFromFragment.ImageHolder, position: Int) {
        if (position in 0 until list.size) {
            val realPosition = holder.adapterPosition
            val data = list[realPosition]
            bindOp(holder, data, realPosition)
            holder.tvDelete?.setOnClickListener {
                if (list is MutableList) {
                    list.remove(data)
                    notifyDataSetChanged()
                }
            }
        }
    }

}