package com.hellomydoc.chat.presentation.userInterface

import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.hellomydoc.chat.presentation.viewModels.ChatViewModel

import com.hellomydoc.chat.R

@Composable
fun NotFoundAnimation(viewModel: ChatViewModel) {
    val json = R.raw.not_found
    LottieView(
        json = json,
        modifier = Modifier.size(Style.notFoundAnimationSize.dp)
    )
}