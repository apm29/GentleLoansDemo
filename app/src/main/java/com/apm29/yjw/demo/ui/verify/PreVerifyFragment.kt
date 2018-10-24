package com.apm29.yjw.demo.ui.verify

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.apm29.yjw.demo.arch.BaseFragment
import com.apm29.yjw.demo.di.component.AppComponent
import com.apm29.yjw.demo.di.component.DaggerDefaultFragmentComponent
import com.apm29.yjw.demo.di.module.DefaultFragmentModule
import com.apm29.yjw.demo.model.FORM
import com.apm29.yjw.demo.model.MAIN
import com.apm29.yjw.demo.model.VerifyProgress
import com.apm29.yjw.demo.ui.verify.PreVerifyFragmentArgs
import com.apm29.yjw.demo.utils.navigateErrorHandled
import com.apm29.yjw.demo.utils.showToast
import com.apm29.yjw.demo.viewmodel.CommunicateViewModel
import com.apm29.yjw.demo.viewmodel.RealNameVerifyViewModel
import com.apm29.yjw.gentleloansdemo.R
import kotlinx.android.synthetic.main.pre_verify_fragment.*
import java.lang.IllegalArgumentException

class PreVerifyFragment : BaseFragment<RealNameVerifyViewModel>() {


    override fun setupViewLayout(savedInstanceState: Bundle?): Int {
        return R.layout.pre_verify_fragment
    }

    override fun setupModel(appComponent: AppComponent) {
        DaggerDefaultFragmentComponent.builder()
                .defaultFragmentModule(DefaultFragmentModule(this))
                .appComponent(appComponent)
                .build()
                .inject(this)
    }

    override fun setupViews(savedInstanceState: Bundle?) {
        val verifyProgress: VerifyProgress? = PreVerifyFragmentArgs.fromBundle(arguments).verifyProgress
        verifyProgress?.let {
            if (viewPagerVerify.adapter != null) {
                return
            }
            viewPagerVerify.adapter = object : FragmentPagerAdapter(childFragmentManager) {
                override fun getItem(position: Int): Fragment {
                    val clazz = verifyProgress.getPageType(position)
                    return when (clazz) {
                        YYSVerifyFragment::class.java -> YYSVerifyFragment()
                        RealNameVerifyFragment::class.java -> RealNameVerifyFragment()
                        else -> throw IllegalArgumentException("未知页面")
                    }
                }

                override fun getCount(): Int {
                    return verifyProgress.pageCount
                }

                override fun getPageTitle(position: Int): CharSequence? {
                    return verifyProgress.pageTitles[position]
                }
            }
        }
        hideToolBarArrow()

    }

    override fun initData(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        val activity = requireActivity()
        val viewModel = ViewModelProviders.of(activity).get(CommunicateViewModel::class.java)
        val verifyProgress: VerifyProgress? = PreVerifyFragmentArgs.fromBundle(arguments).verifyProgress
        val destination: Int? = PreVerifyFragmentArgs.fromBundle(arguments).destination
        verifyProgress?.let {

            val desAction =  when(destination){
                MAIN->R.id.action_preVerifyFragment_to_mainFragment
                FORM->R.id.action_preVerifyFragment_to_formListFragment
                else->throw  IllegalArgumentException("未知页面")
            }
            viewModel.yysResult.observe(this, Observer { event ->
                if (!event.hasBeenHandled && event.getContentIfNotHandled() == true) {

                    if (verifyProgress.pageCount > 1) {
                        viewPagerVerify.setCurrentItem(1, true)
                    } else {
                        findNavController().navigate(desAction)
                    }
                }
            })
            viewModel.realResult.observe(this, Observer { event ->
                if (!event.hasBeenHandled && event.getContentIfNotHandled() == true) {
                    findNavController().navigate(desAction)
                }
            })
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.app_bar_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == R.id.toLogin) {
            findNavController().navigate(R.id.action_global_loginFragment)
            return true
        }
        return super.onOptionsItemSelected(item)
    }


}