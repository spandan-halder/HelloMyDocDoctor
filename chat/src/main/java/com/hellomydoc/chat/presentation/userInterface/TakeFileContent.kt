package com.hellomydoc.chat.presentation.userInterface.mainPageContent.pageContent.mainContent.chatContent.chatInput.attachmentSelection

import android.app.Activity
import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.hellomydoc.chat.*
import com.hellomydoc.chat.customIcons.HmdFile
import com.hellomydoc.chat.presentation.Style
import com.hellomydoc.chat.presentation.userInterface.values.floatingActionButtonElevations

@Composable
fun TakeFileContent(viewModel: ChatViewModel) {
    val intent = Intent(Intent.ACTION_GET_CONTENT)
    intent.type = "application/pdf"
    intent.addCategory(Intent.CATEGORY_OPENABLE)
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if(it.resultCode == Activity.RESULT_OK){
            val path = it.data?.data.toString()
            if(path!=null){
                viewModel.onNewFileUri(path)
            }
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
            imageVector = Icons.HmdFile,
            tint = Style.floatingActionButtonIconAddStateColor,
            contentDescription = "Take file",
            modifier = Modifier.size(24.dp)
        )
    }
}