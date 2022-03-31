package com.hellomydoc.chat.presentation.userInterface.mainPageContent.pageContent.mainContent

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.hellomydoc.chat.ChatViewModel

@Composable
fun PeerIdNotFoundContent(viewModel: ChatViewModel) {
    val text = viewModel.peerNotFoundText
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ){
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            NotFoundAnimation(viewModel)
            UserNotFoundText(text)
        }
    }
}