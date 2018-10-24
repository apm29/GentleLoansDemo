package com.apm29.yjw.demo.ui.splash

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.TextWatcher
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.ViewCompat
import androidx.lifecycle.Observer
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.navOptions
import com.apm29.yjw.demo.arch.BaseFragment
import com.apm29.yjw.demo.arch.user.UserManager
import com.apm29.yjw.demo.di.component.AppComponent
import com.apm29.yjw.demo.di.component.DaggerDefaultFragmentComponent
import com.apm29.yjw.demo.di.module.DefaultFragmentModule
import com.apm29.yjw.demo.model.ProfileBean
import com.apm29.yjw.demo.model.VerifyProgress
import com.apm29.yjw.demo.ui.verify.PreVerifyFragmentArgs
import com.apm29.yjw.demo.utils.getTextOrEmpty
import com.apm29.yjw.demo.utils.navigateErrorHandled
import com.apm29.yjw.demo.utils.showToast
import com.apm29.yjw.demo.viewmodel.DefaultFragmentViewModel
import com.apm29.yjw.gentleloansdemo.BuildConfig
import com.apm29.yjw.gentleloansdemo.R
import kotlinx.android.synthetic.main.login_fragment.*

class LoginFragment : BaseFragment<DefaultFragmentViewModel>() {

    override var observingError: Boolean = true

    override fun setupViewLayout(savedInstanceState: Bundle?): Int {
        return R.layout.login_fragment
    }

    override fun setupModel(appComponent: AppComponent) {
        DaggerDefaultFragmentComponent.builder()
                .defaultFragmentModule(DefaultFragmentModule(this))
                .appComponent(appComponent)
                .build()
                .inject(this)
    }

    override fun setupViews(savedInstanceState: Bundle?) {
        checkboxUserService.setOnCheckedChangeListener { _, isChecked ->
            btnLogin.isEnabled = isChecked
        }


        val stringBuilder = SpannableStringBuilder(textService.text)
        stringBuilder.setSpan(object : ClickableSpan() {
            override fun onClick(widget: View) {
                showToast("没做!")
            }
        },
                textService.text.indexOfFirst { it == '《' },
                textService.text.indexOfFirst { it == '》' } + 1,
                SpannableString.SPAN_INCLUSIVE_INCLUSIVE
        )
        textService.text = stringBuilder
        textService.movementMethod = LinkMovementMethod.getInstance()
        textService.highlightColor = Color.DKGRAY
        textInputMobile.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                textInputLayoutMobile.error = null
            }
        })
        tvSend.setOnClickListener {
            doSendSMS()
        }
        textInputMobile.setOnEditorActionListener { view, actionId, action ->
            judgeIMEAction(view, actionId, action)
        }
        textInputPass.setOnEditorActionListener { view, actionId, action ->
            judgeIMEAction(view, actionId, action)
        }
        btnLogin.setOnClickListener {
            doLogin()
        }
        //api21 以下VectorDrawable在xml中不可用,需要手动代码设置
        textInputLayoutPass.passwordVisibilityToggleDrawable = AppCompatResources.getDrawable(requireContext(), R.drawable.selector_password_toggle)
        if (Build.VERSION.SDK_INT < 21) {
            imageView2.setImageDrawable(AppCompatResources.getDrawable(requireContext(), R.drawable.bg_gradient_primary2dark))
        }
        //从sp中获取上次登录的手机
        textInputMobile.setText(UserManager.retrieveUserMobile())
    }

    override fun initData(savedInstanceState: Bundle?) {
        mViewModel.smsResult.observe(this, Observer {
            //showToast(msg = it.msg)
            //因为DefaultViewModel绑定的HostActivity的生命周期,基本上是不会注销的,所以判断是否已经数据过期
            if (it.success() && !it.isDataExpired()) {
                tvSend.count(60)
                if (BuildConfig.DEBUG) {
                    textInputPass.setText(getString(R.string.default_debug_sms))
                }
                it.expireData()
            } else if (!it.success()) {
                textInputLayoutMobile.error = it.msg
            }
        })

        mViewModel.loginResult.observe(this, Observer {
            //showToast(msg = it.msg)
            if (it.success() && !it.isDataExpired()) {
                it?.getDataIfNotExpired()?.let { data -> UserManager.login(data) }
                UserManager.saveUserMobile(textInputMobile.getTextOrEmpty())
                mViewModel.profileVerify()
            } else if (!it.success()) {
                textInputLayoutPass.error = it.msg
            }
        })

        mViewModel.profile.observe(this, Observer {
            if (it.success() && !it.isDataExpired()) {
                navigateToVerify(it.peekData())
            }
        })
    }


    private fun navigateToVerify(profile: ProfileBean) {
        val transitionName = getString(R.string.app_icon)
        val extras = FragmentNavigatorExtras(
                imageViewLogo to transitionName
        )
        ViewCompat.setTransitionName(imageViewLogo, transitionName)
        if (profile.is_real && profile.yys_auth) {

            navigateErrorHandled(
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
            navigateErrorHandled(
                    R.id.preVerifyFragment,
                    preVerifyFragmentArgs,
                    navOptions {
                        clearTask = true
                    },
                    extras
            )
        }
    }


    private fun judgeIMEAction(view: TextView?, actionId: Int, action: KeyEvent?): Boolean {
        println("view = [$view], actionId = [$actionId], action = [$action]")
        return if (actionId == resources.getInteger(R.integer.action_id_login)
                || actionId == EditorInfo.IME_ACTION_GO
                || actionId == EditorInfo.IME_ACTION_DONE
        ) {
            doLogin()
            true
        } else {
            false
        }
    }

    private fun doLogin() {
        textInputLayoutPass.error = null
        mViewModel.doLogin(textInputMobile.getTextOrEmpty(), textInputPass.getTextOrEmpty())
    }

    private fun doSendSMS() {
        textInputLayoutMobile.error = null
        mViewModel.sendLoginVerifySMS(textInputMobile.getTextOrEmpty())
    }

}