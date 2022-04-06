package com.hellomydoc.chat.presentation.userInterface.mainPageContent.pageContent.mainContent


import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.hellomydoc.chat.presentation.userInterface.NotFoundAnimation
import com.hellomydoc.chat.presentation.viewModels.ChatViewModel

@Composable
fun UserIdNotFoundContent(viewModel: ChatViewModel) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ){
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            NotFoundAnimation(viewModel)
            UserNotFoundText(viewModel.userNotFoundString)
        }
    }
}



