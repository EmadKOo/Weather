package com.emad.weatherapp.utils

import android.content.Context
import android.graphics.Typeface
import dagger.hilt.android.qualifiers.ActivityContext
import javax.inject.Inject

class Fonts @Inject constructor(@ActivityContext private val context: Context) {

    fun Jost() = Typeface.createFromAsset(context.assets, "fonts/Jost-VariableFont_wght.ttf")
    fun Manrope() = Typeface.createFromAsset(context.assets, "fonts/Manrope-VariableFont_wght.ttf")

}