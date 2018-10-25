package com.apm29.yjw.demo.ui.dialog

import android.content.res.Resources
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.apm29.yjw.demo.app.GlideApp
import com.apm29.yjw.demo.arch.BaseFragment
import com.apm29.yjw.demo.di.component.AppComponent
import com.apm29.yjw.demo.di.component.DaggerDefaultFragmentComponent
import com.apm29.yjw.demo.di.module.DefaultFragmentModule
import com.apm29.yjw.demo.model.KEY_DATA
import com.apm29.yjw.demo.model.KEY_POSITION
import com.apm29.yjw.demo.model.Photo
import com.apm29.yjw.demo.third.TextDrawable
import com.apm29.yjw.demo.utils.getColorCompat
import com.apm29.yjw.demo.utils.sp2Px
import com.apm29.yjw.demo.viewmodel.DefaultFragmentViewModel
import com.apm29.yjw.gentleloansdemo.R
import com.github.chrisbanes.photoview.PhotoView
import kotlinx.android.synthetic.main.assets_header.view.*
import kotlinx.android.synthetic.main.photo_fragment.*

class PhotoViewFragment :BaseFragment<DefaultFragmentViewModel>(){
    override fun setupViewLayout(savedInstanceState: Bundle?): Int {
        return R.layout.photo_fragment
    }

    override fun setupModel(appComponent: AppComponent) {
        DaggerDefaultFragmentComponent.builder()
                .appComponent(appComponent)
                .defaultFragmentModule(DefaultFragmentModule(this))
                .build()
                .inject(this)
    }

    override fun setupViews(savedInstanceState: Bundle?) {
        val list = arguments?.getParcelableArrayList<Photo>(KEY_DATA)
        val position:Int = arguments?.getInt(KEY_POSITION)?:0
        photoViewPager.adapter = object :PagerAdapter(){
            override fun isViewFromObject(view: View, `object`: Any): Boolean {
                return view === `object`
            }

            override fun getCount(): Int {
                return list?.size?:0
            }

            override fun instantiateItem(container: ViewGroup, position: Int): Any {
                val photoView = PhotoView(container.context)
                val layoutParams = ViewPager.LayoutParams()
                layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
                layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
                photoView.layoutParams = layoutParams
                GlideApp.with(container.context)
                        .load(list?.get(position)?.imageUrl)
                        .error(TextDrawable("图片未上传",getColorCompat(R.color.colorAccent),sp2Px(32f)))
                        .into(photoView)
                container.addView(photoView)
                return photoView
            }

            override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
                container.removeView(`object` as View)
            }
        }

        photoViewPager.setCurrentItem(position,false)
    }

    override fun onSaveInstanceState(outState: Bundle) {

    }

    override fun initData(savedInstanceState: Bundle?) {

    }
}