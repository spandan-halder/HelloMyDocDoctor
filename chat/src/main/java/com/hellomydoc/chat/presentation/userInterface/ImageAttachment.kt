package com.hellomydoc.chat.presentation.userInterface

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.hellomydoc.chat.*
import com.hellomydoc.chat.models.Chat
import com.hellomydoc.chat.presentation.userInterface.mainPageContent.pageContent.mainContent.chatContent.chatsSection.conversations.messageItem.chatAttachment.UserCancelledAttachment
import com.hellomydoc.chat.presentation.userInterface.mainPageContent.pageContent.mainContent.chatContent.chatsSection.conversations.messageItem.chatAttachment.imageAttachment.ErrorAttachment
import com.hellomydoc.chat.presentation.userInterface.mainPageContent.pageContent.mainContent.chatContent.chatsSection.conversations.messageItem.chatAttachment.imageAttachment.UnknownErrorAttachment
import com.hellomydoc.chat.presentation.userInterface.mainPageContent.pageContent.mainContent.chatContent.chatsSection.conversations.messageItem.chatAttachment.imageAttachment.UploadProgressAttachment
import com.hellomydoc.chat.presentation.viewModels.ChatViewModel

@Composable
fun ImageAttachment(viewModel: ChatViewModel, chat: Chat, isMy: Boolean) {
    val name = chat.data?.attachment?.name?:return
    Box(){
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f),
            backgroundColor = if(isMy) Style.myChatCardBackgroundColor else Style.peerChatCardBackgroundColor,
            shape = RoundedCornerShape(12.dp),
        ){
            val progress = chat.data?.attachment?.progress?:return@Card
            when(progress){
                ChatBox.FileUploadError.cancelled->{
                    UserCancelledAttachment(isMy)
                }
                ChatBox.FileUploadError.error->{
                    ErrorAttachment(isMy)
                }
                ChatBox.FileUploadError.unknown->{
                    UnknownErrorAttachment(isMy)
                }
                ChatBox.FileUploadError.success->{
                    SuccessImageAttachment(viewModel,isMy,name)
                }
                else->{
                    UploadProgressAttachment(viewModel,isMy,progress)
                }
            }
        }
        if(chat.data?.attachment!=null){
            ChatMenu(viewModel, chat, isMy,true)
        }
    }
}


