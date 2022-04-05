package com.hellomydoc.chat.presentation.userInterface.mainPageContent.pageContent.mainContent.chatContent.chatsSection.conversations.messageItem.chatMenu

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.hellomydoc.chat.ChatViewModel
import com.hellomydoc.chat.models.Chat

@Composable
fun DeleteMenu(viewModel: ChatViewModel, chat: Chat, onDone: ()->Unit, onDelete: ()->Unit) {
    var ask by remember { mutableStateOf(false) }
    DropdownMenuItem(
        onClick = {
            ask = true
        }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Filled.Delete,
                contentDescription = "Delete",
                tint = Color.Red
            )
            Text(
                "Delete",
                color = Color.Red
            )
        }
        if(ask){
            AlertDialog(
                modifier = Modifier.fillMaxWidth().padding(24.dp),
                onDismissRequest = {
                    ask = false
                    onDone()
                },
                title = {
                    Text(
                        text = "Delete",
                        color = Color.Red,
                        fontWeight = FontWeight.Bold
                    )
                },
                text = {
                    Column() {
                        Text("Are you sure to delete?")
                    }
                },
                buttons = {
                    Row(
                        modifier = Modifier.padding(all = 8.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        TextButton(
                            modifier = Modifier.wrapContentSize(),
                            onClick = {
                                ask = false
                                onDone()
                            },
                            colors = ButtonDefaults.textButtonColors(
                                contentColor = Color.Blue
                            )
                        ) {
                            Text("Dismiss")
                        }

                        TextButton(
                            modifier = Modifier.wrapContentSize(),
                            onClick = {
                                ask = false
                                onDelete()
                                onDone()
                            },
                            colors = ButtonDefaults.textButtonColors(
                                contentColor = Color.Gray
                            )
                        ) {
                            Text("Sure")
                        }
                    }
                }
            )
        }
    }
}