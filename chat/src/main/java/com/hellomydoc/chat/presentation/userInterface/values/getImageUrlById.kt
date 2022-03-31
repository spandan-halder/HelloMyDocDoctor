package com.hellomydoc.chat.presentation.userInterface.values

fun getImageUrlById(name: String): String? {
    return "https://hellomydoc.com/api/v1/welcome/getChatAssetByName/$name"
}