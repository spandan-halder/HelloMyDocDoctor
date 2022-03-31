package com.hellomydoc.chat.presentation.userInterface.mainPageContent.pageContent.mainContent.chatContent.chatsSection.conversations.messageItem.chatMenu

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.provider.ContactsContract
import android.widget.Toast
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat.startActivity
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberPermissionState
import com.hellomydoc.chat.ChatViewModel
import com.hellomydoc.chat.models.Chat


@Composable
fun ContactMenus(viewModel: ChatViewModel, chat: Chat,onDone: ()->Unit) {
    CallMenu(viewModel,chat,onDone)
    SaveContactMenu(viewModel,chat,onDone)
    SmsMenu(viewModel, chat, onDone)
}
@Composable
fun SmsMenu(viewModel: ChatViewModel, chat: Chat, onDone: () -> Unit) {
    val name = chat.data?.attachment?.name?:return
    val phone = chat.data?.attachment?.phone?:return
    if(phone.isEmpty()){
        return
    }
    val context = LocalContext.current
    DropdownMenuItem(
        onClick = {
            try {
                val uri = Uri.parse("smsto:$phone")
                val intent = Intent(Intent.ACTION_SENDTO, uri)
                intent.putExtra("sms_body", "Hi, $name...")
                context.startActivity(intent)
            } catch (e: Exception) {
                Toast.makeText(
                    context,
                    "Could not call",
                    Toast.LENGTH_SHORT
                ).show()
            }
            onDone()
        }
    ) {
        Text(
            "SMS",
            color = Color.Black
        )
    }
}

@Composable
fun SaveContactMenu(viewModel: ChatViewModel, chat: Chat, onDone: () -> Unit) {
    val name = chat.data?.attachment?.name?:return
    val phone = chat.data?.attachment?.phone?:return
    if(name.isEmpty()||phone.isEmpty()){
        return
    }
    val context = LocalContext.current
    DropdownMenuItem(
        onClick = {
            val intent = Intent(Intent.ACTION_INSERT)
            intent.type = ContactsContract.Contacts.CONTENT_TYPE

            intent.putExtra(ContactsContract.Intents.Insert.NAME, name)
            intent.putExtra(ContactsContract.Intents.Insert.PHONE, phone)

            context.startActivity(intent)
            onDone()
        }
    ) {
        Text(
            "Save",
            color = Color.Black
        )
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CallMenu(viewModel: ChatViewModel, chat: Chat, onDone: () -> Unit) {
    val context = LocalContext.current
    val locationPermissionState = rememberPermissionState(
        Manifest.permission.CALL_PHONE,
        onPermissionResult = {
            if(it){
                val phone = chat.data?.attachment?.phone?:""
                if(phone.isEmpty()){
                    onDone()
                }
                else{
                    try {
                        val callIntent = Intent(Intent.ACTION_CALL)
                        callIntent.data = Uri.parse("tel:$phone")
                        context.startActivity(callIntent)
                    } catch (e: Exception) {
                        Toast.makeText(
                            context,
                            "Could not call",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    onDone()
                }
            }
            else{
                Toast.makeText(
                    context,
                    "Calling permission is needed to make a call",
                    Toast.LENGTH_SHORT
                ).show()
                onDone()
            }
        }
    )

    DropdownMenuItem(
        onClick = {
            if (locationPermissionState.status==PermissionStatus.Granted) {
                val phone = chat.data?.attachment?.phone?:""
                if(phone.isEmpty()){
                    onDone()
                }
                else{
                    try {
                        val callIntent = Intent(Intent.ACTION_CALL)
                        callIntent.data = Uri.parse("tel:$phone")
                        context.startActivity(callIntent)
                    } catch (e: Exception) {
                        Toast.makeText(
                            context,
                            "Could not call",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    onDone()
                }
            }
            else{
                locationPermissionState.launchPermissionRequest()
            }
            //onDone()
        }
    ) {
        Text(
            "Call",
            color = Color.Black
        )
    }
}
