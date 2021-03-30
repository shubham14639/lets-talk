package com.example.letstalk.Uitil

import android.content.Context
import android.net.ConnectivityManager
import android.util.Log

object InternetConnection {
    fun isConnected(context: Context): Boolean {
        var connected = false
        try {
            val cm =
                context.applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val nInfo = cm.activeNetworkInfo
            connected = nInfo != null && nInfo.isAvailable && nInfo.isConnected
            if (!connected) makeToast(context, "No connection available!")
            return connected
        } catch (e: Exception) {
            Log.e("Connectivity Exception", e.message!!)
        }
        return connected
    }
}