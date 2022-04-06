package com.hellomydoc.chat.presentation.userInterface

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.hellomydoc.chat.presentation.viewModels.ChatViewModel
import com.hellomydoc.chat.models.Chat

@Composable
fun ChatAttachment(viewModel: ChatViewModel, chat: Chat, isMy: Boolean = true) {
    if(chat.deleted){
        return
    }
    when(chat.data?.attachment?.type){
        "contact"->{
            ContactAttachment(viewModel,chat,isMy)
        }
        "location"->{
            LocationAttachment(viewModel,chat,isMy)
        }
        "image"->{
            ImageAttachment(viewModel,chat,isMy)
        }
        "doc"->{
            DocAttachment(viewModel,chat,isMy)
        }
        else->{
            return
        }
    }
    Spacer(
        modifier = Modifier.height(8.dp)
    )
}









