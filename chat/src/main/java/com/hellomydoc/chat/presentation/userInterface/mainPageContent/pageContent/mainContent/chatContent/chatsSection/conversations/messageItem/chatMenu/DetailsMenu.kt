package com.hellomydoc.chat.presentation.userInterface.mainPageContent.pageContent.mainContent.chatContent.chatsSection.conversations.messageItem.chatMenu

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.hellomydoc.chat.ChatViewModel
import com.hellomydoc.chat.models.Chat
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import org.joda.time.format.DateTimeFormat

@Composable
fun DetailsMenu(viewModel: ChatViewModel, chat: Chat, isMy: Boolean, onDone:()->Unit) {
    var show by remember { mutableStateOf(false) }
    DropdownMenuItem(
        onClick = {
            show = true
        }
    ) {
        Text(
            "Details",
            color = Color.Black
        )
    }
    if(show){
        Dialog(
            onDismissRequest = {
                onDone()
            }
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                backgroundColor = Color.White
            ){
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp)
                ){
                    Text(
                        "Details",
                        color = Color.Black,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )
                    when{
                        chat.seenAt>0L->{
                            Delivered(chat)
                            Seen(chat)
                        }
                        chat.receivedAt>0L->{
                            Delivered(chat)
                        }
                        chat.arrivedServerAt>0L->{
                            ArrivedSever(chat)
                        }
                        else->{
                            Waiting(chat)
                        }
                    }
                }
            }
        }
    }
}

fun millisToTime(input: Long):String{
    return DateTime(input, DateTimeZone.UTC).toDateTime(DateTimeZone.getDefault()).toString(
        DateTimeFormat.forPattern("hh:mm a")
    )
}

@Composable
fun Seen(chat: Chat) {
    Column(){
        Icon(
            imageVector = Icons.Filled.DoneAll,
            tint = Color.Blue,
            contentDescription = "Seen",
            modifier = Modifier
                .size(24.dp)
                .padding(horizontal = 4.dp)
        )
        Text(
            millisToTime(chat.seenAt),
            color = Color.Gray
        )
    }
}

@Composable
fun Delivered(chat: Chat) {
    Column(){
        Icon(
            imageVector = Icons.Filled.DoneAll,
            tint = Color.Gray,
            contentDescription = "Delivered",
            modifier = Modifier
                .size(24.dp)
                .padding(horizontal = 4.dp)
        )
        Text(
            millisToTime(chat.receivedAt),
            color = Color.Gray
        )
    }
}

@Composable
fun ArrivedSever(chat: Chat) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Filled.Done,
            tint = Color.Gray,
            contentDescription = "Arrived at server",
            modifier = Modifier
                .size(24.dp)
                .padding(horizontal = 4.dp)
        )
        Text(
            "Arrived at server",
            color = Color.Gray
        )
    }
}

@Composable
fun Waiting(chat: Chat) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Filled.AccessTime,
            tint = Color.Gray,
            contentDescription = "Waiting",
            modifier = Modifier
                .size(24.dp)
                .padding(horizontal = 4.dp)
        )
        Text(
            "Waiting...",
            color = Color.Gray
        )
    }
}

