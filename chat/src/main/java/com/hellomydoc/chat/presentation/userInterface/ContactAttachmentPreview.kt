package com.hellomydoc.chat.presentation.userInterface.mainPageContent.pageContent.mainContent.chatContent.chatInput.sendSection.attachmentPreview

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.hellomydoc.chat.*
import com.hellomydoc.chat.presentation.Style

@Composable
fun ContactAttachmentPreview(viewModel: ChatViewModel, contact: Contact?) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(Style.previewSectionHeight.dp)
            .background(Style.previewSectionBackgroundColor),
        contentAlignment = Alignment.Center
    ){
        ContactPreviewContent(viewModel,contact)
        PreviewDelete(viewModel)
    }
}

@Composable
private fun ContactPreviewContent(viewModel: ChatViewModel, contact: Contact?) {
    Card(
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth()
    ){
        Column(
            modifier = Modifier.wrapContentSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Text(contact?.name.toString())
            Text(contact?.phone.toString())
        }
    }
}