package com.apm29.yjw.demo.ui.form.register

import android.os.Bundle
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.RelativeSizeSpan
import android.text.style.SuperscriptSpan
import android.view.View
import android.widget.TextView
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
import com.apm29.yjw.demo.ui.list.adapter.AssetsAdapter
import com.apm29.yjw.demo.utils.findHostNavController
import com.apm29.yjw.demo.utils.mortgageList
import com.apm29.yjw.demo.utils.setupOneOptPicker
import com.apm29.yjw.demo.utils.showToast
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




    override fun setupViews(savedInstanceState: Bundle?) {
        swipeRefresh.setOnRefreshListener {
            mViewModel.assets()
        }

        btnSave.setOnClickListener {
            showToast("saved:\n ${estateAdapter.list.size} \n ${carAdapter.list.size}")
            mViewModel.postAssetsInfo(estateAdapter.list,carAdapter.list)
        }
    }

    override fun initData(savedInstanceState: Bundle?) {
        mViewModel.assets()

        mViewModel.assetsData.observe(this, Observer {
            setupList(it.first,it.second)
        })

        mViewModel.assetsPostResultData.observe(this, Observer {
            it.getContentIfNotHandled {
                result->
                if (result?.success() == true){
                    findHostNavController()?.popBackStack()
                }
                showToast(result?.msg)
            }
        })
    }
    private fun bindEstateOperation(holder: AssetsHolder, estate: Estate, position: Int) {
        holder.tvEstateTitle?.text = getString(R.string.text_estate)
        holder.pickerMortgage?.setupOneOptPicker(mortgageList, 1) { select, _, _ ->
            estate.mortgage = select == 0
            holder.groupMortgages?.visibility = if(estate.mortgage == true) View.VISIBLE else View.GONE
        }
        holder.groupMortgages?.visibility = if(estate.mortgage == true) View.VISIBLE else View.GONE
        holder.pickerMortgage?.text =  if(estate.mortgage == true) mortgageList[0] else mortgageList[1]
        /**
         * @href https://juejin.im/post/59bf91476fb9a00a583178d3
         */
        val stringBuilder = SpannableStringBuilder("m2")
        stringBuilder.setSpan( SuperscriptSpan(),1,2, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE )
        stringBuilder.setSpan( RelativeSizeSpan(0.5f),1,2, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE )
        holder.unitArea?.text = stringBuilder
    }




    private fun bindCarOperation(holder: AssetsHolder, car: Car, position: Int) {
        holder.tvEstateTitle?.text = getString(R.string.text_car)
    }

    lateinit var estateAdapter:AssetsAdapter<Estate>

    lateinit var carAdapter:AssetsAdapter<Car>

    private fun setupList(dataEstate:List<Estate>,dataCar:List<Car>) {
        estateAdapter = AssetsAdapter(
                list = dataEstate,
                layoutRes = R.layout.estate_item_layout,
                clickAdd = {
                    tvEstateTitle.requestFocus()
                    Estate()
                },
                bindOp = ::bindEstateOperation
        )
        carAdapter = AssetsAdapter(
                list = dataCar,
                layoutRes = R.layout.car_item_layout,
                clickAdd = {
                    tvCarTitle.requestFocus()
                    Car()
                },
                bindOp = ::bindCarOperation
        )
        listEstate.layoutManager = LinearLayoutManager(requireContext())
        listCar.layoutManager = LinearLayoutManager(requireContext())
        listEstate.adapter = estateAdapter
        listCar.adapter = carAdapter
    }

    override fun hideLoading() {
        super.hideLoading()
        swipeRefresh.isRefreshing = false
    }

    class AssetsHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        //empty / estate / car 三个公用ViewHolder view都需要为nullable

        //------------------------------------------------------
        var tvEstateTitle: TextView? = itemView.findViewById(R.id.tvEstateTitle)

        var okOwner: TextView? = itemView.findViewById(R.id.okOwner)
        var tvOwner: TextView? = itemView.findViewById(R.id.tvOwner)
        var etOwner: TextInputLayout? = itemView.findViewById(R.id.etOwner)

        var okArea: TextView? = itemView.findViewById(R.id.okArea)
        var tvArea: TextView? = itemView.findViewById(R.id.tvArea)
        var etArea: TextInputLayout? = itemView.findViewById(R.id.etArea)
        var unitArea: TextView? = itemView.findViewById(R.id.unitArea)

        var okLocation: TextView? = itemView.findViewById(R.id.okLocation)
        var tvLocation: TextView? = itemView.findViewById(R.id.tvLocation)
        var etLocation: TextInputLayout? = itemView.findViewById(R.id.etLocation)

        var okMortgageStatus: TextView? = itemView.findViewById(R.id.okMortgageStatus)
        var tvMortgageStatus: TextView? = itemView.findViewById(R.id.tvMortgageStatus)
        var pickerMortgage: TextView? = itemView.findViewById(R.id.pickerMortgage)

        var okMortgage1: TextView? = itemView.findViewById(R.id.okMortgage1)
        var tvMortgage1: TextView? = itemView.findViewById(R.id.tvMortgage1)
        var etMortgage1: TextInputLayout? = itemView.findViewById(R.id.etMortgage1)

        var okMortgage2: TextView? = itemView.findViewById(R.id.okMortgage2)
        var tvMortgage2: TextView? = itemView.findViewById(R.id.tvMortgage2)
        var etMortgage2: TextInputLayout? = itemView.findViewById(R.id.etMortgage2)

        var groupMortgages: Group? = itemView.findViewById(R.id.group_mortgages)
        //------------------------------------------------------
        var okBrand: TextView? = itemView.findViewById(R.id.okBrand)
        var tvBrand: TextView? = itemView.findViewById(R.id.tvBrand)
        var etBrand: TextInputLayout? = itemView.findViewById(R.id.etBrand)

        var okColor: TextView? = itemView.findViewById(R.id.okColor)
        var tvColor: TextView? = itemView.findViewById(R.id.tvColor)
        var etColor: TextInputLayout? = itemView.findViewById(R.id.etColor)

        var okLicense: TextView? = itemView.findViewById(R.id.okLicense)
        var tvLicense: TextView? = itemView.findViewById(R.id.tvLicense)
        var etLicense: TextInputLayout? = itemView.findViewById(R.id.etLicense)


        //------------------------------------------------------
        var removeIcon: TextView? = itemView.findViewById(R.id.removeIcon)
        var groupAll: Group? = itemView.findViewById(R.id.groupAll)
    }
}



