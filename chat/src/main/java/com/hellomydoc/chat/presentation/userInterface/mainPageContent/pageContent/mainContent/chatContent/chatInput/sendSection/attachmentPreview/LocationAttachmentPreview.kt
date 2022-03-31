package com.hellomydoc.chat.presentation.userInterface.mainPageContent.pageContent.mainContent.chatContent.chatInput.sendSection.attachmentPreview

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.google.android.gms.maps.model.LatLng
import com.hellomydoc.chat.*
import com.hellomydoc.chat.presentation.Style
import com.hellomydoc.chat.presentation.userInterface.values.getMapImage

@Composable
fun LocationAttachmentPreview(viewModel: ChatViewModel, latLng: LatLng?) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(Style.previewSectionHeight.dp)
            .background(Style.previewSectionBackgroundColor),
        contentAlignment = Alignment.Center
    ){
        LocationPreviewContent(viewModel,latLng)
        PreviewDelete(viewModel)
    }
}

@Composable
private fun LocationPreviewContent(viewModel: ChatViewModel, latLng: LatLng?) {
    Card(
        modifier = Modifier
            .fillMaxHeight()
            .width(Style.previewContentWidth.dp)
    ){
        AsyncImage(
            model = getMapImage(latLng),
            contentDescription = null,
            contentScale = ContentScale.Crop
        )
    }
}
