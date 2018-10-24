package com.apm29.yjw.demo.ui.form.information

import android.os.Bundle
import android.view.View
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.apm29.yjw.demo.app.GlideApp
import com.apm29.yjw.demo.arch.BaseFragment
import com.apm29.yjw.demo.di.component.AppComponent
import com.apm29.yjw.demo.di.component.DaggerDefaultFragmentComponent
import com.apm29.yjw.demo.di.module.DefaultFragmentModule
import com.apm29.yjw.demo.model.*
import com.apm29.yjw.demo.model.Photo
import com.apm29.yjw.demo.ui.dialog.ImageEditFragmentArgs
import com.apm29.yjw.demo.ui.list.adapter.ImageAdapter
import com.apm29.yjw.demo.utils.navigateErrorHandled
import com.apm29.yjw.demo.viewmodel.CommunicateViewModel
import com.apm29.yjw.demo.viewmodel.InformationFormViewModel
import com.apm29.yjw.gentleloansdemo.R
import kotlinx.android.synthetic.main.image_info_form_fragment.*

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
            if (data.filePath == null) {
                toEditImage(data)
            } else {

            }
        }

        holder.tvEdit?.setOnClickListener {
            toEditImage(data)
        }

        holder.ivCapturedImage?.let {
            if (data.filePath != null) {
                GlideApp.with(this)
                        .load(data.filePath)
                        .placeholder(R.drawable.upload)
                        .into(it)
            } else {
                it.setImageBitmap(null)
            }
        }
        holder.tvDebug?.text = data.imageUrl
        holder.checkStatus?.isChecked = data.imageUrl!=null
    }

    private fun toEditImage(data: Photo) {
        val args = ImageEditFragmentArgs.Builder()
                .setPhotoData(data)
                .build()
        navigateErrorHandled(R.id.imageEditFragment, args.toBundle(), null, null)
    }

    private val houseEditable = true
    private val companyEditable = true
    private val idEditable = true
    private val coupleIdEditable = true
    private val registerAccountEditable = true
    private val marriageEditable = true
    private val proofEditable = true

    private val mDataHouse: ArrayList<Photo> = arrayListOf()
    private val mDataCompany: ArrayList<Photo> = arrayListOf()
    private val mDataId: ArrayList<Photo> = arrayListOf()
    private val mDataCoupleId: ArrayList<Photo> = arrayListOf()
    private val mDataRegisterAccount: ArrayList<Photo> = arrayListOf()
    private val mDataMarriage: ArrayList<Photo> = arrayListOf()
    private val mDataProof: ArrayList<Photo> = arrayListOf()

    override fun setupViews(savedInstanceState: Bundle?) {
        setupList()
    }

    private fun setupList() {
        listHouse.layoutManager = GridLayoutManager(requireContext(), 2)
        listHouse.adapter = ImageAdapter(houseEditable, 6, mDataHouse, R.layout.empty_item_layout,
                addOp = {
                    return@ImageAdapter Photo(
                            houseEditable, null, null, type = IMAGE_TYPE_HOUSE, filePath = null
                    )
                },
                bindOp = ::bindHolder
        )

        listCompany.layoutManager = GridLayoutManager(requireContext(), 2)
        listCompany.adapter = ImageAdapter(companyEditable, 6, mDataCompany, R.layout.empty_item_layout,
                addOp = {
                    return@ImageAdapter Photo(
                            companyEditable, null, null, type = IMAGE_TYPE_COMPANY, filePath = null
                    )
                },
                bindOp = ::bindHolder
        )

        listRegisterAccount.layoutManager = GridLayoutManager(requireContext(), 2)
        listRegisterAccount.adapter = ImageAdapter(registerAccountEditable, 6, mDataRegisterAccount, R.layout.empty_item_layout,
                addOp = {
                    return@ImageAdapter Photo(
                            registerAccountEditable, null, null, type = IMAGE_TYPE_REGISTER_ACCOUNT, filePath = null
                    )
                },
                bindOp = ::bindHolder
        )

        listId.layoutManager = GridLayoutManager(requireContext(), 2)
        listId.adapter = ImageAdapter(idEditable, 6, mDataId, R.layout.empty_item_layout,
                addOp = {
                    return@ImageAdapter Photo(
                            idEditable, null, null, type = IMAGE_TYPE_ID, filePath = null
                    )
                },
                bindOp = ::bindHolder
        )

        listCoupleId.layoutManager = GridLayoutManager(requireContext(), 2)
        listCoupleId.adapter = ImageAdapter(coupleIdEditable, 6, mDataCoupleId, R.layout.empty_item_layout,
                addOp = {
                    return@ImageAdapter Photo(
                            coupleIdEditable, null, null, type = IMAGE_TYPE_COUPLE_ID, filePath = null
                    )
                },
                bindOp = ::bindHolder
        )

        listMarriage.layoutManager = GridLayoutManager(requireContext(), 2)
        listMarriage.adapter = ImageAdapter(marriageEditable, 6, mDataMarriage, R.layout.empty_item_layout,
                addOp = {
                    return@ImageAdapter Photo(
                            marriageEditable, null, null, type = IMAGE_TYPE_MARRIAGE, filePath = null
                    )
                },
                bindOp = ::bindHolder
        )
        listProof.layoutManager = GridLayoutManager(requireContext(), 2)
        listProof.adapter = ImageAdapter(companyEditable, 6, mDataProof, R.layout.empty_item_layout,
                addOp = {
                    return@ImageAdapter Photo(
                            proofEditable, null, null, type = IMAGE_TYPE_PROOF, filePath = null
                    )
                },
                bindOp = ::bindHolder
        )
    }

    override fun initData(savedInstanceState: Bundle?) {
        val communicateViewModel = ViewModelProviders.of(requireActivity()).get(CommunicateViewModel::class.java)
        communicateViewModel.mPhotoData.observe(this, Observer {
            it.getContentIfNotHandled { image ->

                when (image?.type) {
                    IMAGE_TYPE_HOUSE -> {
                        replaceImageData(mDataHouse, listHouse, image)
                    }
                    IMAGE_TYPE_COMPANY -> {
                        replaceImageData(mDataCompany, listCompany, image)
                    }
                    IMAGE_TYPE_REGISTER_ACCOUNT -> {
                        replaceImageData(mDataRegisterAccount, listRegisterAccount, image)
                    }
                    IMAGE_TYPE_ID -> {
                        replaceImageData(mDataId, listId, image)
                    }
                    IMAGE_TYPE_COUPLE_ID -> {
                        replaceImageData(mDataCoupleId, listCoupleId, image)
                    }
                    IMAGE_TYPE_MARRIAGE -> {
                        replaceImageData(mDataMarriage, listMarriage, image)
                    }
                    IMAGE_TYPE_PROOF -> {
                        replaceImageData(mDataProof, listProof, image)
                    }
                    else -> {
                        throw IllegalArgumentException("错误的照片排列位置")
                    }
                }
            }
        })
    }


    private fun replaceImageData(dataList: ArrayList<Photo>, recyclerView: RecyclerView, image: Photo) {
        dataList[image.position].imageUrl = image.imageUrl
        dataList[image.position].filePath = image.filePath
        dataList[image.position].locationDes = image.locationDes
        recyclerView.adapter?.notifyItemChanged(image.position)
    }

    class ImageHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivCapturedImage: ImageView? = itemView.findViewById(R.id.ivImageCaptured)
        val tvEdit: TextView? = itemView.findViewById(R.id.tvEdit)
        val tvDelete: TextView? = itemView.findViewById(R.id.tvDelete)
        val tvDebug: TextView? = itemView.findViewById(R.id.tvDebug)
        val checkStatus: CheckBox? = itemView.findViewById(R.id.checkStatus)
        val progressView: ProgressBar? = itemView.findViewById(R.id.progressView)

    }

}
