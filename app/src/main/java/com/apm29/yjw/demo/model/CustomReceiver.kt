package com.apm29.yjw.demo.model

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProviders

import com.apm29.yjw.demo.ui.HostActivity

import org.json.JSONException
import org.json.JSONObject

import cn.jpush.android.api.JPushInterface
import com.apm29.yjw.demo.app.ActivityManager
import com.apm29.yjw.demo.viewmodel.CommunicateViewModel

/**
 * 自定义接收器
 *
 * 如果不定义这个 Receiver，则：
 * 1) 默认用户会打开主界面
 * 2) 接收不到自定义消息
 */
class CustomReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        try {
            val bundle = intent.extras
            Log.d(TAG, "[CustomReceiver] onReceive - " + intent.action + ", extras: " + printBundle(bundle!!))

            when {
                JPushInterface.ACTION_REGISTRATION_ID == intent.action -> {
                    val regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID)
                    Log.d(TAG, "[CustomReceiver] 接收Registration Id : " + regId!!)
                    //send the Registration Id to your server...

                }
                JPushInterface.ACTION_MESSAGE_RECEIVED == intent.action -> {
                    Log.d(TAG, "[CustomReceiver] 接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE)!!)
                    processCustomMessage(context, bundle)

                }
                JPushInterface.ACTION_NOTIFICATION_RECEIVED == intent.action -> {
                    Log.d(TAG, "[CustomReceiver] 接收到推送下来的通知")
                    val notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID)
                    Log.d(TAG, "[CustomReceiver] 接收到推送下来的通知的ID: $notifactionId")

                }
                JPushInterface.ACTION_NOTIFICATION_OPENED == intent.action -> {
                    Log.d(TAG, "[CustomReceiver] 用户点击打开了通知")

                    //打开自定义的Activity
                    onPushOpen(context, bundle)

                }
                JPushInterface.ACTION_RICHPUSH_CALLBACK == intent.action -> Log.d(TAG, "[CustomReceiver] 用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA)!!)
                //在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..
                JPushInterface.ACTION_CONNECTION_CHANGE == intent.action -> {
                    val connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false)
                    Log.w(TAG, "[CustomReceiver]" + intent.action + " connected state change to " + connected)
                }
                else -> Log.d(TAG, "[CustomReceiver] Unhandled intent - " + intent.action)
            }
        } catch (e: Exception) {

        }

    }

    private fun onPushOpen(context: Context, bundle: Bundle?) {
        val i = Intent(context, HostActivity::class.java)
        i.putExtras(bundle ?: bundleOf())
        val json = bundle?.getString(JPushInterface.EXTRA_EXTRA)
        //i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        //context.startActivity(i)
        if (!ActivityManager.containsActivity(HostActivity::class.java)) {
            context.startActivity(i)
        } else {
            //HostActivity is singleTask , it will be waked up from backstage
            val hostActivity = ActivityManager.getHostActivity()
            hostActivity?.let {
                context.startActivity(Intent(context,HostActivity::class.java))
                val communicateViewModel = ViewModelProviders.of(hostActivity).get(CommunicateViewModel::class.java)
                communicateViewModel.onPushClick(json)
            }
        }
    }

    //send msg to MainActivity
    private fun processCustomMessage(context: Context, bundle: Bundle) {

    }

    companion object {
        private val TAG = "JIGUANG-JKD"

        // 打印所有的 intent extra 数据
        private fun printBundle(bundle: Bundle): String {
            val sb = StringBuilder()
            for (key in bundle.keySet()) {
                if (key == JPushInterface.EXTRA_NOTIFICATION_ID) {
                    sb.append("\nkey:" + key + ", value:" + bundle.getInt(key))
                } else if (key == JPushInterface.EXTRA_CONNECTION_CHANGE) {
                    sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key))
                } else if (key == JPushInterface.EXTRA_EXTRA) {
                    if (TextUtils.isEmpty(bundle.getString(JPushInterface.EXTRA_EXTRA))) {
                        Log.i(TAG, "This message has no Extra data")
                        continue
                    }

                    try {
                        val json = JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA))
                        val it = json.keys()

                        while (it.hasNext()) {
                            val myKey = it.next()
                            sb.append("\nkey:" + key + ", value: [" +
                                    myKey + " - " + json.optString(myKey) + "]")
                        }
                    } catch (e: JSONException) {
                        Log.e(TAG, "Get message extra JSON error!")
                    }

                } else {
                    sb.append("\nkey:" + key + ", value:" + bundle.get(key))
                }
            }
            return sb.toString()
        }
    }
}
