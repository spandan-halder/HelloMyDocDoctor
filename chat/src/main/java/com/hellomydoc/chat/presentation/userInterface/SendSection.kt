package com.hellomydoc.chat.presentation.userInterface.mainPageContent.pageContent.mainContent.chatContent.chatInput.sendSection

import android.content.Context
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.hellomydoc.chat.*
import com.hellomydoc.chat.customIcons.HmdChatSend
import com.hellomydoc.chat.presentation.Style
import com.hellomydoc.chat.presentation.userInterface.values.floatingActionButtonElevations

@Composable
fun SendSection(viewModel: ChatViewModel) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = Style.sendSectionVerticalPadding.dp),
        contentAlignment = Alignment.Center
    ){
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(Style.sendSectionHeight.dp)
                .padding(horizontal = Style.sendSectionHorizontalPadding.dp)
        ){
            AddOrCloseAttachmentSectionButton(viewModel)
            Spacer(modifier = Modifier.width(Style.sendSectionButtonAndInputGap.dp))
            TextInputAndSendSection(viewModel)
        }
    }
}

@Composable
private fun TextInputAndSendSection(viewModel: ChatViewModel) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .clip(CircleShape)
            .background(Style.textInputAndSendBackgroundColor),
        contentAlignment = Alignment.Center
    ){
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ){
            MessageInput(viewModel)
            SendIcon(viewModel)
        }
    }
}


@Composable
private fun RowScope.MessageInput(viewModel: ChatViewModel) {
    val customTextSelectionColors = TextSelectionColors(
        handleColor = Style.inputTextSelectionHandleColor,
        backgroundColor = Style.inputTextSelectionBackgroundColor
    )
    CompositionLocalProvider(LocalTextSelectionColors provides customTextSelectionColors) {
        TextField(
            value = viewModel.inputText.value,
            placeholder = {
                Text(
                    Style.inputTextPlaceholderText,
                    color = Style.inputTextPlaceholderTextColor
                )
            },
            onValueChange = {
                viewModel.inputText.value = it
            },
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .wrapContentHeight()
                .padding(horizontal = Style.inputTextHorizontalPadding.dp),
            colors = inputTextColors(),
            shape = CircleShape,
        )
    }
}

@Composable
private fun inputTextColors(): TextFieldColors {
    return TextFieldDefaults.textFieldColors(
        focusedIndicatorColor = Color.Transparent,
        unfocusedIndicatorColor = Color.Transparent,
        disabledIndicatorColor = Color.Transparent,
        errorIndicatorColor = Color.Transparent,
        cursorColor = Style.inputTextCursorColor,
        backgroundColor = Color.Transparent,
    )
}

@Composable
private fun SendIcon(viewModel: ChatViewModel) {
    val context = LocalContext.current
    IconButton(onClick = {
        sendMessage(context,viewModel,viewModel.inputText.value)
        viewModel.inputText.value = ""
    }) {
        Icon(
            imageVector = Icons.HmdChatSend,
            contentDescription = "Send",
            tint = Style.sendIconTint,
            modifier = Modifier.size(Style.sendIconSize.dp)
        )
    }
    Spacer(modifier = Modifier.width(Style.sendIconRightPadding.dp))
}

private fun sendMessage(context: Context, viewModel: ChatViewModel, text: String) {
    viewModel.insert(context,text)
}

@Composable
private fun AddOrCloseAttachmentSectionButton(viewModel: ChatViewModel) {
    val color = animateColorAsState(targetValue = if(viewModel.attachmentVisible.value) Style.floatingActionButtonAttachmentStateColor else Style.floatingActionButtonAddStateColor)
    val rotation = animateFloatAsState(
        targetValue = if(viewModel.attachmentVisible.value) Style.floatingActionButtonCloseStateRotation else 0f,
        animationSpec = tween(
            durationMillis = Style.attachmentVisibilityAnimationDuration,
            easing = LinearOutSlowInEasing
        ))
    ////////////////////////////////
    FloatingActionButton(
        onClick = {
            viewModel.attachmentVisible.toggle()
        },
        modifier = Modifier
            .size(Style.floatingActionButtonSize.dp)
            .graphicsLayer {
                rotationZ = rotation.value
            },
        backgroundColor = color.value,
        elevation = floatingActionButtonElevations()
    ) {
        FloatingActionButtonIcon(viewModel)
    }
}



@Composable
private fun FloatingActionButtonIcon(viewModel: ChatViewModel) {
    Icon(
        imageVector = Icons.Rounded.Add,
        tint = if(viewModel.attachmentVisible.value) Style.floatingActionButtonIconAddStateColor else Style.FloatingActionButtonCloseStateColor,
        contentDescription = "Show/hide attachment section",
        modifier = Modifier.size(Style.floatingActionButtonIconSize.dp)
    )
}