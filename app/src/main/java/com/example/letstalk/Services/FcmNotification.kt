package com.example.letstalk.Services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.example.letstalk.Activity.ChatActivity
import com.example.letstalk.R
import com.example.letstalk.Uitil.AppLog
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class FcmNotification : FirebaseMessagingService() {
    override fun onNewToken(p0: String) {
        AppLog.logger("New Token Called $p0")
    }

    override fun onMessageReceived(message: RemoteMessage) {
        if (message.notification != null) {
            val title = message.notification?.title.toString()
            val body = message.notification?.body.toString()
            showNotification(title,body)
            AppLog.logger("Title is $title and body is $body and total data is ${message.data}")
        } else {
            AppLog.logger("message is null")
        }
    }

    override fun onDeletedMessages() {
        super.onDeletedMessages()
    }

    fun showNotification(title: String, message: String) {
        val mNotificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "YOUR_CHANNEL_ID",
                "YOUR_CHANNEL_NAME",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            mNotificationManager.createNotificationChannel(channel)
        }
        val mBuilder = NotificationCompat.Builder(applicationContext, "YOUR_CHANNEL_ID")
            .setSmallIcon(R.mipmap.ic_launcher) // notification icon
            .setContentTitle(title) // title for notification
            .setContentText(message)// message for notification
            .setAutoCancel(true) // clear notification after click
        val intent = Intent(applicationContext, ChatActivity::class.java)
        val pi = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        mBuilder.setContentIntent(pi)
        mNotificationManager.notify(0, mBuilder.build())
    }

}