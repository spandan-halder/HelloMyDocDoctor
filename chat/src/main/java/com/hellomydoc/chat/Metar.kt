package com.hellomydoc.chat

import android.content.Context
import android.content.pm.PackageManager
import com.hellomydoc.chat.app.ChatApp

object Metar {
    private var app: ChatApp? = null
    operator fun get(key: String): String {
        return try {
            val ai = (app as Context)
                .packageManager
                .getApplicationInfo(
                    (app as Context)
                        .packageName,
                    PackageManager.GET_META_DATA
                )
            val bundle = ai.metaData
            bundle.getString(key)?:""
        } catch (e: PackageManager.NameNotFoundException) {
            ""
        }
    }
    fun initialize(app: ChatApp) {
        Metar.app = app
    }
}