package com.hellomydoc.chat.presentation.userInterface.mainPageContent.pageContent.mainContent.chatContent.chatsSection.conversations.messageItem

import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.runtime.*
import com.hellomydoc.chat.models.Chat
import com.hellomydoc.chat.presentation.userInterface.mainPageContent.pageContent.mainContent.chatContent.chatsSection.conversations.messageItem.mychat.MyMessage
import com.hellomydoc.chat.presentation.userInterface.PeerMessage
import com.hellomydoc.chat.presentation.viewModels.ChatViewModel

@Composable
fun LazyItemScope.MessageItem(viewModel: ChatViewModel, chat: Chat) {
    val isMe = chat.sender == viewModel.myId.value
    if(isMe){
        MyMessage(viewModel,chat)
    }else{
        PeerMessage(viewModel,chat)
    }
}











