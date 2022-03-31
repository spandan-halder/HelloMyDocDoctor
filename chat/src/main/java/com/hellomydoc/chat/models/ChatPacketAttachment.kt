package com.hellomydoc.chat.models

import com.google.gson.Gson

data class ChatPacketAttachment(
    val url: String? = null,
    val thumbnail: String? = null,
    val type: String,
    val name: String? = null,
    val phone: String? = null,
    val location: Location? = null,
    val json: String? = null,
    val progress: Int? = 0
){
    fun clone(): ChatPacketAttachment {
        return ChatPacketAttachment(
            url,
            thumbnail,
            type,
            name,
            json
        )
    }
    fun jsonString(): String {
        return Gson().toJson(this)
    }
}
