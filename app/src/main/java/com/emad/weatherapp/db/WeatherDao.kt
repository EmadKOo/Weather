package com.emad.weatherapp.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.emad.weatherapp.db.models.Photo

@Dao
interface WeatherDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addPhoto(photo: Photo): Long

    @Query("delete from photos where id = :id")
    fun deletePhoto(id: Int)

    @Query("select * from photos")
    fun getAllPhotos(): LiveData<List<Photo>>
}