package com.apm29.yjw.demo.ui.dialog


import androidx.appcompat.app.AlertDialog
import com.apm29.yjw.demo.app.ActivityManager
import com.apm29.yjw.demo.app.AppApplication
import com.apm29.yjw.gentleloansdemo.R
import java.lang.Exception

object LoadingDialog {
    var dialog: AlertDialog? = null
    val title = AppApplication.context.getString(R.string.title_warning_dialog)
    val error = AppApplication.context.getString(R.string.app_loading_text)

    fun getInstance(): AlertDialog? {

        synchronized (this){
            if (dialog == null) {
               synchronized(this){
                   ActivityManager.getHostActivity()?.let {
                       dialog = AlertDialog.Builder(it)
                               .setTitle(title)
                               .setMessage(error)
                               .create()
                   }
               }
            }
        }
        return dialog!!
    }

    fun show(){
        try {
            getInstance()
            dialog?.show()
        }catch (e:Exception){
            synchronized(this){
                ActivityManager.getHostActivity()?.let {
                    dialog = AlertDialog.Builder(it)
                            .setTitle(title)
                            .setMessage(error)
                            .create()
                }
            }
        }
    }

    fun dismiss(){
        dialog?.dismiss()
    }
}
