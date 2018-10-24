package com.apm29.yjw.demo.di.component

import com.apm29.yjw.demo.di.module.DefaultFragmentModule
import com.apm29.yjw.demo.di.scope.ActivityScope
import com.apm29.yjw.demo.ui.dialog.ImageEditFragment
import com.apm29.yjw.demo.ui.form.information.*
import com.apm29.yjw.demo.ui.form.register.ApplicantInfoFragment
import com.apm29.yjw.demo.ui.form.register.FamilyAssetsFragment
import com.apm29.yjw.demo.ui.form.register.RegisterFormFragment
import com.apm29.yjw.demo.ui.loan.LoanDetailFragment
import com.apm29.yjw.demo.ui.list.LoanLogListFragment
import com.apm29.yjw.demo.ui.loan.LoanRepaymentRecordFragment
import com.apm29.yjw.demo.ui.loan.LoanRepaymentScheduleFragment
import com.apm29.yjw.demo.ui.main.*
import com.apm29.yjw.demo.ui.main.MainFragment
import com.apm29.yjw.demo.ui.splash.LoginFragment
import com.apm29.yjw.demo.ui.splash.SplashFragment
import com.apm29.yjw.demo.ui.verify.PreVerifyFragment
import com.apm29.yjw.demo.ui.verify.RealNameVerifyFragment
import com.apm29.yjw.demo.ui.verify.WebViewFragment
import com.apm29.yjw.demo.ui.verify.YYSVerifyFragment
import dagger.Component


/**
 * 默认的公用component,
 * 需要注入的类,添加对应的inject方法,注入DefaultModule和APPComponent
 */
@ActivityScope
@Component(
        modules = [DefaultFragmentModule::class], dependencies = [AppComponent::class]
)
interface DefaultFragmentComponent {
    fun inject(any: MainFragment)
    fun inject(any: HomeFragment)
    fun inject(any: MineFragment)
    fun inject(any: RealNameVerifyFragment)
    fun inject(loginFragment: LoginFragment)
    fun inject(preVerifyFragment: PreVerifyFragment)
    fun inject(yysVerifyFragment: YYSVerifyFragment)
    fun inject(webViewFragment: WebViewFragment)
    fun inject(splashFragment: SplashFragment)
    fun inject(registerFormFragment: RegisterFormFragment)
    fun inject(any:ApplicantInfoFragment)
    fun inject(familyAssetsFragment: FamilyAssetsFragment)
    fun inject(loanLogListFragment: LoanLogListFragment)
    fun inject(personalInfoFragment: PersonalInfoFragment)
    fun inject(aboutUsFragment: AboutUsFragment)
    fun inject(applicantInfoManagerFragment: ApplicantInfoManagerFragment)
    fun inject(personalInfoFromFragment: PersonalInfoFromFragment)
    fun inject(contactsInfoFormFragment: ContactsInfoFormFragment)
    fun inject(loanUsageInfoFromFragment: LoanUsageInfoFromFragment)
    fun inject(assetsInfoFromFragment: AssetsInfoFromFragment)
    fun inject(jobInfoFromFragment: JobInfoFromFragment)
    fun inject(imageInfoFromFragment: ImageInfoFromFragment)
    fun inject(imageEditFragment: ImageEditFragment)
    fun inject(loanDetailFragment: LoanDetailFragment)
    fun inject(loanScheduleFragment: LoanRepaymentScheduleFragment)
    fun inject(loanRepaymentRecordFragment: LoanRepaymentRecordFragment)
}