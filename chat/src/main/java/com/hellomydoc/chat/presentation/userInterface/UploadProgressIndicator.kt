package com.hellomydoc.chat.presentation.userInterface.mainPageContent.pageContent.mainContent.chatContent.chatsSection.conversations.messageItem.chatAttachment

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun UploadProgressIndicator(progress: Int) {
    Box(
        modifier = Modifier.wrapContentSize(),
        contentAlignment = Alignment.Center
    ){
        Icon(
            imageVector = Icons.Filled.CloudUpload,
            tint = Color.Gray,
            contentDescription = "Uploading",
            modifier = Modifier.size(18.dp)
        )
        CircularProgressIndicator(
            color = Color(0xFF0099FF),
            modifier = Modifier.size(32.dp),
            progress = progress/100f
        )
    }
}
