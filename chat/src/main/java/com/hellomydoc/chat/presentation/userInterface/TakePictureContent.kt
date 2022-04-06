package com.hellomydoc.chat.presentation.userInterface.mainPageContent.pageContent.mainContent.chatContent.chatInput.attachmentSelection

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.options
import com.hellomydoc.chat.customIcons.HmdCamera
import com.hellomydoc.chat.presentation.userInterface.Style
import com.hellomydoc.chat.presentation.userInterface.values.floatingActionButtonElevations
import com.hellomydoc.chat.presentation.viewModels.ChatViewModel

@Composable
fun TakePictureContent(viewModel: ChatViewModel) {
    val context = LocalContext.current
    val launcher =
        rememberLauncherForActivityResult(CropImageContract()) { result ->
            if(result.isSuccessful){
                result.uriContent?.let {
                    viewModel.onNewImageUri(it,result.getUriFilePath(context))
                }
            }
        }
    FloatingActionButton(
        onClick = {
            launcher.launch(options {
                setImageSource(includeGallery = true, includeCamera = true)
            })
        },
        modifier = Modifier.size(Style.floatingActionButtonSize.dp),
        backgroundColor = Style.FloatingActionButtonCloseStateColor,
        elevation = floatingActionButtonElevations()
    ) {
        Icon(
            imageVector = Icons.HmdCamera,
            tint = Style.floatingActionButtonIconAddStateColor,
            contentDescription = "Take picture",
            modifier = Modifier.size(24.dp)
        )
    }
}