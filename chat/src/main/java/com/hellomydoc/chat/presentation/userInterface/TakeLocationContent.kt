package com.hellomydoc.chat.presentation.userInterface.mainPageContent.pageContent.mainContent.chatContent.chatInput.attachmentSelection

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.hellomydoc.chat.customIcons.HmdLocation
import com.hellomydoc.chat.presentation.userInterface.Style
import com.hellomydoc.chat.presentation.userInterface.values.floatingActionButtonElevations
import com.hellomydoc.chat.presentation.viewModels.ChatViewModel

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun TakeLocationContent(viewModel: ChatViewModel) {
    FloatingActionButton(
        onClick = {
            viewModel.onLocationPickClick()
        },
        modifier = Modifier.size(Style.floatingActionButtonSize.dp),
        backgroundColor = Style.FloatingActionButtonCloseStateColor,
        elevation = floatingActionButtonElevations()
    ) {
        Icon(
            imageVector = Icons.HmdLocation,
            tint = Style.floatingActionButtonIconAddStateColor,
            contentDescription = "Take location",
            modifier = Modifier.size(24.dp)
        )
    }
}