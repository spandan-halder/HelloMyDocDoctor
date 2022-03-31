package com.hellomydoc.chat.presentation.userInterface.mainPageContent.pageContent.mainContent

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight

@Composable
fun UserNotFoundText(text: String) {
    Text(
        text,
        color = Color.Black,
        fontWeight = FontWeight.Black
    )
}