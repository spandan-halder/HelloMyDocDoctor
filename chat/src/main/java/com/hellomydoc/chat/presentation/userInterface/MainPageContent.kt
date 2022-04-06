package com.hellomydoc.chat.presentation.userInterface


import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.hellomydoc.chat.presentation.viewModels.ChatViewModel
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












