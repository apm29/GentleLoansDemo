package com.apm29.yjw.demo.ui.form.information

import android.os.Bundle
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.RelativeSizeSpan
import android.text.style.SuperscriptSpan
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.apm29.yjw.demo.arch.BaseFragment
import com.apm29.yjw.demo.di.component.AppComponent
import com.apm29.yjw.demo.di.component.DaggerDefaultFragmentComponent
import com.apm29.yjw.demo.di.module.DefaultFragmentModule
import com.apm29.yjw.demo.model.Car
import com.apm29.yjw.demo.model.Estate
import com.apm29.yjw.demo.ui.form.register.FamilyAssetsFragment
import com.apm29.yjw.demo.ui.list.adapter.AssetsAdapter
import com.apm29.yjw.demo.utils.disableAll
import com.apm29.yjw.demo.utils.mortgageList
import com.apm29.yjw.demo.utils.setupOneOptPicker
import com.apm29.yjw.demo.utils.showToast
import com.apm29.yjw.demo.viewmodel.InformationFormViewModel
import com.apm29.yjw.gentleloansdemo.R
import kotlinx.android.synthetic.main.family_assets_fragment.*

class AssetsInfoFromFragment:BaseFragment<InformationFormViewModel>() {
    override fun setupViewLayout(savedInstanceState: Bundle?): Int {
        return R.layout.assets_info_form_fragment
    }

    override fun setupModel(appComponent: AppComponent) {
        DaggerDefaultFragmentComponent.builder()
                .appComponent(appComponent)
                .defaultFragmentModule(DefaultFragmentModule(this))
                .build()
                .inject(this)
    }

    override fun setupViews(savedInstanceState: Bundle?) {
        swipeRefresh.setOnRefreshListener {
            mViewModel.assets()
        }

        btnSave.setOnClickListener {
            showToast("saved:\n ${estateAdapter.list.size} \n ${carAdapter.list.size}")
        }
    }

    override fun initData(savedInstanceState: Bundle?) {
        mViewModel.assets()

        mViewModel.assetsData.observe(this, Observer {
            setupList(it.first,it.second)
        })
    }
    private fun bindEstateOperation(holder: FamilyAssetsFragment.AssetsHolder, estate: Estate, position: Int) {
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
        stringBuilder.setSpan( SuperscriptSpan(),1,2,SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE )
        stringBuilder.setSpan( RelativeSizeSpan(0.5f),1,2, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE )
        holder.unitArea?.text = stringBuilder
        holder.itemView.disableAll()
    }




    private fun bindCarOperation(holder: FamilyAssetsFragment.AssetsHolder, car: Car, position: Int) {
        holder.tvEstateTitle?.text = getString(R.string.text_car)
        holder.itemView.disableAll()
    }

    lateinit var estateAdapter: AssetsAdapter<Estate>

    lateinit var carAdapter: AssetsAdapter<Car>

    private fun setupList(dataEstate:List<Estate>, dataCar:List<Car>) {
        estateAdapter = AssetsAdapter(
                editable = false,
                list = dataEstate,
                layoutRes = R.layout.estate_item_layout,
                clickAdd = {
                    tvEstateTitle.requestFocus()
                    Estate()
                },
                bindOp = ::bindEstateOperation
        )
        carAdapter = AssetsAdapter(
                editable = false,
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
}