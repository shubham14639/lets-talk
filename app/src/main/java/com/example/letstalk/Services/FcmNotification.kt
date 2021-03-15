package com.example.letstalk.Services

import android.app.Application
import android.app.Notification
import com.example.letstalk.Uitil.AppLog
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class FcmNotification : FirebaseMessagingService() {
    override fun onNewToken(p0: String) {
        AppLog.logger("New Token Called $p0")
    }

    override fun onMessageReceived(message: RemoteMessage) {
        if (message.notification!=null) {
            val body = message.notification?.body
            val title = message.notification?.title

            AppLog.logger("Title is $title and body is $body and total data is ${message.data}")
        }else{
            AppLog.logger("message is null")
        }
    }

    override fun onDeletedMessages() {
        super.onDeletedMessages()
    }

}
class App :Application(){
     var CHANNEL_ID="FCM_CHANNEL_ID"
    override fun onCreate() {
        super.onCreate()
    }
}