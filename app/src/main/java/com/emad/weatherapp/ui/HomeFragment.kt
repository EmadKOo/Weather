package com.emad.weatherapp.ui

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.emad.weatherapp.App.Companion.capitalizeFirstChar
import com.emad.weatherapp.App.Companion.toCelsius
import com.emad.weatherapp.R
import com.emad.weatherapp.databinding.FragmentHomeBinding
import com.emad.weatherapp.db.models.Photo
import com.emad.weatherapp.pojo.WeatherResponse
import com.emad.weatherapp.utils.Constants.Companion.IMAGE_PICK_CAMERA_CODE
import com.emad.weatherapp.utils.Constants.Companion.IMAGE_PICK_GALLERY_CODE
import com.emad.weatherapp.utils.Constants.Companion.REQUEST_CAMERA
import com.emad.weatherapp.utils.Constants.Companion.REQUEST_GALLERY
import com.emad.weatherapp.utils.Constants.Companion.REQUEST_LOCATION
import com.emad.weatherapp.utils.Constants.Companion.STORAGE_REQUEST_CODE
import com.emad.weatherapp.utils.Fonts
import com.emad.weatherapp.utils.NetworkUtils
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@Suppress("DEPRECATION")
@AndroidEntryPoint
class HomeFragment : BaseFragment(), ISelectedImage {
    private var weatherResponse: WeatherResponse? = null
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var actionFlag: Int = -1 // 1 ->add image, 2->saveImage, 3->share image, 4-> getAllImages
    private lateinit var currentImageURI: Uri
    lateinit var mBinding: FragmentHomeBinding
    @Inject
    lateinit var fonts: Fonts
    @Inject
    lateinit var networkUtils: NetworkUtils
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mBinding= FragmentHomeBinding.inflate(inflater, container, false)
        mBinding.fonts= fonts
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (networkUtils.isNetworkConnected()){
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
            handleViews()
            checkLocationPermission()
        }else{
            showHideButtons(GONE)
            showIndefiniteSnackbar("No Internet Connection")
        }

    }

    fun handleViews(){
        mBinding.addImage.setOnClickListener {
            actionFlag= 1
            if (checkStoragePermission()){
                showCameraGalleryDialog(this)
            }
        }

        mBinding.shareCurrentImage.setOnClickListener {
            actionFlag= 3
            if (checkStoragePermission()){
                shareCurrentImage()
            }
        }

        mBinding.saveCurrentImage.setOnClickListener {
            actionFlag= 2
            if (checkStoragePermission()){
                saveCurrentImage()
            }
        }

        mBinding.allSavedImages.setOnClickListener {
            actionFlag= 4
            if (checkStoragePermission()){
                goToSavedPhotos()
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun getLocation(){
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                homeViewModel.getWeather(location?.latitude.toString(), location?.longitude.toString())
                homeViewModel.getWeatherLiveData().observe(viewLifecycleOwner, Observer {response ->
                    weatherResponse= response
                    mBinding.cityName.text= "${response.list[0].name}, ${response.list[0].sys.country}"
                    mBinding.tempTV.text = "${ toCelsius(response.list[0].main.temp)} \u2103"
                    mBinding.descriptionTV.text = capitalizeFirstChar(response.list[0].weather[0].description)
                })
        }
    }

    private fun shareCurrentImage(){
        showHideButtons(GONE)
        val mUri =Uri.parse(saveScreenshot( mBinding.containerLayout).toString())
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_STREAM, mUri)
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(Intent.createChooser(intent, "Share via"))
        currentImageURI= mUri
        showHideButtons(VISIBLE)
        Log.d("TAG", "shareCurrentImage: "+ mUri.toString())
    }

    private fun saveCurrentImage(){
        showHideButtons(GONE)
        showProgressDialog("Please Wait...")
        CoroutineScope(Default).launch {
            lateinit var mUri: String
            if (weatherResponse == null){
                mUri= "android.resource://com.emad.weatherapp/" + R.drawable.back2
            }else {
                mUri = Uri.fromFile(saveScreenshot( mBinding.containerLayout)).toString()
            }
            val currentPhoto= Photo(
                photoPath = mUri,
                cityName = weatherResponse!!.list[0].name,
                temp = toCelsius(weatherResponse!!.list[0].main.temp)
                ,description= capitalizeFirstChar(weatherResponse!!.list[0].weather[0].description))

            homeViewModel.addPhoto(currentPhoto)
            withContext(Main){
                showHideButtons(VISIBLE)
                hideProgressDialog()
                showSnackbar("Image Saved Successfully")
            }
        }
    }
    private fun showHideButtons(visible: Int) {
        mBinding.allSavedImages.visibility = visible
        mBinding.shareCurrentImage.visibility = visible
        mBinding.saveCurrentImage.visibility = visible
        mBinding.addImage.visibility = visible
    }

    private fun goToSavedPhotos(){
        val action= HomeFragmentDirections.actionHomeFragmentToSavedPhotosFragment()
        findNavController().navigate(action)
    }

    private fun pickImageGallery(){
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(Intent.createChooser(intent, "Pick Background Image"), IMAGE_PICK_GALLERY_CODE)
    }

    private fun openCamera(){
        val contentValues= ContentValues()
        contentValues.put(MediaStore.Images.Media.TITLE, "New Picture")
        contentValues.put(MediaStore.Images.Media.DESCRIPTION, "CAMERA")
        currentImageURI = activity?.getContentResolver()?.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,contentValues)!!
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, currentImageURI)
        startActivityForResult(intent, IMAGE_PICK_CAMERA_CODE)
    }

    private fun checkCameraPermission() {
        if (checkSelfPermission(requireActivity(), Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED){
            requestPermissions( arrayOf(Manifest.permission.CAMERA), REQUEST_CAMERA)
        }else{
            openCamera()
        }
    }

    private fun checkGalleryPermission() {
        if (checkSelfPermission(requireActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED)
            requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), REQUEST_GALLERY)
        else
            pickImageGallery()
    }

    private fun checkLocationPermission(){
        if (checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_COARSE_LOCATION)!= PackageManager.PERMISSION_GRANTED || checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED)
            requestPermissions(arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_LOCATION)
        else
            getLocation()
    }

    private fun checkStoragePermission():Boolean{
        if (checkSelfPermission(requireActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED || checkSelfPermission(requireActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE), STORAGE_REQUEST_CODE)
            return false
        }else
            return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if ( resultCode == RESULT_OK ) {
            if (requestCode == IMAGE_PICK_CAMERA_CODE) {
                mBinding.backgroundImageHomeFragment.setImageURI(currentImageURI)
            }else if (requestCode == IMAGE_PICK_GALLERY_CODE){
                mBinding.backgroundImageHomeFragment.setImageURI(data!!.data)
                currentImageURI = data?.data!!
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (PackageManager.PERMISSION_GRANTED == grantResults[0]) {
            if (requestCode == REQUEST_CAMERA) {
                openCamera()
            } else if (requestCode == REQUEST_GALLERY){
                pickImageGallery()
            }else if (requestCode == REQUEST_LOCATION)
                getLocation()
            else if (requestCode == STORAGE_REQUEST_CODE){
                Log.d("TAG", "onRequestPermissionsResult: "+ actionFlag)
                if (actionFlag==1)
                    showCameraGalleryDialog(this)
                else if (actionFlag==2)
                    saveCurrentImage()
                else if (actionFlag==3)
                    shareCurrentImage()
                else if (actionFlag==4)
                    goToSavedPhotos()
            }
        }
    }

    override fun imageResource(a: Int) {
        hideCameraGalleryDialog()
        if (a==1)  checkCameraPermission()
        else if (a==2) checkGalleryPermission()
    }
}
