package com.hellomydoc.chat.presentation.userInterface


import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.hellomydoc.chat.presentation.viewModels.ChatViewModel

import com.hellomydoc.chat.presentation.userInterface.mainPageContent.pageContent.mainContent.MainContentSection

@Composable
fun PageContent(viewModel: ChatViewModel) {
    Column(
        modifier = Modifier.fillMaxSize()
    ){
        HeaderContent(viewModel)
        MainContentSection(viewModel)
    }
}

