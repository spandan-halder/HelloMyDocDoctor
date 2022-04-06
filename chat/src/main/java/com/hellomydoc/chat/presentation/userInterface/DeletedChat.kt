package com.hellomydoc.chat.presentation.userInterface

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Block
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hellomydoc.chat.models.Chat
import com.hellomydoc.chat.presentation.userInterface.Style

@Composable
fun DeletedChat(chat: Chat) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(Style.chatTextPadding.dp)
    ) {
        Icon(
            imageVector = Icons.Outlined.Block,
            contentDescription = "Deleted",
            tint = Color.Gray
        )
        Text(
            "Deleted",
            color = Color.Gray,
            fontSize = 14.sp,
            fontStyle = FontStyle.Italic
        )
    }
}