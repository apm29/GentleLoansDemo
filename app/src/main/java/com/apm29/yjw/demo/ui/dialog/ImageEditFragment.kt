package com.apm29.yjw.demo.ui.dialog

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import androidx.appcompat.app.AlertDialog
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.apm29.yjw.demo.arch.BaseFragment
import com.apm29.yjw.demo.di.component.AppComponent
import com.apm29.yjw.demo.di.component.DaggerDefaultFragmentComponent
import com.apm29.yjw.demo.di.module.DefaultFragmentModule
import com.apm29.yjw.demo.utils.findHostNavController
import com.apm29.yjw.demo.utils.showToast
import com.apm29.yjw.demo.viewmodel.CommunicateViewModel
import com.apm29.yjw.demo.viewmodel.DefaultFragmentViewModel
import com.apm29.yjw.gentleloansdemo.BuildConfig
import com.apm29.yjw.gentleloansdemo.R
import com.yanzhenjie.permission.AndPermission
import com.yanzhenjie.permission.Permission
import kotlinx.android.synthetic.main.image_edit_layout.*
import java.io.File
import java.text.SimpleDateFormat
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption
import com.amap.api.location.AMapLocationListener
import com.apm29.yjw.demo.model.CODE_CAMERA_IMAGE_CAPTURE
import com.apm29.yjw.demo.model.Photo
import java.util.*




class ImageEditFragment : BaseFragment<DefaultFragmentViewModel>() {
    override fun setupViewLayout(savedInstanceState: Bundle?): Int {
        return R.layout.image_edit_layout
    }

    override fun setupModel(appComponent: AppComponent) {
        DaggerDefaultFragmentComponent.builder()
                .appComponent(appComponent)
                .defaultFragmentModule(DefaultFragmentModule(this))
                .build().inject(this)
    }

    override fun setupViews(savedInstanceState: Bundle?) {
        btnCapture.setOnClickListener {
            requestCameraPermission()
        }
    }

    private val mPhotoData: Photo? by lazy {
        ImageEditFragmentArgs.fromBundle(arguments)
                .photoData
    }


    private fun requestCameraPermission() {
        AndPermission.with(this)
                .runtime()
                .permission(arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE))
                .onGranted {
                    createImageFile()?.let { file ->
                        captureImage(file)
                    }
                }
                .onDenied {

                    val permissionNames = Permission.transformText(context, it)
                    val message = requireActivity().getString(R.string.message_permission_rationale, TextUtils.join("\n", permissionNames))
                    AlertDialog.Builder(requireContext())
                            .setCancelable(false)
                            .setTitle(R.string.title_permission_dialog)
                            .setMessage(message)
                            .setPositiveButton(R.string.resume) { _, _ ->
                                if (AndPermission.hasAlwaysDeniedPermission(this, it)) {
                                    AndPermission.with(this)
                                            .runtime()
                                            .setting()
                                            .onComeback {
                                                // 用户从设置回来了。
                                                requestCameraPermission()
                                            }
                                            .start()
                                } else {
                                    requestCameraPermission()
                                }
                            }
                            .setNegativeButton(R.string.quit) { _, _ ->
                                findHostNavController()?.navigateUp()
                            }
                            .show()
                }
                .rationale(RuntimeRationale())
                .start()
    }

    private var mCurrentPhotoPath: String? = null

    private fun createImageFile(): File? {
        // Create an image filePath name
        return try {
            val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
            val storageDir: File? = requireContext().getExternalFilesDir(null)
            println("storageFile:" + storageDir?.absolutePath)
            File.createTempFile(
                    "JPEG_${timeStamp}_", /* prefix */
                    ".jpg", /* suffix */
                    storageDir /* directory */
            ).apply {
                mCurrentPhotoPath = absolutePath
            }
        } catch (e: Exception) {
            null
        }
    }

    private fun captureImage(file: File) {
        val cameraIntent = Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE)
        if (cameraIntent.resolveActivity(requireContext().packageManager) != null) {
            val photoURI: Uri = if (Build.VERSION_CODES.N <= Build.VERSION.SDK_INT) {
                FileProvider.getUriForFile(
                        requireContext(),
                        BuildConfig.APPLICATION_ID + ".provider",
                        file
                )
            } else {
                file.toUri()
            }
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            startActivityForResult(cameraIntent, CODE_CAMERA_IMAGE_CAPTURE)
        } else {
            showToast("没有可用的拍照App,请到应用市场下载相机类App")
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == CODE_CAMERA_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
//                val photo = data.extras?.get("data") as Bitmap //缩略图
            println(mCurrentPhotoPath)
            mPhotoData?.filePath = mCurrentPhotoPath

            requestLocationPermission()

        }
    }

    private fun requestLocationPermission() {
        AndPermission.with(this)
                .runtime()
                .permission(Manifest.permission.ACCESS_FINE_LOCATION)
                .onGranted {
                    locate()
                }
                .onDenied {

                    val permissionNames = Permission.transformText(context, it)
                    val message = requireActivity().getString(R.string.message_permission_rationale, TextUtils.join("\n", permissionNames))
                    AlertDialog.Builder(requireContext())
                            .setCancelable(false)
                            .setTitle(R.string.title_permission_dialog)
                            .setMessage(message)
                            .setPositiveButton(R.string.resume) { _, _ ->
                                if (AndPermission.hasAlwaysDeniedPermission(this, it)) {
                                    AndPermission.with(this)
                                            .runtime()
                                            .setting()
                                            .onComeback {
                                                // 用户从设置回来了。
                                                requestLocationPermission()
                                            }
                                            .start()
                                } else {
                                    requestLocationPermission()
                                }
                            }
                            .setNegativeButton(R.string.quit) { _, _ ->
                                findHostNavController()?.navigateUp()
                            }
                            .show()
                }
                .rationale(RuntimeRationale())
                .start()
    }

    private fun locate() {
        showLoading()
        //声明AMapLocationClient类对象
        val mLocationClient //初始化定位
                = AMapLocationClient(requireContext().applicationContext)
        //声明定位回调监听器
        val mLocationListener = AMapLocationListener { location ->
            if (location.errorCode == 0) {
                showToast(location.address)
                mPhotoData?.locationDes = location.address

                uploadImage()


            }else{
                showToast(location.errorInfo)
                println("${location.errorCode} = ${location.errorInfo}")
            }
            hideLoading()
        }

        val option = AMapLocationClientOption()
        option.locationMode = AMapLocationClientOption.AMapLocationMode.Hight_Accuracy
        option.isOnceLocation = true
        option.isOnceLocationLatest = true
        option.isNeedAddress = true
        option.httpTimeOut = 150000
        //设置定位回调监听
        mLocationClient.setLocationListener(mLocationListener)
        mLocationClient.setLocationOption(option)
        mLocationClient.startLocation()
    }

    private fun uploadImage() {
        mCurrentPhotoPath?.let {
            mViewModel.uploadImage(it)
        }
    }

    override fun initData(savedInstanceState: Bundle?) {
        mViewModel.uploadResultData.observe(this, Observer {
            mPhotoData?.imageUrl = it.peekData().path
            hideLoading()
            backWithResult()
        })
    }

    private fun backWithResult() {
        val communicateViewModel = ViewModelProviders.of(requireActivity()).get(CommunicateViewModel::class.java)
        communicateViewModel.onImageSaved(mPhotoData)
        findHostNavController()?.popBackStack(R.id.imageInfoFromFragment, false)
    }
}