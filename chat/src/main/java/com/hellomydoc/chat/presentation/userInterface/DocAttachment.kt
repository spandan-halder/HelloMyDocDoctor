package com.hellomydoc.chat.presentation.userInterface

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import com.hellomydoc.chat.*
import com.hellomydoc.chat.R
import com.hellomydoc.chat.models.Chat
import com.hellomydoc.chat.presentation.viewModels.ChatViewModel

@Composable
fun DocAttachment(viewModel: ChatViewModel, chat: Chat, isMy: Boolean) {
    val context = LocalContext.current
    val name = chat.data?.attachment?.name?:""
    val url = "https://hellomydoc.com/api/v1/welcome/getChatAssetByName/$name"
    Box(modifier = Modifier
        .fillMaxWidth()){
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f),
            backgroundColor = if(isMy) Style.myChatCardBackgroundColor else Style.peerChatCardBackgroundColor,
            shape = RoundedCornerShape(12.dp),
        ){
            val progress = chat.data?.attachment?.progress?:return@Card
            when(progress){
                ChatBox.FileUploadError.cancelled->{
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ){
                        Text(
                            "Cancelled by user!",
                            color = if(isMy) Color.White else Color.Black,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                ChatBox.FileUploadError.error->{
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ){
                        Text(
                            "Error!",
                            color = if(isMy) Color.White else Color.Black,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                ChatBox.FileUploadError.unknown->{
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ){
                        Text(
                            "Unknown error!",
                            color = if(isMy) Color.White else Color.Black,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                ChatBox.FileUploadError.success->{
                    SubcomposeAsyncImage(
                        model = R.drawable.pdf_svgrepo_com,
                        loading = {
                            Box(
                                contentAlignment = Alignment.Center
                            ){
                                Icon(
                                    imageVector = Icons.Filled.Download,
                                    tint = Color.Gray,
                                    modifier = Modifier.size(18.dp),
                                    contentDescription = "Downloading"
                                )
                                CircularProgressIndicator(
                                    color = Color(0xFF0099FF),
                                    modifier = Modifier.size(32.dp)
                                )
                            }
                        },
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(8.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .clickable {
                                val i = Intent(Intent.ACTION_VIEW)
                                i.data = Uri.parse(url)
                                context.startActivity(i)
                            },
                    )
                }
                else->{
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ){
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ){
                            Box(
                                modifier = Modifier.wrapContentSize(),
                                contentAlignment = Alignment.Center
                            ){
                                Icon(
                                    imageVector = Icons.Filled.CloudUpload,
                                    tint = Color.Gray,
                                    contentDescription = "Uploading",
                                    modifier = Modifier.size(18.dp)
                                )
                                CircularProgressIndicator(
                                    color = Color(0xFF0099FF),
                                    modifier = Modifier.size(32.dp),
                                    progress = progress/100f
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                "$progress%",
                                color = Color.Gray
                            )
                        }
                    }
                }
            }
        }
        if(chat.data?.attachment!=null){
            ChatMenu(viewModel, chat, isMy,true)
        }
    }
}