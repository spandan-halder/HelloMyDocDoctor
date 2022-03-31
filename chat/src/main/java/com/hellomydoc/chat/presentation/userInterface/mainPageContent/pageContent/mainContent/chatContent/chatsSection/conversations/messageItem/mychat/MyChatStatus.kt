package com.hellomydoc.chat.presentation.userInterface.mainPageContent.pageContent.mainContent.chatContent.chatsSection.conversations.messageItem.mychat

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.hellomydoc.chat.models.Chat

@Composable
fun MyChatStatus(chat: Chat) {
    when{
        chat.seenAt>0->{
            MyChatSeenStatus()
        }
        chat.receivedAt>0->{
            MyChatReceivedStatus()
        }
        chat.arrivedServerAt>0->{
            MyChatArrivedStatus()
        }
        else->{
            MyChatWaitingStatus()
        }
    }
}

@Composable
private fun MyChatWaitingStatus() {
    Icon(
        imageVector = Icons.Filled.AccessTime,
        tint = Color.Gray,
        contentDescription = "Waiting",
        modifier = Modifier
            .size(24.dp)
            .padding(horizontal = 4.dp)
    )
}

@Composable
private fun MyChatArrivedStatus() {
    Icon(
        imageVector = Icons.Filled.Done,
        tint = Color.Gray,
        contentDescription = "Arrived at server",
        modifier = Modifier
            .size(24.dp)
            .padding(horizontal = 4.dp)
    )
}

@Composable
private fun MyChatReceivedStatus() {
    Icon(
        imageVector = Icons.Filled.DoneAll,
        tint = Color.Gray,
        contentDescription = "Received",
        modifier = Modifier
            .size(24.dp)
            .padding(horizontal = 4.dp)
    )
}

@Composable
private fun MyChatSeenStatus() {
    Icon(
        imageVector = Icons.Filled.DoneAll,
        tint = Color(0xff035efc),
        contentDescription = "Seen",
        modifier = Modifier
            .size(24.dp)
            .padding(horizontal = 4.dp)
    )
}