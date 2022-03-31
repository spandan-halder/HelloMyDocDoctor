package com.hellomydoc.chat.models

import com.google.gson.Gson

data class ChatPacketData(
    val text: String? = null,
    val attachment: ChatPacketAttachment? = null
) {
    fun clone(): ChatPacketData {
        return ChatPacketData(
            text,
            attachment?.clone()
        )
    }
    fun jsonString(): String {
        return Gson().toJson(this)
    }


    companion object {
        fun fromString(json: String): ChatPacketData? {
            try {
                return Gson().fromJson(json, ChatPacketData::class.java)
            } catch (e: Exception) {
            }
            return null
        }
    }
}