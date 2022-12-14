package com.hellomydoc.chat.presentation.userInterface

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.hellomydoc.chat.presentation.userInterface.mainPageContent.pageContent.mainContent.chatContent.chatsSection.conversations.messageItem.MessageItem
import com.hellomydoc.chat.presentation.viewModels.ChatViewModel

@Composable
fun Conversations(viewModel: ChatViewModel) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        state = viewModel.state
    ){
        items(viewModel.chats){
            MessageItem(viewModel,it)
        }
    }
}
