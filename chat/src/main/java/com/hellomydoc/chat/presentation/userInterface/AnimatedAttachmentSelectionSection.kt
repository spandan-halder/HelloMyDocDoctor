package com.hellomydoc.chat.presentation.userInterface

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.hellomydoc.chat.presentation.userInterface.mainPageContent.pageContent.mainContent.chatContent.chatInput.attachmentSelection.AttachmentSelectionSection
import com.hellomydoc.chat.presentation.viewModels.ChatViewModel

@Composable
fun AnimatedAttachmentSelectionSection(viewModel: ChatViewModel) {
    var heightValue by remember {
        mutableStateOf(0f)
    }
    heightValue = if(viewModel.attachmentVisible.value) Style.attachmentSelectionHeight else 0f
    val height = animateFloatAsState(
        targetValue = heightValue,
        animationSpec = tween(
            durationMillis = Style.attachmentVisibilityAnimationDuration,
            easing = LinearOutSlowInEasing
        )
    )
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(height.value.dp)
            .background(Color.White)
    ){
        AttachmentSelectionSection(viewModel)
    }
}





