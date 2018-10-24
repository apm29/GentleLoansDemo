package com.apm29.yjw.demo.third

import androidx.navigation.NavController
import com.apm29.yjw.demo.model.INDEX
import com.apm29.yjw.demo.model.INFO
import com.apm29.yjw.demo.model.LOAN_RECORD
import com.apm29.yjw.demo.model.PushMessage
import com.apm29.yjw.gentleloansdemo.R

object PushMessageHandler {
    fun handleMessage(navController: NavController?,message: PushMessage){
        when(message.jump){
            LOAN_RECORD->{
                navController?.navigate(R.id.loanLogListFragment)
            }
            INFO,INDEX-> {
                navController?.navigate(R.id.registerFormFragment)
            }
            else->{

            }
        }
    }

    fun markMessageAsRead(data: PushMessage) {

    }
}