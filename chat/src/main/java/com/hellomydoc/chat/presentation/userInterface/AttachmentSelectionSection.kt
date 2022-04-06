package com.hellomydoc.chat.presentation.userInterface.mainPageContent.pageContent.mainContent.chatContent.chatInput.attachmentSelection

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.hellomydoc.chat.presentation.userInterface.Style
import com.hellomydoc.chat.presentation.viewModels.ChatViewModel

@Composable
fun AttachmentSelectionSection(viewModel: ChatViewModel) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = Style.sendSectionHorizontalPadding.dp)
    ){
        TakePictureContent(viewModel)
        Spacer(modifier = Modifier.width(Style.floatingActionButtonsGap.dp))
        TakeFileContent(viewModel)
        Spacer(modifier = Modifier.width(Style.floatingActionButtonsGap.dp))
        TakeLocationContent(viewModel)
        Spacer(modifier = Modifier.width(Style.floatingActionButtonsGap.dp))
        TakeContactContent(viewModel)
    }
}