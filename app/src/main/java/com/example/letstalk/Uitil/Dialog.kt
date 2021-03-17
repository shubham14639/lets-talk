package com.example.letstalk.Uitil

import android.app.ProgressDialog
import android.content.Context
import androidx.swiperefreshlayout.widget.CircularProgressDrawable

object Dialog {

}

fun progresDialog(context: Context, msg: String): ProgressDialog {
    val dialog = ProgressDialog(context)
    dialog.setMessage(msg)
    dialog.setCancelable(false)
    dialog.show()
    return dialog
}

fun placeHolder(cont: Context): CircularProgressDrawable {
    val circularProgressDrawable = CircularProgressDrawable(cont)
    circularProgressDrawable.strokeWidth = 12f
    circularProgressDrawable.centerRadius = 50f
    circularProgressDrawable.start()
    return circularProgressDrawable
}


