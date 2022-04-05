package com.hellomydoc.chat.presentation.userInterface.mainPageContent.pageContent


import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.hellomydoc.chat.*
import com.hellomydoc.chat.presentation.userInterface.mainPageContent.pageContent.mainContent.MainContentSection
import com.hellomydoc.chat.presentation.userInterface.mainPageContent.pageContent.header.HeaderContent

@Composable
fun PageContent(viewModel: ChatViewModel) {
    Column(
        modifier = Modifier.fillMaxSize()
    ){
        HeaderContent(viewModel)
        MainContentSection(viewModel)
    }
}

