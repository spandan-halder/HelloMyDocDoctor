package com.hellomydoc.chat.presentation.userInterface.mainPageContent.pageContent.mainContent.chatContent.chatsSection.conversations.messageItem.chatAttachment

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.google.android.gms.maps.model.LatLng
import com.hellomydoc.chat.ChatViewModel
import com.hellomydoc.chat.models.Chat
import com.hellomydoc.chat.presentation.Style
import com.hellomydoc.chat.presentation.userInterface.mainPageContent.pageContent.mainContent.chatContent.chatsSection.conversations.messageItem.chatMenu.ChatMenu
import com.hellomydoc.chat.presentation.userInterface.values.getMapImage
import kotlin.math.abs
import kotlin.math.sign


@Composable
fun LocationAttachment(viewModel: ChatViewModel, chat: Chat, isMy: Boolean) {
    val lat = chat.data?.attachment?.location?.lat?:return
    val lng = chat.data?.attachment?.location?.lng?:return
    val context = LocalContext.current
    Box(
        modifier = Modifier
            .fillMaxWidth()
    ){
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f),
            backgroundColor = if(isMy) Style.myChatCardBackgroundColor else Style.peerChatCardBackgroundColor,
            shape = RoundedCornerShape(12.dp),
        ){
            AsyncImage(
                model = getMapImage(LatLng(lat,lng),15),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .clickable {
                        try {
                            val gmmIntentUri: Uri =
                                Uri.parse("google.streetview:cbll=$lat,$lng")
                            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                            mapIntent.setPackage("com.google.android.apps.maps")
                            context.startActivity(mapIntent)
                        } catch (e: Exception) {
                            val url = "https://www.google.com/maps/place/${decimalToDMS(lat,true)}+${decimalToDMS(lng,false)}"
                            val i = Intent(Intent.ACTION_VIEW)
                            i.data = Uri.parse(url)
                            context.startActivity(i)
                        }

                    },
                placeholder = painterResource(id = viewModel.mapPlaceholderImageId)
            )
        }
        if(chat.data?.attachment!=null){
            ChatMenu(viewModel, chat, isMy,true)
        }
    }
}

fun decimalToDMS(coord: Double,lat: Boolean): String {
    val sign = sign(coord)
    var coord = abs(coord)
    val output: String
    val degrees: String
    val minutes: String
    val seconds: String
    var mod = coord % 1
    var intPart = coord.toInt()
    degrees = intPart.toString()
    coord = mod * 60
    mod = coord % 1
    intPart = coord.toInt()
    if (intPart < 0) {
        intPart *= -1
    }
    minutes = intPart.toString()
    coord = mod * 60
    intPart = coord.toInt()
    if (intPart < 0) {
        intPart *= -1
    }
    seconds = intPart.toString()
    val suffix = getSuffix(lat,sign)
    output = "$degrees°$minutes'$seconds\"$suffix"
    return output
}

fun getSuffix(lat: Boolean, sign: Double): String {
    /*
    Type   Dir.   Sign    Test
    Lat.   N      +       > 0
    Lat.   S      -       < 0
    Long.  E      +       > 0
    Long.  W      -       < 0
    */
    return when{
        lat&&sign>0->"N"
        lat&&sign<0->"S"
        !lat&&sign>0->"E"
        !lat&&sign<0->"W"
        else->""
    }
}
