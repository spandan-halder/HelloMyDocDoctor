package com.hellomydoc.chat

import android.app.Application

interface AppDelegate {
    fun getPrefString(userIdKey: String): String?
    fun setFcmTokenSynced(b: Boolean)
    fun setAndroidId(id: String)

    fun fetchFcmToken(callback: (String)->Unit)
    fun setFcmToken(it: String)

    val myIdKey: String
    val chatFirebaseDatabaseUrl: String
    val chatFirebaseAppName: String
    val app: Application
    val chatApiKey: String
    val chatApplicationId: String
    val chatProjectId: String?
    val fcmSynced: Boolean
    val androidId: String
    val fcmToken: String?
    val userId: String
    val peerNotFoundText: String
    val userNotFoundString: String?
    val userIdKey: String
    val peerIdKey: String
}

