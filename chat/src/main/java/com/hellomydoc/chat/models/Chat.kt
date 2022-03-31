package com.hellomydoc.chat.models

import com.google.gson.Gson

data class Chat(
    var chatId: String = "",
    var sender: String = "",
    var receiver: String = "",
    var data: ChatPacketData? = null,
    var createdAt: Long = 0,
    var arrivedServerAt: Long = 0,
    var receivedAt: Long = 0,
    var seenAt: Long = 0,
    var deleted: Boolean = false
){
    companion object {
        fun fromString(json: String): Chat? {
            try {
                return Gson().fromJson(json, Chat::class.java)
            } catch (e: Exception) {
            }
            return null
        }
    }
    fun jsonString(): String {
        return Gson().toJson(this)
    }
}





