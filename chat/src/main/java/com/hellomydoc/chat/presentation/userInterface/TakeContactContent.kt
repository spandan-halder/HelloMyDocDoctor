package com.hellomydoc.chat.presentation.userInterface.mainPageContent.pageContent.mainContent.chatContent.chatInput.attachmentSelection

import android.app.Activity
import android.content.Intent
import android.provider.ContactsContract
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.hellomydoc.chat.customIcons.HmdUser
import com.hellomydoc.chat.presentation.userInterface.Style
import com.hellomydoc.chat.presentation.userInterface.values.floatingActionButtonElevations
import com.hellomydoc.chat.presentation.viewModels.ChatViewModel

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun TakeContactContent(viewModel: ChatViewModel) {
    val intent = Intent(
        Intent.ACTION_PICK,
        ContactsContract.CommonDataKinds.Phone.CONTENT_URI
    )
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if(it.resultCode == Activity.RESULT_OK){
            val uri = it.data?.data?:return@rememberLauncherForActivityResult
            val cursor = context.contentResolver.query(uri, null, null, null, null)
            cursor?.moveToFirst()
            val phoneIndex =
                cursor?.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)?:return@rememberLauncherForActivityResult
            val nameIndex =
                cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)?:return@rememberLauncherForActivityResult
            val phoneNo = cursor.getString(phoneIndex)?:""
            val name = cursor.getString(nameIndex)?:""
            viewModel.onContactPicked(phoneNo,name)
        }
    }
    FloatingActionButton(
        onClick = {
            launcher.launch(intent)
        },
        modifier = Modifier.size(Style.floatingActionButtonSize.dp),
        backgroundColor = Style.FloatingActionButtonCloseStateColor,
        elevation = floatingActionButtonElevations()
    ) {
        Icon(
            imageVector = Icons.HmdUser,
            tint = Style.floatingActionButtonIconAddStateColor,
            contentDescription = "Take location",
            modifier = Modifier.size(24.dp)
        )
    }
}