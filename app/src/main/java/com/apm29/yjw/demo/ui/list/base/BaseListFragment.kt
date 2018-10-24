package com.apm29.yjw.demo.ui.list.base

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.apm29.yjw.demo.arch.BaseFragment
import com.apm29.yjw.demo.viewmodel.DefaultListViewModel
import com.apm29.yjw.gentleloansdemo.R
import com.paginate.Paginate

abstract class BaseListFragment<T, VH : RecyclerView.ViewHolder, ADAPTER : BaseEmptyAdapter<T, VH>> : BaseFragment<DefaultListViewModel>() {

    override var observingLoading: Boolean = false
    override fun setupViewLayout(savedInstanceState: Bundle?): Int {
        return if (useToolBar) R.layout.base_list_fragment else R.layout.base_list_no_title_fragment
    }

    open val useToolBar = true
    val data = arrayListOf<T>()
    var adapter: ADAPTER = this.setupAdapter()
    private lateinit var paginate: Paginate

    private var lastOffset = 0
    private var lastPosition = 0
    /**
     * 记录RecyclerView当前位置
     */
    private fun getPositionAndOffset(recyclerView: RecyclerView) {
        val layoutManager = recyclerView.layoutManager as LinearLayoutManager
        //获取可视的第一个view
        val topView = layoutManager.getChildAt(0)
        if (topView != null) {
            //获取与该view的顶部的偏移量
            lastOffset = topView.top
            //得到该View的数组位置
            lastPosition = layoutManager.getPosition(topView)
        }
    }

    /**
     * 让RecyclerView滚动到指定位置
     */
    private fun scrollToPosition(recyclerView: RecyclerView) {
        if (recyclerView.layoutManager != null && lastPosition >= 0) {
            (recyclerView.layoutManager as LinearLayoutManager).scrollToPositionWithOffset(lastPosition, lastOffset)
        }
    }


    override fun setupViews(savedInstanceState: Bundle?) {
        val refreshLayout = view?.findViewById<SwipeRefreshLayout>(R.id.refreshLayout)
        val recyclerView = view?.findViewById<RecyclerView>(R.id.recyclerView)
        refreshLayout?.setOnRefreshListener {
            load(true, data)
        }
        recyclerView?.apply {
            this.adapter = this@BaseListFragment.adapter
            this.layoutManager = LinearLayoutManager(context)


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
            paginate = Paginate.with(recyclerView, callbacks)
                    .setLoadingTriggerThreshold(0)
                    .setLoadingListItemCreator(DefaultLoadMreCreator())
                    .build()
            paginate.setHasMoreDataToLoad(!mViewModel.hasLoadAll)
            if (data.isEmpty()) {
                load(true, data)
            }

            scrollToPosition(this)
        }
    }

    abstract fun setupAdapter(): ADAPTER

    abstract fun load(refresh: Boolean, lists: ArrayList<T>)

    override fun initData(savedInstanceState: Bundle?) {
        mViewModel.listChangePositionData.observe(this, Observer {
            if (it.first < it.second) {
                adapter.notifyItemRangeChanged(it.first, it.second)
            } else if (it.first > it.second) {
                adapter.notifyItemRangeRemoved(it.second, it.first)
            } else {
                paginate.setHasMoreDataToLoad(false)
            }
        })
        mViewModel.mLoadingData.observe(this, Observer {
            val refreshLayout = view?.findViewById<SwipeRefreshLayout>(R.id.refreshLayout)
            refreshLayout?.isRefreshing = it
        })
    }

    override fun hideLoading() {
        super.hideLoading()
        val refreshLayout = view?.findViewById<SwipeRefreshLayout>(R.id.refreshLayout)
        refreshLayout?.isRefreshing = false
        paginate.setHasMoreDataToLoad(false)
    }
}