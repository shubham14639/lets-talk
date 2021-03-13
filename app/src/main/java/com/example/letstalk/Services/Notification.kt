package com.example.letstalk.Services

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.example.letstalk.Activity.MainActivity
import com.example.letstalk.R
import com.example.letstalk.Uitil.AppLog
import com.google.firebase.messaging.FirebaseMessagingService

class Notification : FirebaseMessagingService() {
    /*    override fun onMessageReceived(remoteMessage: RemoteMessage) {
            super.onMessageReceived(remoteMessage)
            AppLog.logger("FROM : " + remoteMessage.from)
            if (remoteMessage.data.isNotEmpty()) {
                AppLog.logger("Message data : " + remoteMessage.data)
            }
            if (remoteMessage.notification != null) {
                AppLog.logger("Message body : " + remoteMessage.notification!!.body)
            //    sendNotification(remoteMessage.notification!!.body)
            }
        }*/
    override fun onNewToken(p0: String) {
        AppLog.logger("token is $p0")
        Toast.makeText(this, "ONsEcvdsvbd", Toast.LENGTH_LONG).show()
    }

    private fun sendNotification(body: String?) {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        intent.putExtra("Notification", body)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)
        val notificationSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        val notificationBuilder = NotificationCompat.Builder(this, "Notification")
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle("Push Notification FCM")
            .setContentText(body)
            .setAutoCancel(true)
            .setSound(notificationSound)
            .setContentIntent(pendingIntent)

        val notificationManager: NotificationManager =
            this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(0, notificationBuilder.build())
    }


}