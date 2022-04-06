package com.hellomydoc.chat.presentation.userInterface

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hellomydoc.chat.models.Chat
import com.hellomydoc.chat.presentation.viewModels.ChatViewModel

@Composable
fun ColumnScope.ChatDate(viewModel: ChatViewModel, chat: Chat) {
    if(viewModel.chatIdsToShowDate.containsKey(chat.chatId)){
        Card(
            modifier = Modifier
                .wrapContentSize()
                .align(Alignment.CenterHorizontally)
                .padding(vertical = Style.dateVerticalMargin.dp)
            ,
            backgroundColor = Style.dateCardBackgroundColor,
            shape = CircleShape,
            elevation = Style.chatDateCardElevation.dp
        ){
            Text(
                viewModel.chatIdsToShowDate[chat.chatId]?:"",
                color = Style.dateColor,
                fontSize = Style.timeFontSize.sp,
                modifier = Modifier.padding(Style.dateTextMargin.dp)
            )
        }
    }
}