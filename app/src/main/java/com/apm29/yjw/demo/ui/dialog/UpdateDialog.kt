package com.apm29.yjw.demo.ui.dialog

import android.annotation.SuppressLint
import com.tencent.bugly.beta.download.DownloadTask
import android.os.Bundle
import android.text.format.Formatter
import android.view.LayoutInflater
import android.widget.TextView
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.DialogFragment
import com.apm29.yjw.demo.utils.findHostNavController
import com.apm29.yjw.gentleloansdemo.R
import com.tencent.bugly.beta.Beta
import com.tencent.bugly.beta.download.DownloadListener
import java.text.SimpleDateFormat
import java.util.*


class UpdateDialog : DialogFragment() {
    private var tv: TextView? = null
    private var title: TextView? = null
    private var version: TextView? = null
    private var size: TextView? = null
    private var content: TextView? = null
    private var newFeature: TextView? = null
    private var cancel: TextView? = null
    private var start: TextView? = null
    @SuppressLint("SetTextI18n")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.dialog_update_app, container, false)
        tv = view.findViewById(R.id.tv)
        title = view.findViewById(R.id.title)
        version = view.findViewById(R.id.version)
        size = view.findViewById(R.id.size)
        content = view.findViewById(R.id.content)
        newFeature = view.findViewById(R.id.tvUpdateFeature)
        cancel = view.findViewById(R.id.cancel)
        start = view.findViewById(R.id.start)

        /*获取下载任务，初始化界面信息*/
        updateBtn(Beta.getStrategyTask())
        tv?.text = "已下载:" +Formatter.formatFileSize(requireContext(),Beta.getStrategyTask().savedLength)

        /*获取策略信息，初始化界面信息*/
        title?.text = Beta.getUpgradeInfo().title
        version?.text = "版本号:"+ Beta.getUpgradeInfo().versionName
        size?.text = "APK大小:" + Formatter.formatFileSize(requireContext(),Beta.getUpgradeInfo().fileSize)
        content?.text = "更新时间:" + SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss", Locale.getDefault()).format( Date(Beta.getUpgradeInfo().publishTime))
        newFeature?.text = Beta.getUpgradeInfo().newFeature
        /*为下载按钮设置监听*/
        start?.setOnClickListener {

            val task = Beta.startDownload()
            updateBtn(task)
            if (task.status == DownloadTask.DOWNLOADING) {
                finish()
            }
            
        }

        /*为取消按钮设置监听*/
        cancel?.setOnClickListener {
            Beta.cancelDownload()
            finish()
        }

        /*注册下载监听，监听下载事件*/
        Beta.registerDownloadListener(object : DownloadListener {
            override fun onReceive(task: DownloadTask) {
                updateBtn(task)
                tv?.text = task.savedLength.toString()
            }

            override fun onCompleted(task: DownloadTask) {
                updateBtn(task)
                tv?.text = task.savedLength.toString()
            }

            override fun onFailed(task: DownloadTask, code: Int, extMsg: String) {
                updateBtn(task)
                tv?.text = "失败"

            }
        })

        return view
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NO_FRAME,R.style.WarningDialogTheme)
        isCancelable = false
    }

    private fun finish() {
       dismiss()
    }


    override fun onDestroy() {
        super.onDestroy()

        /*注销下载监听*/
        Beta.unregisterDownloadListener()
    }


    fun updateBtn(task: DownloadTask) {

        /*根据下载任务状态设置按钮*/
        when (task.status) {
            DownloadTask.INIT, DownloadTask.DELETED, DownloadTask.FAILED -> {
                start?.text = "开始下载"
            }
            DownloadTask.COMPLETE -> {
                start?.text = "安装"
            }
            DownloadTask.DOWNLOADING -> {
                start?.text = "暂停"
            }
            DownloadTask.PAUSED -> {
                start?.text = "继续下载"
            }
        }
    }

}