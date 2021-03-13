package com.example.letstalk.Uitil

import android.app.Activity
import android.content.Context
import android.util.Log
import android.widget.Toast
import com.example.letstalk.BuildConfig

object AppLog {
    fun logger(value: String) {
        if (BuildConfig.DEBUG)
            Log.d("TESTLOG", "value : $value")
    }

    fun makeToast(context: Context, message: String) {
        Toast.makeText(context,message,Toast.LENGTH_LONG).show()
    }

}