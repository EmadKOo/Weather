package com.emad.weatherapp.db.models

import android.os.Parcelable
import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "photos")
@Keep
@Parcelize
data class Photo(
    var photoPath: String="",
    var cityName: String="",
    var temp: String="",
    var description: String=""
): Parcelable {
    @PrimaryKey(autoGenerate = true)
    var id: Int=0
}