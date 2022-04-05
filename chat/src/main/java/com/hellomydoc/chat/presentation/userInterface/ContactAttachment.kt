package com.hellomydoc.chat.presentation.userInterface.mainPageContent.pageContent.mainContent.chatContent.chatsSection.conversations.messageItem.chatAttachment

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.hellomydoc.chat.*
import com.hellomydoc.chat.models.Chat
import com.hellomydoc.chat.presentation.Style
import com.hellomydoc.chat.presentation.userInterface.mainPageContent.pageContent.mainContent.chatContent.chatsSection.conversations.messageItem.chatMenu.ChatMenu

@Composable
fun ContactAttachment(viewModel: ChatViewModel, chat: Chat, isMy: Boolean) {
    val name = chat.data?.attachment?.name?:return
    val phone = chat.data?.attachment?.phone?:return
    Box(
        modifier = Modifier
            .wrapContentSize()
    ){
        Card(
            modifier = Modifier
                .wrapContentSize()
                .padding(vertical = 6.dp),
            backgroundColor = if(isMy) Style.myChatCardBackgroundColor else Style.peerChatCardBackgroundColor,
            shape = RoundedCornerShape(12.dp),
        ){
            Column(
                modifier = Modifier
                    .wrapContentSize()
                    .padding(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                ContactCardHeader(isMy)
                Divider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    color = Color.LightGray,
                    thickness = 0.5.dp
                )
                ContactDetails(isMy,name,phone)
            }
        }
        if(chat.data?.attachment!=null){
            ChatMenu(viewModel, chat, isMy,true)
        }
    }
}

@Composable
private fun ContactDetails(isMy: Boolean, name: String, phone: String) {
    Text(
        name,
        color = if(isMy) Color.White else Color.Black,
        fontWeight = FontWeight.Bold
    )
    Spacer(
        modifier = Modifier.height(8.dp)
    )
    Text(
        phone,
        color = if(isMy) Color.White else Color.Black,
    )
}

@Composable
private fun ContactCardHeader(isMy: Boolean) {
    Text(
        "Contact",
        color = if(isMy) Color.White else Color.Black,
        fontWeight = FontWeight.Bold
    )
}