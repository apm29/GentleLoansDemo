package com.apm29.yjw.demo.ui.splash

import android.os.Bundle
import android.view.animation.ScaleAnimation
import androidx.lifecycle.Observer
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import com.apm29.yjw.demo.arch.BaseFragment
import com.apm29.yjw.demo.arch.UserManager
import com.apm29.yjw.demo.di.component.AppComponent
import com.apm29.yjw.demo.di.module.DefaultActivityModule
import com.apm29.yjw.demo.viewmodel.DefaultActivityViewModel
import com.apm29.yjw.gentleloansdemo.R
import kotlinx.android.synthetic.main.splash_fragment.*
import kotlinx.coroutines.experimental.GlobalScope
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.delay
import com.yanzhenjie.permission.AndPermission
import android.text.TextUtils
import androidx.appcompat.app.AlertDialog
import androidx.core.view.ViewCompat
import androidx.navigation.navOptions
import com.apm29.yjw.demo.di.component.DaggerDefaultActivityComponent
import com.apm29.yjw.demo.model.ProfileBean
import com.apm29.yjw.demo.model.VerifyProgress
import com.apm29.yjw.demo.ui.dialog.RuntimeRationale
import com.apm29.yjw.demo.ui.main.PreVerifyFragmentArgs
import com.apm29.yjw.demo.utils.showToast
import com.yanzhenjie.permission.Permission


class SplashFragment : BaseFragment<DefaultActivityViewModel>() {
    override fun setupViewLayout(savedInstanceState: Bundle?): Int {
        return R.layout.splash_fragment
    }

    override fun setupModel(appComponent: AppComponent) {
        DaggerDefaultActivityComponent.builder()
                .defaultActivityModule(DefaultActivityModule(requireActivity()))
                .appComponent(appComponent)
                .build()
                .inject(this)
    }

    override fun setupViews(savedInstanceState: Bundle?) {
//        GlideApp.with(this)
//                .load("https://raw.githubusercontent.com/bumptech/glide/master/static/glide_logo.png")
//                .placeholder(R.mipmap.error)
//                .into(imageView)
        imageViewLogo.animation = ScaleAnimation(1.0f, 2.0f, 1.0f, 2.0f, ScaleAnimation.RELATIVE_TO_SELF, 0.5f, ScaleAnimation.RELATIVE_TO_SELF, 0.5f).apply {
            this.duration = 2000
            this.fillAfter = true
        }
        imageViewLogo.animate()
    }

    override fun initData(savedInstanceState: Bundle?) {
        requestAllPermissions()

        mViewModel.profile.observe(this, Observer {
            if (it != null) {
                navigateToVerify(it)
            }
        })
        mViewModel.mErrorData.observe(this, Observer {
            UserManager.toLogin(findNavController())
        })
    }

    private fun navigateToVerify(profile: ProfileBean) {
        val extras = FragmentNavigatorExtras(
                imageViewLogo to getString(R.string.app_icon)
        )
        ViewCompat.setTransitionName(imageViewLogo, getString(R.string.app_icon))
        if (profile.yys_auth&&profile.is_real) {
            val mainFragmentArgs = MainFragmentArgs.Builder()
                    .setIsReal(if (profile.is_real) 1 else 0)
                    .setYys(if (profile.yys_auth) 1 else 0)
                    .build().toBundle()
            findNavController().navigate(R.id.mainFragment, mainFragmentArgs, navOptions { clearTask = true }, extras)
        }else{
            val preVerifyFragmentArgs = PreVerifyFragmentArgs.Builder()
                    .setVerifyProgress(VerifyProgress(profile.is_real,profile.yys_auth))
                    .build()
                    .toBundle()
            findNavController().navigate(
                    R.id.action_splashFragment_to_preVerifyFragment,
                    preVerifyFragmentArgs,
                    navOptions {
                        clearTask = true
                    },
                    extras
            )

        }
    }

    private fun requestAllPermissions() {
        AndPermission.with(this)
                .runtime()
                .permission(Permission.Group.CAMERA, Permission.Group.CONTACTS, Permission.Group.LOCATION, Permission.Group.STORAGE)
                .onGranted {
                    GlobalScope.async {
                        delay(2000)
                        mViewModel.profileVerify()
                    }
                    showToast("permission granted : ${it.joinToString(",")}")
                }
                .onDenied {
                    showToast("permission denied : ${it.joinToString(",")}")
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
                                                showToast("come back from app setting :${it.joinToString(",")}")
                                                requestAllPermissions()
                                            }
                                            .start()
                                } else {
                                    requestAllPermissions()
                                }
                            }
                            .setNegativeButton(R.string.cancel) { _, _ ->
                                showToast("request canceled")
                                System.exit(0)
                            }
                            .show()

                }
                .rationale(RuntimeRationale())
                .start()
    }


}