package com.apm29.yjw.demo.ui.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.apm29.yjw.gentleloansdemo.R

class WarningDialog : DialogFragment() {
    companion object {
        var instance:WarningDialog? = null

        fun getInstance(title: String, error: String): WarningDialog {

            if (instance == null) {
                instance = warningDialog(title, error)
            }else{
                instance?.arguments = bundleOf(
                        TITLE to title,
                        ERROR to error
                )
            }
            return instance?: warningDialog(title, error)
        }

        @JvmName("get_instance")
        fun getInstance():WarningDialog?{
            return instance
        }

        private fun warningDialog(title: String, error: String): WarningDialog {
            return WarningDialog().also {
                val bundle = Bundle()
                bundle.putString(TITLE, title)
                bundle.putString(ERROR, error)
                it.arguments = bundle
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NO_TITLE,R.style.WarningDialogTheme)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val inflate = inflater.inflate(R.layout.dialog_warning, container, false)
        val tvTitle = inflate.findViewById<TextView>(R.id.tvTitle)
        val tvContent = inflate.findViewById<TextView>(R.id.tvContent)
        tvTitle.text = arguments?.getString(TITLE)?:""
        tvContent.text = arguments?.getString(ERROR)?:""
        return inflate
    }

    fun show(fragmentManager: FragmentManager) {
        if (!isAdded) {
            show(fragmentManager, "WarningDialog")
        }
    }
}

private const val TITLE = "title"
private const val ERROR = "mErrorData"