package com.apm29.yjw.demo.ui.list.base

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.apm29.yjw.demo.arch.BaseFragment
import com.apm29.yjw.demo.viewmodel.DefaultListViewModel
import com.apm29.yjw.gentleloansdemo.R
import com.paginate.Paginate
import kotlinx.android.synthetic.main.base_list_fragment.*

abstract class BaseListFragment<T, VH : RecyclerView.ViewHolder, ADAPTER : BaseEmptyAdapter<T, VH>> : BaseFragment<DefaultListViewModel>() {
    override fun setupViewLayout(savedInstanceState: Bundle?): Int {
        return R.layout.base_list_fragment
    }


    val data = arrayListOf<T>()
    var adapter: ADAPTER = this.setupAdapter()
    lateinit var paginate:Paginate
    override fun setupViews(savedInstanceState: Bundle?) {

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)


        val callbacks = object : Paginate.Callbacks {
            override fun isLoading(): Boolean {
                return mViewModel.mLoadingData.value ?: false
            }

            override fun hasLoadedAllItems(): Boolean {
                return mViewModel.hasLoadAll
            }

            override fun onLoadMore() {
                load(false, data)
            }

        }

        refreshLayout.setOnRefreshListener {
            load(true, data)
        }
        paginate = Paginate.with(recyclerView, callbacks)
                .setLoadingTriggerThreshold(0)
                .setLoadingListItemCreator(DefaultLoadMreCreator())
                .build()
        paginate.setHasMoreDataToLoad(true)


        load(true, data)
    }

    abstract fun setupAdapter(): ADAPTER

    abstract fun load(refresh: Boolean, lists: ArrayList<T>)

    override fun initData(savedInstanceState: Bundle?) {
        mViewModel.listChangePositionData.observe(this, Observer {
            if (it.first < it.second) {
                adapter.notifyItemRangeChanged(it.first, it.second)
            } else if (it.first > it.second) {
                adapter.notifyItemRangeRemoved(it.second, it.first)
            }
        })
    }

    override fun hideLoading() {
        super.hideLoading()
        refreshLayout.isRefreshing = false
        paginate.setHasMoreDataToLoad(false)
    }
}