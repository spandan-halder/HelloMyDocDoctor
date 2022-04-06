package com.hellomydoc.chat.presentation.userInterface


import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.hellomydoc.chat.presentation.viewModels.ChatViewModel

@Composable
fun ChatContent(viewModel: ChatViewModel) {
    Box(
        modifier = Modifier.fillMaxSize()
    ){
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            ChatsSection(viewModel)
            ChatInputSection(viewModel)
        }
    }
}



