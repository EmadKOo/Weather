package com.emad.weatherapp.network

import com.emad.weatherapp.pojo.WeatherResponse
import com.emad.weatherapp.utils.Constants.Companion.API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {

    @GET("data/2.5/find")
    suspend fun getCurrentWeather(
        @Query("lat") lat: String,
        @Query("lon") long: String,
        @Query("appid") appID: String = API_KEY
    ): Response<WeatherResponse>
}