package com.hellomydoc.chat.presentation.userInterface

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.hellomydoc.chat.presentation.viewModels.ChatViewModel

@Composable
fun FileAttachmentPreview(viewModel: ChatViewModel, uri: Uri?) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(Style.previewSectionHeight.dp)
            .background(Style.previewSectionBackgroundColor),
        contentAlignment = Alignment.Center
    ){
        FilePreviewContent(viewModel)
        PreviewDelete(viewModel)
    }
}

@Composable
private fun FilePreviewContent(viewModel: ChatViewModel) {
    Card(
        modifier = Modifier
            .fillMaxHeight()
            .width(Style.previewContentWidth.dp)
    ){
        Icon(
            imageVector = Icons.Filled.PictureAsPdf,
            contentDescription = "Pdf",
            tint = Style.pdfIconTint,
            modifier = Modifier.fillMaxSize()
        )
    }
}