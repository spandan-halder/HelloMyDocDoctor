package com.hellomydoc.chat.presentation.userInterface.mainPageContent.pageContent.mainContent.chatContent.chatsSection.conversations.messageItem.chatAttachment.imageAttachment

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.hellomydoc.chat.ChatViewModel

@Composable
fun ImageLoadingContent(viewModel: ChatViewModel) {
    Box(
        contentAlignment = Alignment.Center
    ){
        AsyncImage(
            model = viewModel.placeHolderImageId,
            contentDescription = null,
            contentScale = ContentScale.Crop,
        )
        Icon(
            imageVector = Icons.Filled.Download,
            tint = Color.Gray,
            modifier = Modifier.size(18.dp),
            contentDescription = "Downloading"
        )
        CircularProgressIndicator(
            color = Color(0xFF0099FF),
            modifier = Modifier.size(32.dp)
        )
    }
}