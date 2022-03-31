package com.hellomydoc.chat.presentation.userInterface.mainPageContent.pageContent.mainContent


import androidx.compose.runtime.*
import com.hellomydoc.chat.*
import com.hellomydoc.chat.presentation.userInterface.mainPageContent.pageContent.mainContent.chatContent.ChatContent

@Composable
fun MainContentSection(viewModel: ChatViewModel) {
    when {
        viewModel.myId.value.isEmpty() -> {
            UserIdNotFoundContent(viewModel)
        }
        viewModel.peerId.value.isEmpty() -> {
            PeerIdNotFoundContent(viewModel)
        }
        else -> {
            ChatContent(viewModel)
        }
    }
}
