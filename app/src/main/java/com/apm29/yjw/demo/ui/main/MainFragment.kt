package com.apm29.yjw.demo.ui.main

import android.Manifest
import android.os.Bundle
import android.provider.ContactsContract
import android.text.TextUtils
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.navigation.ui.setupWithNavController
import com.apm29.yjw.demo.arch.BaseFragment
import com.apm29.yjw.demo.di.component.AppComponent
import com.apm29.yjw.demo.di.component.DaggerDefaultFragmentComponent
import com.apm29.yjw.demo.di.module.DefaultFragmentModule
import com.apm29.yjw.demo.model.PushMessage
import com.apm29.yjw.demo.third.PushMessageHandler
import com.apm29.yjw.demo.ui.dialog.RuntimeRationale
import com.apm29.yjw.demo.utils.findHostNavController
import com.apm29.yjw.demo.utils.navigateErrorHandled
import com.apm29.yjw.demo.utils.showToast
import com.apm29.yjw.demo.viewmodel.CommunicateViewModel
import com.apm29.yjw.demo.viewmodel.DefaultFragmentViewModel
import com.apm29.yjw.demo.viewmodel.DefaultFragmentViewModel.Companion.PROJECTION
import com.apm29.yjw.gentleloansdemo.BuildConfig
import com.apm29.yjw.gentleloansdemo.R
import com.google.gson.Gson
import com.tencent.bugly.beta.Beta
import com.yanzhenjie.permission.AndPermission
import com.yanzhenjie.permission.Permission
import kotlinx.android.synthetic.main.main_fragment.*
import android.widget.Toast
import com.tencent.bugly.beta.upgrade.UpgradeStateListener
import android.content.Intent
import com.apm29.yjw.demo.app.AppApplication
import com.apm29.yjw.demo.ui.dialog.UpdateDialog
import com.tencent.bugly.beta.UpgradeInfo
import com.tencent.bugly.beta.upgrade.UpgradeListener



class MainFragment : BaseFragment<DefaultFragmentViewModel>() {

    override fun setupViewLayout(savedInstanceState: Bundle?): Int {
        return R.layout.main_fragment
    }

    override fun setupModel(appComponent: AppComponent) {
        DaggerDefaultFragmentComponent.builder()
                .appComponent(appComponent)
                .defaultFragmentModule(DefaultFragmentModule(this))
                .build()
                .inject(this)
    }

    override fun setupViews(savedInstanceState: Bundle?) {
        //NavigationUI.setupWithNavController(navigation,findNavController())
        navigation.setupWithNavController(Navigation.findNavController(requireActivity(), R.id.main_host_fragment))
    }

    override fun initData(savedInstanceState: Bundle?) {
        requestContactPermission()

        val communicateViewModel = ViewModelProviders.of(requireActivity()).get(CommunicateViewModel::class.java)
        communicateViewModel.pushJsonData.observe(requireActivity(), Observer {
            //推送点击统一处理
            it.getContentIfNotHandled()?.let {
                showToast(it)
                val message:PushMessage = Gson().fromJson(it,PushMessage::class.java)
                PushMessageHandler.handleMessage(findHostNavController(),message)
                PushMessageHandler.markMessageAsRead(message)
            }
        })

        communicateViewModel.mJumpData.observe(this, Observer {
            it.getContentIfNotHandled()?.let {
                navigateErrorHandled(it.id, it.args, it.navOptions, it.extras)
            }

        })
    }



    private fun requestContactPermission() {
        AndPermission.with(this)
                .runtime()
                .permission(Manifest.permission.READ_CONTACTS)
                .rationale(RuntimeRationale())
                .onGranted {
                    if (BuildConfig.DEBUG) {
                        hideLoading()
                        return@onGranted
                    }
                    val query = requireContext().contentResolver.query(ContactsContract.RawContacts.CONTENT_URI, PROJECTION,
                            null, null, null)
                    query?.let { _ ->
                        if (query.count <= 0) {
                            throw RuntimeException("没有联系人或者未获得授权")
                        }
                        if (query.moveToNext()) {
                            query.getString(0)
                        }
                    }
                    query?.close()
                    mViewModel.contact(requireContext().contentResolver)
                }
                .onDenied {
                    val permissionNames = Permission.transformText(context, it)
                    val message = requireActivity().getString(R.string.message_permission_rationale, TextUtils.join("\n", permissionNames))

                    AlertDialog.Builder(requireContext())
                            .setCancelable(false)
                            .setTitle(R.string.title_permission_dialog)
                            .setMessage(message)
                            .setPositiveButton(R.string.resume) { _, _ ->
                                if (AndPermission.hasAlwaysDeniedPermission(this, it)) {
                                    AndPermission.with(this)
                                            .runtime()
                                            .setting()
                                            .onComeback {
                                                // 用户从设置回来了。
                                                requestContactPermission()
                                            }
                                            .start()
                                } else {
                                    requestContactPermission()
                                }
                            }
                            .setNegativeButton(R.string.quit) { _, _ ->
                                System.exit(0)
                            }
                            .show()
                }
                .start()
    }


}
