package com.apm29.yjw.demo.ui.splash

import android.os.Bundle
import android.text.TextUtils
import android.view.animation.ScaleAnimation
import androidx.appcompat.app.AlertDialog
import androidx.core.view.ViewCompat
import androidx.lifecycle.Observer
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import com.apm29.yjw.demo.arch.BaseFragment
import com.apm29.yjw.demo.arch.UserManager
import com.apm29.yjw.demo.di.component.AppComponent
import com.apm29.yjw.demo.di.component.DaggerDefaultFragmentComponent
import com.apm29.yjw.demo.di.module.DefaultFragmentModule
import com.apm29.yjw.demo.model.ProfileBean
import com.apm29.yjw.demo.model.VerifyProgress
import com.apm29.yjw.demo.ui.dialog.RuntimeRationale
import com.apm29.yjw.demo.ui.verify.PreVerifyFragmentArgs
import com.apm29.yjw.demo.viewmodel.DefaultFragmentViewModel
import com.apm29.yjw.gentleloansdemo.R
import com.yanzhenjie.permission.AndPermission
import com.yanzhenjie.permission.Permission
import kotlinx.android.synthetic.main.splash_fragment.*
import kotlinx.coroutines.experimental.GlobalScope
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.delay


class SplashFragment : BaseFragment<DefaultFragmentViewModel>() {
    override fun setupViewLayout(savedInstanceState: Bundle?): Int {
        return R.layout.splash_fragment
    }

    override fun setupModel(appComponent: AppComponent) {
        DaggerDefaultFragmentComponent.builder()
                .defaultFragmentModule(DefaultFragmentModule(this))
                .appComponent(appComponent)
                .build()
                .inject(this)
    }

    override fun setupViews(savedInstanceState: Bundle?) {
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
                navigateToVerify(it.peekData())
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
        if (profile.yys_auth && profile.is_real) {
            findNavController().navigate(
                    R.id.mainFragment,
                    null,
                    navOptions {
                        clearTask = true
                    },
                    extras
            )
        } else {
            val preVerifyFragmentArgs = PreVerifyFragmentArgs.Builder()
                    .setVerifyProgress(VerifyProgress(profile.is_real, profile.yys_auth))
                    .build()
                    .toBundle()
            findNavController().navigate(
                    R.id.preVerifyFragment,
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
                .permission(
                        Permission.READ_PHONE_STATE,
                        Permission.CAMERA,
                        Permission.READ_CONTACTS,
                        Permission.ACCESS_FINE_LOCATION,
                        Permission.WRITE_EXTERNAL_STORAGE
                )
                .onGranted {
                    GlobalScope.async {
                        delay(2000)
                        mViewModel.profileVerify(false)
                    }
                    //showToast("permission granted : ${it.joinToString(",")}")
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
                                                requestAllPermissions()
                                            }
                                            .start()
                                } else {
                                    requestAllPermissions()
                                }
                            }
                            .setNegativeButton(R.string.cancel) { _, _ ->
                                System.exit(0)
                            }
                            .show()

                }
                .rationale(RuntimeRationale())
                .start()
    }


}