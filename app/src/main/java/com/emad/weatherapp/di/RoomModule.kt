package com.emad.weatherapp.di

import android.app.Application
import android.util.Log
import androidx.room.Room
import androidx.room.RoomDatabase
import com.emad.weatherapp.db.WeatherDB
import com.emad.weatherapp.db.WeatherDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RoomModule {
    @Provides @Singleton
    fun provideRoomDB( app: Application):WeatherDB {
        return Room.databaseBuilder(app, WeatherDB::class.java, "photosDB")
            .fallbackToDestructiveMigration()
            .allowMainThreadQueries()
            .build()
    }


    @Provides @Singleton
    fun providesDao(db: WeatherDB): WeatherDao{
        return db.getWeatherDao()
    }
}