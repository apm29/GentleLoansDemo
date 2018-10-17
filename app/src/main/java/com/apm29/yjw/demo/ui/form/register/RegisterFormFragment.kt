package com.apm29.yjw.demo.ui.form.register

import android.app.AlertDialog
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import cn.fraudmetrix.octopus.aspirit.bean.OctopusParam
import cn.fraudmetrix.octopus.aspirit.main.OctopusManager
import com.apm29.yjw.demo.app.ActivityManager
import com.apm29.yjw.demo.arch.BaseFragment
import com.apm29.yjw.demo.di.component.AppComponent
import com.apm29.yjw.demo.di.component.DaggerDefaultFragmentComponent
import com.apm29.yjw.demo.di.module.DefaultFragmentModule
import com.apm29.yjw.demo.model.DataMagicBox
import com.apm29.yjw.demo.viewmodel.ALIPAY_CHANNEL_CODE
import com.apm29.yjw.demo.viewmodel.DefaultFragmentViewModel
import com.apm29.yjw.demo.viewmodel.TAOBAO_CHANNEL_CODE
import com.apm29.yjw.gentleloansdemo.R
import kotlinx.android.synthetic.main.register_form_fragment.*


/*
 * Defines an array that contains column names to move from
 * the Cursor to the ListView.
 */
class RegisterFormFragment : BaseFragment<DefaultFragmentViewModel>(){
    override fun setupViewLayout(savedInstanceState: Bundle?): Int {
        return R.layout.register_form_fragment
    }


    override fun setupModel(appComponent: AppComponent) {
        DaggerDefaultFragmentComponent.builder()
                .defaultFragmentModule(DefaultFragmentModule(this))
                .appComponent(appComponent)
                .build()
                .inject(this)
    }

    override fun setupViews(savedInstanceState: Bundle?) {

        val navController = ActivityManager.findHostActivity()?.findNavController(R.id.app_host_fragment)
        layoutApplicantInfo.setOnClickListener {
            tvApplicantInfo.isEnabled = false
            navController?.navigate(R.id.action_registerFormFragment_to_applicantInfoFragment)
        }

        layoutAssetsInfo.setOnClickListener {
            tvApplicantInfo.isEnabled = false
            navController?.navigate(R.id.action_registerFormFragment_to_familyAssetsFragment)
        }

        layoutAlipay.setOnClickListener {
            mViewModel.preVerifyForAlibaba(ALIPAY_CHANNEL_CODE)
        }
        layoutTaobao.setOnClickListener {
            mViewModel.preVerifyForAlibaba(TAOBAO_CHANNEL_CODE)
        }
    }

    override fun initData(savedInstanceState: Bundle?) {
        //获取profile信息,判断是否已经验证支付宝淘宝
        mViewModel.profileVerify()

        mViewModel.profile.observe(this, Observer {
            statusAlipay.text = if(it.peekData().alipay_status) "已验证" else "未验证"
            statusTaobao.text = if(it.peekData().taobao_status) "已验证" else "未验证"
        })

        mViewModel.magicBoxData.observe(this, Observer {
            toAuth(it)
        })
    }

    private fun toAuth(data:DataMagicBox) {
        val (idCardNo, mobile, realName, channelCode) = data
        ////对导航栏的返回按钮，导航栏背景，导航栏title进行设置(可选操作)
        //OctopusManager.getInstance().setNavImgResId(R.mipmap.icon_back);//设置导航 返回图标
        //OctopusManager.getInstance().setPrimaryColorResId(R.color.jun_golden);//设置导航背 景
        //OctopusManager.getInstance().setTitleColorResId(R.color.colorAccent);// 设置title字体 颜色
        //OctopusManager.getInstance().setTitleSize(14);//sp 设置title字体大小
        OctopusManager.getInstance().isShowWarnDialog = true// 强制对话框是否显示
        OctopusManager.getInstance().statusBarBg = R.color.colorAccent// 设置状态栏背景

        //调用SDK
        //activity为当前所在Activity实例
        // channelCode为渠道码,例如支付宝对应的渠道码:005004，其他渠道请替换对应编码，
        // octopus Param为需要上传的可选参数，octopusTaskCallBack回调方法实现OctopusTaskCallBack接口 即可
        val param = OctopusParam()
        //param.passbackarams=“*****”;//可选，透传参数
        param.identityCode = idCardNo//可选，身份证号码
        param.mobile = mobile//可选，手机号码
        param.realName = realName//可选，真实姓名
        OctopusManager.getInstance().getChannel(ActivityManager.currentActivity, channelCode, param){
            code,taskId->
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("认证提示")
                    .setCancelable(false)
                    .setPositiveButton("确定")
                    { d, _ ->
                        d.dismiss()
                        mViewModel.profileVerify()
                    }
            //成功
            when (code) {
                -1//任务失败
                -> builder.setMessage("任务失败:$taskId")
                0//任务成功
                -> {
                    builder.setMessage("认证成功")

                    //上传到服务器
                    mViewModel.uploadAlipayResult(mobile, taskId,data.type)
                }
                10//任务失败
                -> builder.setMessage("网络异常无法连接到服务")
                11//任务失败
                -> builder.setMessage("服务访问失败导致任务创建失败:$taskId")
                20//任务失败
                -> builder.setMessage("任务创建失败,请反馈具体错误信息给技术支持:$taskId")
                30//任务失败
                -> builder.setMessage("数据解析失败:$taskId")
                31//任务失败
                -> builder.setMessage("返回的数据为空,网站改版或访问限制，请稍后重试:$taskId")
                40//任务失败
                -> builder.setMessage("爬取网页失败,网站改版或访问限制，请稍后重试:$taskId")
                50//任务失败
                -> builder.setMessage("任务被用户强制关闭")
                else//任务失败 code:  1050-1100
                -> builder.setMessage("任内部请求异常引起的相关错误,请反馈具体错误信息给技术支持务失败:$code")
            }
            builder.show()
        }
    }
}