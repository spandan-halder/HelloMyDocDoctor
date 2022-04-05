package com.hellomydoc.chat.presentation.userInterface.mainPageContent.pageContent.mainContent

import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.hellomydoc.chat.ChatViewModel
import com.hellomydoc.chat.presentation.LottieView
import com.hellomydoc.chat.R
import com.hellomydoc.chat.presentation.Style

@Composable
fun NotFoundAnimation(viewModel: ChatViewModel) {
    val json = R.raw.not_found
    LottieView(
        json = json,
        modifier = Modifier.size(Style.notFoundAnimationSize.dp)
    )
}