package com.emad.weatherapp.ui

interface ISelectedImage {
    fun imageResource(a: Int) // if a == 1 => camera  if a == 2 => gallery
}