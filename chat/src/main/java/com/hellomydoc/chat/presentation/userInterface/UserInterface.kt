package com.hellomydoc.chat.presentation.userInterface

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.hellomydoc.chat.ChatViewModel
import com.hellomydoc.chat.presentation.Style
import com.hellomydoc.chat.presentation.userInterface.mainPageContent.MainPageContent
import com.hellomydoc.chat.presentation.userInterface.mainPageContent.ModalBottomSheetContent
import com.hellomydoc.doctor.ui.theme.HelloMyDocDoctorTheme

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun UserInterface(viewModel: ChatViewModel) {
    HelloMyDocDoctorTheme {
        ModalBottomSheetLayoutContent(viewModel)
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ModalBottomSheetLayoutContent(viewModel: ChatViewModel) {
    val modalBottomSheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    val scope = rememberCoroutineScope()
    ModalBottomSheetLayout(
        sheetContent = {
            ModalBottomSheetContent()
        },
        sheetState = modalBottomSheetState,
        sheetShape = RoundedCornerShape(
            topStart = Style.modalBottomSheetCornerRadius.dp,
            topEnd = Style.modalBottomSheetCornerRadius.dp
        ),
        sheetBackgroundColor = Color.White,
    ) {
        MainPageContent(viewModel,modalBottomSheetState,scope)
    }
}
/////////////////////////////////////////////
