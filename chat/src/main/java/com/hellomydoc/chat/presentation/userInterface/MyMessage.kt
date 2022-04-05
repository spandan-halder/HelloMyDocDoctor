package com.hellomydoc.chat.presentation.userInterface.mainPageContent.pageContent.mainContent.chatContent.chatsSection.conversations.messageItem.mychat

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import com.hellomydoc.chat.ChatViewModel
import com.hellomydoc.chat.models.Chat
import com.hellomydoc.chat.presentation.Style
import com.hellomydoc.chat.presentation.userInterface.mainPageContent.pageContent.mainContent.chatContent.chatsSection.conversations.messageItem.ChatDate
import com.hellomydoc.chat.presentation.userInterface.mainPageContent.pageContent.mainContent.chatContent.chatsSection.conversations.messageItem.chatAttachment.ChatAttachment
import com.hellomydoc.chat.presentation.userInterface.mainPageContent.pageContent.mainContent.chatContent.chatsSection.conversations.messageItem.chatMenu.ChatMenu
import com.hellomydoc.chat.presentation.userInterface.mainPageContent.pageContent.mainContent.chatContent.chatsSection.conversations.messageItem.chatTimeAndStatus.ChatTimeAndStatus

@Composable
fun MyMessage(viewModel: ChatViewModel, chat: Chat) {
    MyMessageContent(viewModel,chat)
}

@Composable
private fun MyMessageContent(viewModel: ChatViewModel, chat: Chat) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(
                horizontal = Style.myChatHorizontalMargin.dp,
                vertical = Style.myChatVerticalMargin.dp
            )
    ){
        val configuration = LocalConfiguration.current
        Column(
            modifier = Modifier
                .width((configuration.screenWidthDp * Style.messageCardMaxSizeFactor).dp)
                .align(Alignment.CenterEnd),
            horizontalAlignment = Alignment.End
        ){
            ChatDate(viewModel,chat)
            ChatAttachment(viewModel,chat)
            MyChatText(viewModel,chat)
            ChatTimeAndStatus(chat,true)
        }
    }
}


data class DateTimeAndShowDate(
    val date: String,
    val showDate: Boolean,
    val time: String
)

