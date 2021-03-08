package com.emad.weatherapp.pojo

import com.google.gson.annotations.SerializedName

class WeatherResponse(@SerializedName("message") val message : String,
                      @SerializedName("cod") val cod : Int,
                      @SerializedName("count") val count : Int,
                      @SerializedName("list") val list : List<Info>) {


    data class Clouds (

        @SerializedName("all") val all : Int
    )

    data class Coord (

        @SerializedName("lat") val lat : Double,
        @SerializedName("lon") val lon : Double
    )

    data class Info (

        @SerializedName("id") val id : Int,
        @SerializedName("name") val name : String,
        @SerializedName("coord") val coord : Coord,
        @SerializedName("main") val main : Main,
        @SerializedName("dt") val dt : Int,
        @SerializedName("wind") val wind : Wind,
        @SerializedName("sys") val sys : Sys,
        @SerializedName("rain") val rain : String,
        @SerializedName("snow") val snow : String,
        @SerializedName("clouds") val clouds : Clouds,
        @SerializedName("weather") val weather : List<Weather>
    )

    data class Main (

        @SerializedName("temp") val temp : Double,
        @SerializedName("feels_like") val feels_like : Double,
        @SerializedName("temp_min") val temp_min : Double,
        @SerializedName("temp_max") val temp_max : Double,
        @SerializedName("pressure") val pressure : Int,
        @SerializedName("humidity") val humidity : Int,
        @SerializedName("sea_level") val sea_level : Int,
        @SerializedName("grnd_level") val grnd_level : Int
    )

    data class Sys (

        @SerializedName("country") val country : String
    )

    data class Weather (

        @SerializedName("id") val id : Int,
        @SerializedName("main") val main : String,
        @SerializedName("description") val description : String,
        @SerializedName("icon") val icon : String
    )

    data class Wind (

        @SerializedName("speed") val speed : Double,
        @SerializedName("deg") val deg : Int
    )
}