package com.hellomydoc.chat.presentation.userInterface.mainPageContent.pageContent.mainContent.chatContent.chatsSection.conversations.messageItem.chatTimeAndStatus

import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.unit.sp
import com.hellomydoc.chat.presentation.Style

@Composable
fun ChatTime(time: String) {
    Text(
        time,
        color = Style.timeColor,
        fontSize = Style.timeFontSize.sp
    )
}