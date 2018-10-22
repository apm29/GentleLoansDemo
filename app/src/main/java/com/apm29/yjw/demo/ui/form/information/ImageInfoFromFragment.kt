package com.apm29.yjw.demo.ui.form.information

import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.view.View
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.view.ViewCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.apm29.yjw.demo.arch.BaseFragment
import com.apm29.yjw.demo.di.component.AppComponent
import com.apm29.yjw.demo.di.component.DaggerDefaultFragmentComponent
import com.apm29.yjw.demo.di.module.DefaultFragmentModule
import com.apm29.yjw.demo.ui.dialog.ImageEditFragmentArgs
import com.apm29.yjw.demo.ui.list.base.BaseEmptyAdapter
import com.apm29.yjw.demo.ui.list.base.EMPTY_TYPE
import com.apm29.yjw.demo.utils.navigateErrorHandled
import com.apm29.yjw.demo.viewmodel.CommunicateViewModel
import com.apm29.yjw.demo.viewmodel.InformationFormViewModel
import com.apm29.yjw.gentleloansdemo.R
import kotlinx.android.synthetic.main.image_info_form_fragment.*
import java.lang.IllegalArgumentException
import kotlin.collections.ArrayList

class ImageInfoFromFragment : BaseFragment<InformationFormViewModel>() {
    override fun setupViewLayout(savedInstanceState: Bundle?): Int {
        return R.layout.image_info_form_fragment
    }

    override fun setupModel(appComponent: AppComponent) {
        DaggerDefaultFragmentComponent.builder()
                .appComponent(appComponent)
                .defaultFragmentModule(DefaultFragmentModule(this))
                .build()
                .inject(this)
    }

    private fun bindHolder(holder: ImageHolder, data: Photo, position: Int) {

        data.position = position
        holder.ivCapturedImage?.setOnClickListener {
            val args = ImageEditFragmentArgs.Builder()
                    .setPhotoData(data)
                    .build()
            val name = getString(R.string.camera_image)
            val extras = FragmentNavigatorExtras(
                    holder.ivCapturedImage to name
            )
            ViewCompat.setTransitionName(holder.ivCapturedImage, name)
            navigateErrorHandled(R.id.imageEditFragment, args.toBundle(), null, extras)
        }
        holder.tvDebug?.text = data.imageUrl
    }

    private val houseEditable = true

    private val mDataHouse:ArrayList<Photo> = arrayListOf()

    override fun setupViews(savedInstanceState: Bundle?) {

        listHouse.layoutManager = GridLayoutManager(requireContext(), 2)
        listHouse.adapter = ImageAdapter(houseEditable, 6, mDataHouse, R.layout.empty_item_layout,
                addOp = {
                    return@ImageAdapter Photo(
                            houseEditable, null, null, type = IMAGE_TYPE_HOUSE,file = null
                    )
                },
                bindOp = ::bindHolder
        )
    }

    override fun initData(savedInstanceState: Bundle?) {
        val communicateViewModel = ViewModelProviders.of(requireActivity()).get(CommunicateViewModel::class.java)
        communicateViewModel.mPhotoData.observe(this, Observer {
            it.getContentIfNotHandled { image ->
                when(image?.type){
                    IMAGE_TYPE_HOUSE->{
                        mDataHouse[image.position].imageUrl = image.imageUrl
                        mDataHouse[image.position].locationDes = image.locationDes
                        listHouse.adapter?.notifyItemChanged(image.position)
                    }
                    else->{
                        throw IllegalArgumentException("错误的照片排列位置")
                    }
                }
            }
        })
    }

    class ImageHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivCapturedImage: ImageView? = itemView.findViewById(R.id.ivImageCaptured)
        val tvEdit: TextView? = itemView.findViewById(R.id.tvEdit)
        val tvDelete: TextView? = itemView.findViewById(R.id.tvDelete)
        val tvDebug: TextView? = itemView.findViewById(R.id.tvDebug)
        val checkStatus: CheckBox? = itemView.findViewById(R.id.checkStatus)
        val progressView: ProgressBar? = itemView.findViewById(R.id.progressView)

    }


    class ImageAdapter<T : ImageData>(
            private val editable: Boolean,
            private val maxCount: Int,
            list: List<T>,
            emptyRes: Int,
            val addOp: () -> T,
            bindOp: (ImageHolder, T, Int) -> Unit
    ) : BaseEmptyAdapter<T, ImageHolder>(
            list, emptyRes, bindOp
    ) {
        override fun createEmptyHolder(inflate: View): ImageHolder {
            return ImageHolder(inflate)
        }

        override fun createBaseHolder(viewType: Int, view: View): ImageHolder {
            if (viewType == ADD_TYPE) {
                view.setOnClickListener {
                    if (list is MutableList) {
                        if (list.add(addOp())) {
                            notifyItemInserted(list.size)
                        }
                    }
                }
            }
            return ImageHolder(view)
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

        override fun onBindViewHolder(holder: ImageHolder, position: Int) {
            if (position in 0 until list.size) {
                val data = list[holder.adapterPosition]
                bindOp(holder, data, holder.adapterPosition)
            }
        }

    }
}

const val IMAGE_TYPE_HOUSE = 1
const val IMAGE_TYPE_COMPANY = 2

const val ADD_TYPE = 29

interface ImageData {
    var imageUrl: String?
    var locationDes: String?
    var editable: Boolean
    var type: Int
    var position: Int
    var file:String?
}


data class Photo(
        override var editable: Boolean,
        override var imageUrl: String?,/**thumb*/
        override var locationDes: String?,
        override var type: Int,
        override var position: Int = -1,
        override var file: String?
) : ImageData, Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readByte() != 0.toByte(),
            parcel.readString(),
            parcel.readString(),
            parcel.readInt(),
            parcel.readInt(),
            parcel.readString()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeByte(if (editable) 1 else 0)
        parcel.writeString(imageUrl)
        parcel.writeString(locationDes)
        parcel.writeInt(type)
        parcel.writeInt(position)
        parcel.writeString(file)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Photo> {
        override fun createFromParcel(parcel: Parcel): Photo {
            return Photo(parcel)
        }

        override fun newArray(size: Int): Array<Photo?> {
            return arrayOfNulls(size)
        }
    }


}