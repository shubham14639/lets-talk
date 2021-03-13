package com.example.letstalk.Uitil

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*

object DateUitil {
    val calendar=Calendar.getInstance()
    val time= calendar.time
    @SuppressLint("SimpleDateFormat")
    val df = SimpleDateFormat("HH:mm a")
    val currentTime: String = df.format(Calendar.getInstance().time)
}