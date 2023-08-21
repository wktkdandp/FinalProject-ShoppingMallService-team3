package com.petpal.swimmer_customer.util

import android.content.Context

object AutoLoginUtil {
    private const val PREFERENCE_NAME = "swimmer_preference"
    private const val AUTO_LOGIN = "auto_login"

    fun setAutoLogin(context: Context, value: Boolean) {


        val prefs = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
        prefs.edit().putBoolean(AUTO_LOGIN, value).apply()
    }

    fun getAutoLogin(context: Context): Boolean {
        val prefs = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
        return prefs.getBoolean(AUTO_LOGIN, false)
    }
}