package com.hellomydoc.chat.presentation.userInterface.mainPageContent.pageContent.mainContent.chatContent.chatsSection.conversations.messageItem.chatMenu

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.hellomydoc.chat.ChatViewModel
import com.hellomydoc.chat.models.Chat

@Composable
fun BoxScope.ChatMenu(viewModel: ChatViewModel, chat: Chat, isMy: Boolean,margin: Boolean) {
    var expanded by remember{ mutableStateOf(false) }
    Box(
        modifier = Modifier
            .wrapContentSize()
            .zIndex(10f)
            .align(Alignment.TopEnd)

    ){
        Column(){
            if(margin){
                Spacer(modifier = Modifier.height(8.dp))
            }
            IconButton(
                onClick = {
                    expanded = true
                },
                modifier = Modifier
                    .padding(end = 12.dp)
                    .size(24.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.KeyboardArrowDown,
                    tint = Color.Gray,
                    contentDescription = "Menu"
                )
            }
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            ViewMenu(chat){
                expanded = false
                viewModel.showDetails(chat)
            }
            if(chat.data?.attachment?.type=="contact"){
                ContactMenus(viewModel,chat){
                    expanded = false
                }
            }
            if(isMy){
                DetailsMenu(viewModel,chat,isMy){
                    expanded = false
                }
                DeleteMenu(viewModel,chat,{
                    expanded = false
                }){
                    viewModel.deleteChat(chat)
                }
            }
        }
    }

}



@Composable
fun ViewMenu(chat: Chat,onDone:()->Unit) {
    DropdownMenuItem(
        onClick = {
            onDone()
        }
    ) {
        Text(
            "View",
            color = Color.Black
        )
    }
}
