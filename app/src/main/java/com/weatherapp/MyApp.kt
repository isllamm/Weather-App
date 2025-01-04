package com.weatherapp

import android.app.Application
import com.weatherapp.core.PrefsHelper
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        PrefsHelper.init(applicationContext)
    }
}