package com.emad.weatherapp.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.emad.weatherapp.db.models.Photo

@Database(entities = [Photo::class], version = 1, exportSchema = false)
abstract class WeatherDB: RoomDatabase() {
    abstract fun getWeatherDao(): WeatherDao
}