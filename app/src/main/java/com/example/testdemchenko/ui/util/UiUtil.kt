package com.example.testdemchenko.ui.util

import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.net.ConnectivityManager
import androidx.core.content.ContextCompat

val Resources.screenSize
    get() = configuration.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK

fun isNetworkConnected(context: Context): Boolean {
    val cm = ContextCompat.getSystemService(context, ConnectivityManager::class.java)
    return cm!!.activeNetworkInfo != null && cm.activeNetworkInfo!!.isConnected
}