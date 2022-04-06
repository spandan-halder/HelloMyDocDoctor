package com.hellomydoc.chat.presentation.userInterface

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import com.hellomydoc.chat.presentation.viewModels.ChatViewModel

import com.hellomydoc.chat.models.Chat
import com.hellomydoc.chat.presentation.userInterface.mainPageContent.pageContent.mainContent.chatContent.chatsSection.conversations.messageItem.chatTimeAndStatus.ChatTimeAndStatus

@Composable
fun PeerMessage(viewModel: ChatViewModel, chat: Chat) {
    PeerMessageContent(viewModel,chat)
}

@Composable
private fun PeerMessageContent(viewModel: ChatViewModel, chat: Chat) {
    val configuration = LocalConfiguration.current
    SideEffect {
        if(chat.seenAt==0L){
            viewModel.markAsSeen(chat)
        }
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(
                horizontal = Style.myChatHorizontalMargin.dp,
                vertical = Style.myChatVerticalMargin.dp
            )
    ){
        Column(
            modifier = Modifier
                .width((configuration.screenWidthDp * Style.messageCardMaxSizeFactor).dp)
                .align(Alignment.CenterStart),
            horizontalAlignment = Alignment.Start
        ){
            ChatDate(viewModel,chat)
            ChatAttachment(viewModel,chat,false)
            PeerChatText(viewModel, chat)
            ChatTimeAndStatus(chat,false)
        }
    }
}
