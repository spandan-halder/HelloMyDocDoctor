package com.hellomydoc.chat.presentation.userInterface.mainPageContent.pageContent.mainContent


import androidx.compose.runtime.*
import com.hellomydoc.chat.presentation.userInterface.ChatContent
import com.hellomydoc.chat.presentation.userInterface.PeerIdNotFoundContent
import com.hellomydoc.chat.presentation.viewModels.ChatViewModel

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
