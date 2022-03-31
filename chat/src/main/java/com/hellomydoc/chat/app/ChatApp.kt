package com.hellomydoc.chat.app

import android.annotation.SuppressLint
import android.app.Application
import android.provider.Settings
import com.hellomydoc.chat.AppDelegate
import com.hellomydoc.chat.ChatBox
import com.hellomydoc.chat.Metar
import dagger.hilt.android.HiltAndroidApp


abstract class ChatApp: Application(), AppDelegate {
    override fun onCreate() {
        super.onCreate()
        onCreateActions()
        Metar.initialize(this)
        ChatBox.onAppInit(this)
        ChatBox.updateFcmTokenFromApp(this)
    }

    abstract fun onCreateActions()

    abstract override fun getPrefString(userIdKey: String): String?

    abstract override fun setFcmTokenSynced(b: Boolean)

    abstract override fun setAndroidId(id: String)

    abstract override fun fetchFcmToken(callback: (String) -> Unit)

    abstract override fun setFcmToken(it: String)

    abstract override val myIdKey: String
    //////////////////////////////////////
    override val chatFirebaseDatabaseUrl: String
        get() = Metar["chat_database_url"]

    override val chatFirebaseAppName: String
        get() = Metar["chat_firebase_app_name"]

    override val app: Application
        get() = this

    override val chatApiKey: String
        get() = Metar["chat_api_key"]

    override val chatApplicationId: String
        get() = Metar["chat_application_id"]

    override val chatProjectId: String
        get() = Metar["chat_project_id"]
    override val androidId: String
        @SuppressLint("HardwareIds")
        get() = Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)
    ////////////////////////////////////
    abstract override val fcmSynced: Boolean
    abstract override val fcmToken: String?
    abstract override val userId: String
    abstract override val peerNotFoundText: String
    abstract override val userNotFoundString: String?
    abstract override val userIdKey: String
    abstract override val peerIdKey: String
}