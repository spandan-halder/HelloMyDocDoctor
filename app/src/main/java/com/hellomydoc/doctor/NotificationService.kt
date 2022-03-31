package com.hellomydoc.doctor
import android.util.Log
import com.hellomydoc.chat.ChatBox
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class NotificationService : FirebaseMessagingService() {

    override fun onNewToken(p0: String) {
        super.onNewToken(p0)
        ChatBox.onNewToken(this.application as App,p0)
    }


    override fun onMessageReceived(p0: RemoteMessage) {
        Log.d("new_message",p0.toString())
    }
}