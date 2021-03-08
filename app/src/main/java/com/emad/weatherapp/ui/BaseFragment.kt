package com.emad.weatherapp.ui

import android.app.Dialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.emad.weatherapp.R
import com.emad.weatherapp.viewmodel.HomeViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.camera_gallery_dialog.*
import kotlinx.android.synthetic.main.dialog_progress.*
import java.io.File
import java.io.FileOutputStream
import java.util.*


open class BaseFragment: Fragment() {
    lateinit var homeViewModel: HomeViewModel
    lateinit var cameraGalleryDialog: Dialog
    private lateinit var progressDialog: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

    }
    fun showSnackbar(msg: String) {
        Snackbar.make(requireActivity().findViewById(android.R.id.content), msg, Snackbar.LENGTH_SHORT).show()
    }

    fun showIndefiniteSnackbar(msg: String) {
        Snackbar.make(requireActivity().findViewById(android.R.id.content), msg, Snackbar.LENGTH_INDEFINITE).show()
    }
    fun showProgressDialog(text: String) {
        progressDialog = Dialog(requireActivity())
        progressDialog.setContentView(R.layout.dialog_progress)
        progressDialog.tv_progress_text.text = text
        progressDialog.show()
    }

    fun hideProgressDialog() {
        progressDialog.dismiss()
    }
    fun showCameraGalleryDialog(iselectedImage: ISelectedImage) {
        cameraGalleryDialog = Dialog(requireActivity())
        cameraGalleryDialog.setContentView(R.layout.camera_gallery_dialog)
        cameraGalleryDialog.cameraLayout.setOnClickListener {
            //camera
            iselectedImage.imageResource(1)
        }
        cameraGalleryDialog.galleryLayout.setOnClickListener{
            //gallery
            iselectedImage.imageResource(2)
        }
        cameraGalleryDialog.setCanceledOnTouchOutside(true)
        cameraGalleryDialog.getWindow()?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        cameraGalleryDialog.getWindow()?.setLayout(LinearLayoutCompat.LayoutParams.MATCH_PARENT, LinearLayoutCompat.LayoutParams.WRAP_CONTENT)
        cameraGalleryDialog.show()
    }

    fun hideCameraGalleryDialog(){
        cameraGalleryDialog.dismiss()
    }
    fun saveScreenshot( view: View):File {
        val picDir = File(Environment.getExternalStorageDirectory().toString() + "/WeatherApp")
        if (!picDir.exists()) {
            picDir.mkdir()
        }
        view.isDrawingCacheEnabled = true
        view.buildDrawingCache(true)
        var bitmap = view.drawingCache

        val fileName = "${System.currentTimeMillis()}.png"
        val picFile = File("$picDir/$fileName")

        picFile.createNewFile()
        val picOut = FileOutputStream(picFile)
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, (bitmap.height / 1.2).toInt())
        bitmap.compress(CompressFormat.PNG, 100, picOut)
        picOut.close()
        view.destroyDrawingCache()
        return picFile
    }
}