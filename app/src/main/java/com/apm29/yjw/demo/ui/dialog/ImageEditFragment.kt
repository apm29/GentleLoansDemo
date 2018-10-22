package com.apm29.yjw.demo.ui.dialog

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.TextUtils
import androidx.appcompat.app.AlertDialog
import androidx.core.content.FileProvider
import androidx.core.view.ViewCompat
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.FragmentNavigatorExtras
import com.apm29.yjw.demo.arch.BaseFragment
import com.apm29.yjw.demo.di.component.AppComponent
import com.apm29.yjw.demo.di.component.DaggerDefaultFragmentComponent
import com.apm29.yjw.demo.di.module.DefaultFragmentModule
import com.apm29.yjw.demo.ui.form.information.Photo
import com.apm29.yjw.demo.ui.form.information.ImageInfoFromFragmentArgs
import com.apm29.yjw.demo.utils.findHostNaviController
import com.apm29.yjw.demo.utils.showToast
import com.apm29.yjw.demo.viewmodel.CommunicateViewModel
import com.apm29.yjw.demo.viewmodel.DefaultFragmentViewModel
import com.apm29.yjw.gentleloansdemo.BuildConfig
import com.apm29.yjw.gentleloansdemo.R
import com.tencent.bugly.crashreport.CrashReport
import com.yanzhenjie.permission.AndPermission
import com.yanzhenjie.permission.Permission
import kotlinx.android.synthetic.main.image_edit_layout.*
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


const  val CODE_CAMER_IMAGE_CAPTURE = 1988
class ImageEditFragment:BaseFragment<DefaultFragmentViewModel>() {
    override fun setupViewLayout(savedInstanceState: Bundle?): Int {
        return R.layout.image_edit_layout
    }

    override fun setupModel(appComponent: AppComponent) {
        DaggerDefaultFragmentComponent.builder()
                .defaultFragmentModule(DefaultFragmentModule(this))
                .appComponent(appComponent)
                .build()
                .inject(this)
    }

    override fun setupViews(savedInstanceState: Bundle?) {
        btnCapture.setOnClickListener {
            requestCameraPermission()
        }
        btnSave.setOnClickListener {
            val name = getString(R.string.camera_image)
            val extras = FragmentNavigatorExtras(
                    ivCamera to name
            )
            val args = ImageInfoFromFragmentArgs.Builder()
                    .setPhotoData(mPhotoData)
                    .build()
                    .toBundle()
            ViewCompat.setTransitionName(ivCamera, name)

            val communicateViewModel = ViewModelProviders.of(requireActivity()).get(CommunicateViewModel::class.java)
            communicateViewModel.onImageSaved(mPhotoData)
            findHostNaviController()?.popBackStack(R.id.imageInfoFromFragment,false)
        }
    }

    val mPhotoData: Photo? by lazy {
        ImageEditFragmentArgs.fromBundle(arguments)
                .photoData
    }

    override fun initData(savedInstanceState: Bundle?) {

    }

    private fun requestCameraPermission() {
        AndPermission.with(this)
                .runtime()
                .permission(arrayOf(Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE))
                .onGranted {
                    createImageFile()?.let {
                        file->
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
                                findHostNaviController()?.navigateUp()
                            }
                            .show()
                }
                .rationale(RuntimeRationale())
                .start()
    }
    private var mCurrentPhotoPath: String? = null

    private fun createImageFile(): File? {
        // Create an image file name
        return try {
            val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINESE).format(Date())
            val storageDir: File = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
            File.createTempFile(
                    "JPEG_${timeStamp}_", /* prefix */
                    ".jpg", /* suffix */
                    storageDir /* directory */
            ).apply {
                // Save a file: path for use with ACTION_VIEW intents
                mCurrentPhotoPath = absolutePath
            }
        } catch (e: Exception) {
            null
        }
    }
    private fun captureImage(file:File) {
        val cameraIntent = Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE)
        if (cameraIntent.resolveActivity(requireContext().packageManager)!=null) {
            val photoURI: Uri = FileProvider.getUriForFile(
                    requireContext(),
                    BuildConfig.APPLICATION_ID+".provider",
                    file
            )
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            startActivityForResult(cameraIntent, CODE_CAMER_IMAGE_CAPTURE)
        }else{
            showToast("没有可用的拍照App,请到应用市场下载相机类App")
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == CODE_CAMER_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK )  {
            if (data != null) {
                val photo = data.extras?.get("data") as Bitmap //缩略图
                mPhotoData?.imageUrl = data.extras?.toString()
                ivCamera.setImageBitmap(photo)

            }else{
                showToast("相机Thumb数据为空")
            }
        }
    }
}