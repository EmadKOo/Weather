package com.emad.weatherapp

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App: Application() {
    companion object{
        fun capitalizeFirstChar( text: String): String{
            return text[0].toUpperCase()+text.substring(1)
        }

        fun toCelsius(temp: Double): String{
            return (temp - 272.15).toString().substring(0,4)
        }
    }
}