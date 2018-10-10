package com.apm29.yjw.demo.ui.dialog

import android.content.Context
import android.text.TextUtils
import androidx.appcompat.app.AlertDialog
import com.apm29.yjw.gentleloansdemo.R
import com.yanzhenjie.permission.Permission
import com.yanzhenjie.permission.Rationale
import com.yanzhenjie.permission.RequestExecutor


class RuntimeRationale : Rationale<List<String>> {

    override fun showRationale(context: Context, permissions: List<String>, executor: RequestExecutor) {
        val permissionNames = Permission.transformText(context, permissions)
        val message = context.getString(R.string.message_permission_rationale, TextUtils.join("\n", permissionNames))

        AlertDialog.Builder(context)
                .setCancelable(false)
                .setTitle(R.string.title_permission_dialog)
                .setMessage(message)
                .setPositiveButton(R.string.resume) { dialog, which -> executor.execute() }
                .setNegativeButton(R.string.cancel) { dialog, which -> executor.cancel() }
                .show()
    }
}