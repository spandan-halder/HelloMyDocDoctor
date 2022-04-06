package com.hellomydoc.chat.presentation.userInterface.mainPageContent.pageContent.mainContent.chatContent.chatsSection.conversations.messageItem.chatTimeAndStatus

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.hellomydoc.chat.models.Chat
import com.hellomydoc.chat.presentation.userInterface.Style
import com.hellomydoc.chat.presentation.userInterface.mainPageContent.pageContent.mainContent.chatContent.chatsSection.conversations.messageItem.mychat.MyChatStatus
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import org.joda.time.format.DateTimeFormat

@Composable
fun ChatTimeAndStatus(chat: Chat, isMy: Boolean) {
    Spacer(
        modifier = Modifier.height(Style.timeTopMargin.dp)
    )
    Row(
        verticalAlignment = Alignment.CenterVertically
    ){
        val time = DateTime(chat.createdAt, DateTimeZone.UTC).toDateTime(DateTimeZone.getDefault()).toString(
            DateTimeFormat.forPattern("hh:mm a")
        )
        ChatTime(time)
        if(isMy){
            MyChatStatus(chat)
        }
    }
}




private fun formatDisplayDate(dateTime: DateTime): String {
    val displayDate = dateTime.toString(
        DateTimeFormat.forPattern(Style.dateDisplayFormat)
    )
    val parts = displayDate.split(" ")
    var dis = ""
    if(parts.size==3){
        val day = parts[0]
        val suffix = when(day.last()){
            '1'->"st"
            '2'->"nd"
            '3'->"rd"
            else->"th"
        }
        dis = "${parts[0]}$suffix ${parts[1]} ${parts[2]}"
    }
    return dis
}