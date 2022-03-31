package com.hellomydoc.chat.presentation.userInterface.mainPageContent.pageContent.mainContent.chatContent.chatsSection.conversations.messageItem.chatAttachment.imageAttachment

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight

@Composable
fun ErrorAttachment(isMy: Boolean) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ){
        Text(
            "Error!",
            color = if(isMy) Color.White else Color.Black,
            fontWeight = FontWeight.Bold
        )
    }
}