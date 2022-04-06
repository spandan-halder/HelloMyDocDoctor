package com.hellomydoc.chat.presentation.userInterface

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.hellomydoc.chat.presentation.viewModels.ChatViewModel

import com.hellomydoc.chat.models.Chat

@Composable
fun PeerChatText(viewModel: ChatViewModel, chat: Chat) {
    if(chat.data?.text?.isEmpty()==true&&!chat.deleted){
        return
    }
    Card(
        backgroundColor = Style.peerChatCardBackgroundColor,
        shape = RoundedCornerShape(
            topEnd = Style.chatCardCornerRadius.dp,
            topStart = Style.chatCardCornerRadius.dp,
            bottomStart = 0.dp,
            bottomEnd = Style.chatCardCornerRadius.dp
        )
    ){
        Row(
            verticalAlignment = Alignment.CenterVertically
        ){
            if(chat.deleted){
                DeletedChat(chat)
            }
            else{
                Text(
                    chat.data?.text?:"",
                    color = Style.peerChatTextColor,
                    modifier = Modifier.padding(Style.chatTextPadding.dp)
                )
                if(chat.data?.attachment==null){
                    Box(){
                        ChatMenu(viewModel,chat,false,false)
                    }
                }
            }
        }
    }
}