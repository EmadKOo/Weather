package com.emad.weatherapp.utils

import android.content.Context
import android.net.ConnectivityManager
import dagger.hilt.android.qualifiers.ActivityContext
import javax.inject.Inject


class NetworkUtils @Inject constructor(@ActivityContext private val ctx: Context) {
      fun isNetworkConnected(): Boolean {
            val cm = ctx.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            if (cm != null) {
                val activeNetwork = cm.activeNetworkInfo
                return activeNetwork != null && activeNetwork.isConnectedOrConnecting
            }
            return false
      }
}