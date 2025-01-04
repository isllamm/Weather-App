package com.weatherapp.core

import android.content.Context
import android.content.SharedPreferences

object PrefsHelper {

    private lateinit var preferences: SharedPreferences
    private const val PREFS_NAME = "shared_prefs"

    fun init(context: Context){
        preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    fun setIsDarkMode(isFirst: Boolean) = preferences.edit().putBoolean(Constants.IS_DARK_MODE, isFirst).apply()
    fun isDarkMode(): Boolean = preferences.getBoolean(Constants.IS_DARK_MODE, false)

}