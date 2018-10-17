package com.apm29.yjw.demo.ui.form.register

import android.os.Bundle
import android.view.View
import android.widget.CheckBox
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.constraintlayout.widget.Group
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.apm29.yjw.demo.arch.BaseFragment
import com.apm29.yjw.demo.di.component.AppComponent
import com.apm29.yjw.demo.di.component.DaggerDefaultFragmentComponent
import com.apm29.yjw.demo.di.module.DefaultFragmentModule
import com.apm29.yjw.demo.model.Car
import com.apm29.yjw.demo.model.Estate
import com.apm29.yjw.demo.ui.list.base.BaseEmptyAdapter
import com.apm29.yjw.demo.ui.list.base.EMPTY_TYPE
import com.apm29.yjw.demo.viewmodel.RegisterFormViewModel
import com.apm29.yjw.gentleloansdemo.R
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.family_assets_fragment.*

class FamilyAssetsFragment : BaseFragment<RegisterFormViewModel>() {
    override fun setupViewLayout(savedInstanceState: Bundle?): Int {
        return R.layout.family_assets_fragment
    }

    override fun setupModel(appComponent: AppComponent) {
        DaggerDefaultFragmentComponent.builder()
                .defaultFragmentModule(DefaultFragmentModule(this))
                .appComponent(appComponent)
                .build()
                .inject(this)
    }

    private var dataEstate: List<Estate> = arrayListOf()
    private var dataCar: List<Car> = arrayListOf()
    override fun setupViews(savedInstanceState: Bundle?) {
        swipeRefresh.setOnRefreshListener {
            mViewModel.assets()
        }
    }

    override fun initData(savedInstanceState: Bundle?) {
        mViewModel.assets()

        mViewModel.estateData.observe(this, Observer {
            dataEstate = it
            setupList()
        })
        mViewModel.carData.observe(this, Observer {
            dataCar = it
            setupList()
        })
    }

    private fun setupList() {
        listEstate.layoutManager = LinearLayoutManager(requireContext())
        listCar.layoutManager = LinearLayoutManager(requireContext())

        listEstate.adapter = AssetsAdapter(
                list = dataEstate,
                layoutRes = R.layout.estate_item_layout,
                clickAdd = {
                    Estate()
                },
                bindOp = { h, e ->
                    h.tvEstateTitle?.text = "房产信息"

                }
        )
    }

    override fun hideLoading() {
        super.hideLoading()
        swipeRefresh.isRefreshing = false
    }
}

class AssetsHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    //empty / estate / car 三个公用ViewHolder view都需要为nullable
    var tvEstateTitle:TextView? = itemView.findViewById(R.id.tvEstateTitle)

    var okOwner:TextView? = itemView.findViewById(R.id.okOwner)
    var tvOwner:TextView? = itemView.findViewById(R.id.tvOwner)
    var etOwner:TextInputLayout? = itemView.findViewById(R.id.etOwner)

    var okArea:TextView? = itemView.findViewById(R.id.okArea)
    var tvArea:TextView? = itemView.findViewById(R.id.tvArea)
    var etArea:TextInputLayout? = itemView.findViewById(R.id.etArea)

    var okLocation:TextView? = itemView.findViewById(R.id.okLocation)
    var tvLocation:TextView? = itemView.findViewById(R.id.tvLocation)
    var etLocation:TextInputLayout? = itemView.findViewById(R.id.etLocation)

    var okMortgageStatus:TextView? = itemView.findViewById(R.id.okMortgageStatus)
    var tvMortgageStatus:TextView? = itemView.findViewById(R.id.tvMortgageStatus)
    var pickerMortgage:TextView? = itemView.findViewById(R.id.pickerMortgage)

    var okMortgage1:TextView? = itemView.findViewById(R.id.okMortgage1)
    var tvMortgage1:TextView? = itemView.findViewById(R.id.tvMortgage1)
    var etMortgage1:TextInputLayout? = itemView.findViewById(R.id.etMortgage1)

    var okMortgage2:TextView? = itemView.findViewById(R.id.okMortgage2)
    var tvMortgage2:TextView? = itemView.findViewById(R.id.tvMortgage2)
    var etMortgage2:TextInputLayout? = itemView.findViewById(R.id.etMortgage2)

    var groupMortgages:Group? = itemView.findViewById(R.id.group_mortgages)


}

class AssetsAdapter<T>(
        list: List<T>,
        private val editable: Boolean = true,
        @LayoutRes private val layoutRes: Int,
        private val clickAdd: (View) -> T,
        private val clickRemove: ((View, Int) -> Unit)? = null,
        bindOp: (AssetsHolder, T) -> Unit
) : BaseEmptyAdapter<T, AssetsHolder>(
        list = list,
        emptyRes = R.layout.add_item_layout,
        bindOp = bindOp
) {
    override fun createEmptyHolder(inflate: View): AssetsHolder {
        val addView:View? = inflate.findViewById(R.id.add)
        addView?.setOnClickListener {
            val startSize = list.size
            val add = clickAdd(it)
            if (list is MutableList){
                list.add(add)
            }
            val endSize = list.size
            if (startSize < endSize) {
                notifyItemRangeChanged(startSize, endSize)
            }
        }
        return AssetsHolder(inflate)
    }

    override fun createBaseHolder(viewType: Int, view: View): AssetsHolder {
        return AssetsHolder(view)
    }

    override fun onBindViewHolder(holder: AssetsHolder, position: Int) {
        if (position in 0 until list.size) {
            val data = list[position]
            bindOp(holder, data)
            val removeView:View? = holder.itemView.findViewById(R.id.remove)
            removeView?.setOnClickListener {
                if (list is MutableList){
                    val currentIndex = holder.adapterPosition
                    list.removeAt(currentIndex)
                    println("$currentIndex item removed")
                    notifyItemRemoved(currentIndex)
                    clickRemove?.invoke(it, currentIndex)
                }
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