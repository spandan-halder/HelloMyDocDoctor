package com.hellomydoc.chat.presentation.userInterface.mainPageContent.pageContent.mainContent.chatContent.chatsSection

import androidx.compose.foundation.layout.Column
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import com.hellomydoc.chat.ChatViewModel
import com.hellomydoc.chat.presentation.Style
import com.hellomydoc.chat.presentation.userInterface.mainPageContent.pageContent.mainContent.NotFoundAnimation

@Composable
fun NoConversationYet(viewModel: ChatViewModel) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        NotFoundAnimation(viewModel)
        Text(Style.noConversationYetText)
        OutlinedButton(
            onClick = {
                viewModel.sayHi()
            },
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = Style.sayHiButtonTextColor
            )
        ) {
            Text(
                Style.sayHiText
            )
        }
    }
}