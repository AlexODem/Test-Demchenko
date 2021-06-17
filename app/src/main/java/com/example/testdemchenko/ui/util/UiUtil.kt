package com.example.sparktestdemchenko.ui.util

import android.content.res.Configuration
import android.content.res.Resources

val Resources.screenSize
    get() = configuration.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK