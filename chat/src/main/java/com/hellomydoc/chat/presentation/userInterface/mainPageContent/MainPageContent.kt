package com.hellomydoc.chat.presentation.userInterface.mainPageContent


import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ChevronLeft
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImage
import com.hellomydoc.chat.*
import com.hellomydoc.chat.presentation.Style
import com.hellomydoc.chat.presentation.userInterface.mainPageContent.pageContent.PageContent
import kotlinx.coroutines.CoroutineScope

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MainPageContent(
    viewModel: ChatViewModel,
    modalBottomSheetState: ModalBottomSheetState,
    scope: CoroutineScope
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Style.chatPageBackgroundColor
    ) {
        LaunchedEffect(key1 =Unit){
            viewModel.onScope(scope)
            viewModel.onBottomSheetState(modalBottomSheetState)
        }
        PageContent(viewModel)
        MapLocationPickerContent(viewModel)
        ImageViewer(viewModel)
    }
}



/////////////////////////












