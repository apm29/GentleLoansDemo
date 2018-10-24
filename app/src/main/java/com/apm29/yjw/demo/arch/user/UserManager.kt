package com.apm29.yjw.demo.arch.user

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.core.content.edit
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.Navigator
import androidx.navigation.navOptions
import com.apm29.yjw.demo.app.AppApplication
import com.apm29.yjw.demo.model.LoginBean
import com.apm29.yjw.gentleloansdemo.R

object UserManager : UserLifecycle {
    private val sharedPreferences: SharedPreferences = AppApplication.context.getSharedPreferences("UserInfo", Context.MODE_PRIVATE)

    private const val ACCESS_TOKEN = "access_token"
    private const val USER_MOBILE = "user_mobile"

    init {
        sharedPreferences.getString(ACCESS_TOKEN, null)?.let { token ->
            currentUser = LoginBean(
                    token,
                    UserType.Common
            )
        }
    }

    override val loginDestination: NavDirections
        get() = object : NavDirections {
            override fun getArguments(): Bundle? {
                return bundleOf()
            }

            override fun getActionId(): Int {
                return R.id.loginFragment
            }
        }
    override val logoutDestination: NavDirections
        get() = object : NavDirections {
            override fun getArguments(): Bundle? {
                return bundleOf()
            }

            override fun getActionId(): Int {
                return R.id.loginFragment
            }

        }


    override fun isUserLogin(): Boolean {
        return currentUser != null
    }


    override fun login(userLoginInfo: UserLoginInfo): Boolean {
        currentUser = userLoginInfo

        sharedPreferences.edit (commit = true){
            putString(ACCESS_TOKEN, currentUser?.accessToken)
        }
        return currentUser != null
    }

    override fun logout(): Boolean {
        currentUser = null
        sharedPreferences.edit(commit = true) {
            putString(ACCESS_TOKEN, null)
        }
        return currentUser == null
    }

    override fun toLogin(navController: NavController, navExtras: Navigator.Extras?) {
        navController.navigate(R.id.loginFragment,null, navOptions {
            this.anim {
                this.enter = R.anim.slide_in_bottom
                this.exit = R.anim.nav_default_exit_anim
                this.popEnter = R.anim.nav_default_pop_exit_anim
                this.popExit = R.anim.slide_out_bottom
            }
            this.clearTask = true
        },navExtras)
    }

    override fun toLogout(navController: NavController) {
        navController.navigate(loginDestination, navOptions {
            this.anim {
                this.enter = R.anim.slide_in_bottom
                this.exit = R.anim.nav_default_exit_anim
                this.popEnter = R.anim.nav_default_pop_exit_anim
                this.popExit = R.anim.slide_out_bottom
            }
            popUpTo(R.id.mainFragment){
                inclusive = true
            }
            this.clearTask = true
        })
    }

    override var currentUser: UserLoginInfo? = null



    fun saveUserMobile(mobile:String){
        sharedPreferences.edit (commit = true){
            putString(USER_MOBILE,mobile)
        }
    }

    fun retrieveUserMobile():String?{
        return sharedPreferences.getString(USER_MOBILE,null)
    }
}