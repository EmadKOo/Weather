package com.emad.weatherapp.repository

import com.emad.weatherapp.db.WeatherDao
import com.emad.weatherapp.db.models.Photo
import com.emad.weatherapp.network.ApiInterface
import javax.inject.Inject

class HomeRepository @Inject constructor(var weatherDao: WeatherDao) {
    @Inject
    lateinit var apiService: ApiInterface
    suspend fun getCurrentWeather(lat: String, long: String) = apiService.getCurrentWeather(lat, long)

    suspend fun addImage(photo: Photo): Long {
        return weatherDao.addPhoto(photo)
    }

    suspend fun getAllImages()= weatherDao.getAllPhotos()
    suspend fun removeImage(id: Int)= weatherDao.deletePhoto(id)
}