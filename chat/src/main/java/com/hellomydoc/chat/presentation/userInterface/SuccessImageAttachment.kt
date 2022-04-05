package com.hellomydoc.chat.presentation.userInterface.mainPageContent.pageContent.mainContent.chatContent.chatsSection.conversations.messageItem.chatAttachment.imageAttachment

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import com.hellomydoc.chat.*
import com.hellomydoc.chat.presentation.userInterface.values.getImageUrlById

@Composable
fun SuccessImageAttachment(viewModel: ChatViewModel, isMy: Boolean, name: String) {
    val url = getImageUrlById(name) ?:return
    SubcomposeAsyncImage(
        model = url,
        loading = {
            ImageLoadingContent(viewModel)
        },
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
            .clip(RoundedCornerShape(12.dp))
            .clickable {
                       viewModel.showImage(url)
            },
    )
}