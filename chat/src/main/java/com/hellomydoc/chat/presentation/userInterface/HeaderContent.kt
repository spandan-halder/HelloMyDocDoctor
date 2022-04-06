package com.hellomydoc.chat.presentation.userInterface


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ChevronLeft
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.hellomydoc.chat.presentation.viewModels.ChatViewModel

@Composable
fun HeaderContent(viewModel: ChatViewModel) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ){
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(Style.headerHeight.dp)
                .background(Style.headerBackgroundColor)
        ){
            HeaderCore(viewModel)
            Divider(
                modifier = Modifier.align(Alignment.BottomCenter)
            )
        }
    }
}

@Composable
private fun HeaderCore(viewModel: ChatViewModel) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(Style.headerCorePadding.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        HeaderBackButton(viewModel)
        PeerIdentityAndStatus(viewModel)
        PeerProfileImage(viewModel)
    }
}

@Composable
private fun PeerProfileImage(viewModel: ChatViewModel) {
    AsyncImage(
        model = viewModel.peerProfileImage.value,
        contentDescription = null,
        modifier = Modifier.clip(CircleShape)
    )
}

@Composable
private fun HeaderBackButton(viewModel: ChatViewModel) {
    IconButton(onClick = {
        viewModel.onBackClick()
    }) {
        Icon(
            imageVector = Icons.Outlined.ChevronLeft,
            contentDescription = "Back",
            modifier = Modifier.size(Style.backButtonSize.dp)
        )
    }
}

@Composable
private fun PeerIdentityAndStatus(viewModel: ChatViewModel) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        PeerName(viewModel)
        PeerStatus(viewModel)
    }
}

@Composable
private fun PeerStatus(viewModel: ChatViewModel) {
    Text(
        viewModel.peerStatus.value,
        color = Style.peerStatusTextColor,
        fontSize = Style.peerStatusTextSize.sp
    )
}

@Composable
private fun PeerName(viewModel: ChatViewModel) {
    Text(
        viewModel.peerName.value,
        fontWeight = FontWeight.Bold,
        color = Style.peerNameTextColor
    )
}