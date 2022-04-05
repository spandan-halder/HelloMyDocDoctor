package com.hellomydoc.chat.presentation.userInterface.values

import androidx.compose.material.FloatingActionButtonDefaults
import androidx.compose.material.FloatingActionButtonElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.hellomydoc.chat.presentation.Style

@Composable
fun floatingActionButtonElevations(): FloatingActionButtonElevation {
    return FloatingActionButtonDefaults.elevation(
        defaultElevation = Style.floatingActionButtonElevation.dp,
        pressedElevation = Style.floatingActionButtonPressedElevation.dp
    )
}