package com.apm29.yjw.demo.ui.form.information

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.apm29.yjw.demo.arch.BaseFragment
import com.apm29.yjw.demo.di.component.AppComponent
import com.apm29.yjw.demo.di.component.DaggerDefaultFragmentComponent
import com.apm29.yjw.demo.di.module.DefaultFragmentModule
import com.apm29.yjw.demo.ui.list.base.BaseEmptyAdapter
import com.apm29.yjw.demo.viewmodel.InformationFormViewModel
import com.apm29.yjw.gentleloansdemo.R
import kotlinx.android.synthetic.main.image_info_form_fragment.*

class ImageInfoFromFragment:BaseFragment<InformationFormViewModel>() {
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

    fun bindHolder(holder: ImageHolder,data :String,position:Int){

    }

    override fun setupViews(savedInstanceState: Bundle?) {
        listHouse.layoutManager = GridLayoutManager(requireContext(),3)
        listHouse.adapter = object :BaseEmptyAdapter<String,ImageHolder>(
                arrayListOf("2","2","2","2","2"),bindOp = ::bindHolder
        ){
            override fun createEmptyHolder(inflate: View): ImageHolder {
                return ImageHolder(inflate)
            }

            override fun createBaseHolder(viewType: Int, view: View): ImageHolder {
                return ImageHolder(view)
            }

            override fun layout(): Int {
                return R.layout.image_captured_layout
            }

        }

    }

    class ImageHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

    }
}