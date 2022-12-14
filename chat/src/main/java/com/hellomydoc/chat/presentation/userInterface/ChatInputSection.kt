package com.hellomydoc.chat.presentation.userInterface


import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.hellomydoc.chat.presentation.userInterface.mainPageContent.pageContent.mainContent.chatContent.chatInput.sendSection.SendSection
import com.hellomydoc.chat.presentation.viewModels.ChatViewModel

@Composable
fun ChatInputSection(viewModel: ChatViewModel) {
    ChatInputTopDivider()
    AttachmentPreviewSection(viewModel)
    SendSection(viewModel)
    AnimatedAttachmentSelectionSection(viewModel)
}

@Composable
private fun ChatInputTopDivider() {
    Divider(
        modifier = Modifier.fillMaxWidth(),
        color = Color.LightGray,
        thickness = 0.5.dp
    )
}




