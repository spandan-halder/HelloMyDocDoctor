package com.hellomydoc.chat.presentation.userInterface

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.hellomydoc.chat.presentation.viewModels.ChatViewModel
import com.hellomydoc.chat.presentation.userInterface.mainPageContent.pageContent.mainContent.chatContent.chatsSection.conversations.Conversations

@Composable
fun ColumnScope.ChatsSection(viewModel: ChatViewModel) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .weight(1f),
        contentAlignment = Alignment.Center
    ){
        if(viewModel.chats.isEmpty()){
            NoConversationYet(viewModel)
        }
        else{
            Conversations(viewModel)
        }
    }
}