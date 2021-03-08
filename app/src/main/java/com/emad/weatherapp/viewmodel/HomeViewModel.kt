package com.emad.weatherapp.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emad.weatherapp.db.models.Photo
import com.emad.weatherapp.pojo.WeatherResponse
import com.emad.weatherapp.repository.HomeRepository
import com.emad.weatherapp.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private var homeRepository: HomeRepository): ViewModel() {
    private var weatherLiveData: MutableLiveData<WeatherResponse> = MutableLiveData()
    private var isPhotoAdded: MutableLiveData<Resource<Long>> = MutableLiveData()
    private lateinit var allPhotosLiveData: LiveData<List<Photo>>

    fun getWeatherLiveData() = weatherLiveData
    fun allPhotosLiveData() = allPhotosLiveData

    fun getWeather(lat: String, long: String){
        viewModelScope.launch {
            val response = homeRepository.getCurrentWeather(lat, long)
            if (response.isSuccessful){
                weatherLiveData.postValue(response.body())
            }else{
                Log.d("TAG", "getWeatherLiveData: Error " + response.message())
            }
        }
    }

    fun addPhoto(photo: Photo){
        viewModelScope.launch {
            isPhotoAdded.postValue(Resource.Success(homeRepository.addImage(photo)))
        }
    }

    fun getAllPhotos(){
        viewModelScope.launch{
            allPhotosLiveData = homeRepository.getAllImages()
        }
    }

    fun removePhoto(id: Int){
        viewModelScope.launch{
            homeRepository.removeImage(id)
        }
    }
}