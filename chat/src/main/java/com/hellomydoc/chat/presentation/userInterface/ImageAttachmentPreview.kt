package com.hellomydoc.chat.presentation.userInterface.mainPageContent.pageContent.mainContent.chatContent.chatInput.sendSection.attachmentPreview

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.hellomydoc.chat.*
import com.hellomydoc.chat.presentation.Style

@Composable
fun ImageAttachmentPreview(viewModel: ChatViewModel, uri: Uri?) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(Style.previewSectionHeight.dp)
            .background(Style.previewSectionBackgroundColor),
        contentAlignment = Alignment.Center
    ){
        ImagePreviewContent(uri)
        PreviewDelete(viewModel)
    }
}


@Composable
private fun ImagePreviewContent(uri: Uri?) {
    Card(
        modifier = Modifier
            .fillMaxHeight()
            .width(Style.previewContentWidth.dp)
    ){
        AsyncImage(
            model = uri,
            contentDescription = null,
            contentScale = ContentScale.Crop
        )
    }
}