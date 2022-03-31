package com.hellomydoc.chat.presentation.userInterface.mainPageContent.pageContent.mainContent.chatContent.chatInput.sendSection.attachmentPreview

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.hellomydoc.chat.*

@Composable
fun BoxScope.PreviewDelete(viewModel: ChatViewModel) {
    IconButton(
        onClick = {
            viewModel.onClosePreviewAttachmentClick()
        },
        modifier = Modifier.align(Alignment.CenterEnd)
    ) {
        Icon(
            imageVector = Icons.Filled.Delete,
            tint = Color.Red,
            contentDescription = "Close"
        )
    }
}