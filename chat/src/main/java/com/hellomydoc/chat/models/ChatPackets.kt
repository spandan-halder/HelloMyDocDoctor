package com.hellomydoc.chat.models

import com.google.gson.Gson

data class ChatPackets(
    val items: List<Chat>
) {
    override fun toString(): String {
        return Gson().toJson(this)
    }

    companion object {
        fun fromString(json: String): ChatPackets? {
            try {
                return Gson().fromJson(json, ChatPackets::class.java)
            } catch (e: Exception) {
            }
            return null
        }
    }
}