package com.apm29.yjw.demo.arch.user

import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.Navigator

interface UserLifecycle {
    var currentUser: UserLoginInfo?
    val loginDestination: NavDirections
    val logoutDestination: NavDirections
    fun isUserLogin(): Boolean
    fun login(userLoginInfo: UserLoginInfo): Boolean
    fun logout(): Boolean
    fun toLogin(navController: NavController, navExtras:Navigator.Extras? = null )
    fun toLogout(navController: NavController)
}

