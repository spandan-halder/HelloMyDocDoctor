package com.hellomydoc.chat.presentation.userInterface

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ChevronLeft
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImage
import com.hellomydoc.chat.presentation.viewModels.ChatViewModel

@Composable
fun ImageViewer(viewModel: ChatViewModel) {
    if(viewModel.currentlyShowingImageUrl.value.isNotEmpty()){
        val scale = remember { mutableStateOf(1f) }
        //val rotationState = remember { mutableStateOf(1f) }
        val translationX = remember { mutableStateOf(0f) }
        val translationY = remember { mutableStateOf(0f) }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
                .pointerInput(Unit) {
                    detectTransformGestures { centroid, pan, zoom, rotation ->
                        scale.value *= zoom
                        //rotationState.value += rotation
                        translationX.value += pan.x
                        translationY.value += pan.y
                    }
                }
        ) {
            AsyncImage(
                model = viewModel.currentlyShowingImageUrl.value,
                contentDescription = "Detailed image",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer(
                        scaleX = maxOf(.5f, minOf(3f, scale.value)),
                        scaleY = maxOf(.5f, minOf(3f, scale.value)),
                        //rotationZ = rotationState.value,
                        translationX = translationX.value,
                        translationY = translationY.value
                    )
            )
            IconButton(
                onClick = {
                    viewModel.hideImageViewer()
                },
                modifier = Modifier.background(Color.Black.copy(alpha = 0.3f))
            ) {
                Icon(
                    imageVector = Icons.Outlined.ChevronLeft,
                    contentDescription = "Back",
                    tint = Color.White
                )
            }
        }
    }
}