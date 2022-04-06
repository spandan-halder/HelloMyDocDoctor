package com.hellomydoc.chat.presentation.userInterface


import androidx.compose.runtime.*
import com.hellomydoc.chat.presentation.viewModels.AttachmentType
import com.hellomydoc.chat.presentation.viewModels.ChatViewModel

@Composable
fun AttachmentPreviewSection(viewModel: ChatViewModel) {
    when(viewModel.attachment.value.type){
        AttachmentType.IMAGE -> ImageAttachmentPreview(viewModel, viewModel.attachment.value.uri)
        AttachmentType.FILE -> FileAttachmentPreview(viewModel, viewModel.attachment.value.uri)
        AttachmentType.LOCATION -> LocationAttachmentPreview(viewModel,viewModel.attachment.value.latLng)
        AttachmentType.CONTACT -> ContactAttachmentPreview(viewModel,viewModel.attachment.value.contact)
        AttachmentType.NONE -> {}
    }
}