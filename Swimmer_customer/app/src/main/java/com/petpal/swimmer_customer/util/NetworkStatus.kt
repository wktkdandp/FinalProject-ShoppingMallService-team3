package com.petpal.swimmer_customer.util

import android.content.Context
import android.net.ConnectivityManager

object NetworkStatus {
    const val TYPE_WIFI = 1
    const val TYPE_MOBILE = 2
    const val TYPE_NOT_CONNECTED = 3

    fun getConnectivityStatus(context: Context): Int {
        val manager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = manager.activeNetworkInfo
        if (networkInfo != null) {
            when (networkInfo.type) {
                ConnectivityManager.TYPE_MOBILE -> return TYPE_MOBILE
                ConnectivityManager.TYPE_WIFI -> return TYPE_WIFI
            }
        }
        return TYPE_NOT_CONNECTED
    }
}