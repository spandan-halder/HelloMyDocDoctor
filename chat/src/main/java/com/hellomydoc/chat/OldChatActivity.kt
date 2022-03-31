package com.hellomydoc.chat

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material.icons.outlined.ChevronLeft
import androidx.compose.material.icons.rounded.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.hellomydoc.chat.customIcons.*
import com.hellomydoc.chat.models.Chat
import com.hellomydoc.chat.models.ChatPacketData
import com.hellomydoc.chat.utilities.newUid
import com.hellomydoc.chat.utilities.utcTimestamp
import com.hellomydoc.doctor.ui.theme.HelloMyDocDoctorTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import org.joda.time.format.DateTimeFormat


class OldChatActivity : ComponentActivity() {
    private val chatIdsToShowDate = mutableMapOf<String,String>()
    private val chats = mutableStateListOf<Chat>()
    var chatBox: ChatBox? = null
    var inputText = mutableStateOf("")
    val state = LazyListState()
    private fun scrollToBottomForce(){
        CoroutineScope(Dispatchers.Main).launch {
            if(chats.isNotEmpty()){
                state.scrollToItem(chats.size-1)
            }
        }
    }
    private fun scrollToBottom() {
        if(chats.size>0){
            CoroutineScope(Dispatchers.Main).launch {
                val last = state.layoutInfo.visibleItemsInfo.lastOrNull()?.index?:-1
                if(last==chats.size-2){
                    scrollToBottomForce()
                }
            }
        }
    }
    fun MutableState<Boolean>.toggle(){
        this.value = !this.value
    }
    val attachmentVisible = mutableStateOf(false)
    val appDelegate: AppDelegate
    get(){
        return this.application as AppDelegate
    }
    var peerId = mutableStateOf("")
    var myId = mutableStateOf("")
    object Style{
        const val dateVerticalMargin = 8
        const val chatDateCardElevation = 4
        val peerChatTextColor = Color.Black
        val peerChatCardBackgroundColor = Color(0xffF0F0F0)
        const val myChatVerticalMargin = 8
        const val myChatHorizontalMargin = 24
        const val dateDisplayFormat = "dd MMM, yyyy"
        val dateCardBackgroundColor = Color.White
        const val dateTextMargin = 8
        val dateColor = Color(0xffD32424)
        const val dateFormat = "yyyy/MM/dd"
        const val timeFormat = "hh:mm a"
        const val timeTopMargin = 6
        const val timeFontSize = 12
        val timeColor = Color.Gray
        const val chatTextPadding = 12
        val myChatTextColor = Color.White
        const val messageCardMinSizeFactor = 0.25f
        const val messageCardMaxSizeFactor = 0.75f
        const val chatCardCornerRadius = 20
        val myChatCardBackgroundColor = Color(0xff4F6266)
        const val floatingActionButtonsGap = 18
        val FloatingActionButtonCloseStateColor = Color.White
        val floatingActionButtonIconAddStateColor = Color(0xffD32424)
        const val floatingActionButtonIconSize = 40
        const val floatingActionButtonPressedElevation = 4
        const val floatingActionButtonElevation = 2
        val floatingActionButtonAddStateColor = Color(0xffD32424)
        val floatingActionButtonAttachmentStateColor = Color(0xffF0F0F0)
        const val floatingActionButtonCloseStateRotation = -135f
        const val floatingActionButtonSize = 64
        const val inputTextPlaceholderText = "Type a message"
        const val sendIconRightPadding = 12
        const val sendIconSize = 32
        val sendIconTint = Color(0xff222222)
        val inputTextCursorColor = Color.Black
        const val inputTextHorizontalPadding = 8
        val inputTextPlaceholderTextColor = Color(0xFF7C7C7C)
        val inputTextSelectionBackgroundColor = Color(0xFFFF6F6F)
        val inputTextSelectionHandleColor = Color(0xffD32424)
        val textInputAndSendBackgroundColor = Color(0xffF0F0F0)
        const val sendSectionButtonAndInputGap = 8
        const val sendSectionHorizontalPadding = 24
        const val sendSectionHeight = 64
        const val sendSectionVerticalPadding = 24
        const val attachmentVisibilityAnimationDuration = 300
        const val attachmentSelectionHeight = 80f
        const val backButtonSize = 32
        const val notFoundAnimationSize = 200
        const val headerHeight = 80
    }
    ///////////////////////////////////////////
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val model: ChatViewModel by viewModels()
        collectMemberIds()
        if(savedInstanceState==null){
            chatBox = ChatBox(
                appDelegate,
                room,
                chats,
                chatIdsToShowDate,
                myId.value){chat->
                if(chat==null){
                    scrollToBottomForce()
                }
                else{
                    if(chat.sender==myId.value){
                        scrollToBottomForce()
                    }
                    else{
                        scrollToBottom()
                    }
                }
            }
        }
        setContent {
            Content()
        }
    }

    private val room: String
        get() {
            val m = myId.value
            val p = peerId.value
            if (m.isEmpty() || p.isEmpty()) {
                return ""
            }
            return if (m > p) {
                "${p}_${m}"
            } else {
                "${m}_${p}"
            }
        }

    private fun collectMemberIds() {
        val peerIdKey = appDelegate.peerIdKey
        val myIdKey = appDelegate.myIdKey
        peerId.value = intent?.extras?.getString(peerIdKey)?:""
        myId.value = intent?.extras?.getString(myIdKey)?:""
    }

    @Composable
    private fun Content() {
        HelloMyDocDoctorTheme {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = Color.White
            ) {
                PageContent()
            }
        }
    }

    @Composable
    private fun PageContent() {
        Column(
            modifier = Modifier.fillMaxSize()
        ){
            HeaderContent()
            MainContentSection()
        }
    }

    @Composable
    private fun HeaderContent() {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ){
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(Style.headerHeight.dp)
                    .background(Color.White)
            ){
                HeaderCore()
                Divider(
                    modifier = Modifier.align(Alignment.BottomCenter)
                )
            }
        }
    }

    @Composable
    private fun HeaderCore() {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = {
                onBackClick()
            }) {
                Icon(
                    imageVector = Icons.Outlined.ChevronLeft,
                    contentDescription = "Back",
                    modifier = Modifier.size(Style.backButtonSize.dp)
                )
            }
            PeerIdentityAndStatus()
            AsyncImage(
                model = "https://randomuser.me/api/portraits/men/82.jpg",
                contentDescription = null,
                modifier = Modifier.clip(CircleShape)
            )
        }
    }

    @Composable
    private fun PeerIdentityAndStatus() {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Text(
                "Peer Name",
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Text(
                "Last seen and status",
                color = Color.Gray,
                fontSize = 12.sp
            )
        }
    }

    private fun onBackClick() {
        finish()
    }

    @Composable
    private fun MainContentSection() {
        when {
            myId.value.isEmpty() -> {
                UserIdNotFoundContent()
            }
            peerId.value.isEmpty() -> {
                PeerIdNotFoundContent()
            }
            else -> {
                ChatContent()
            }
        }
    }

    @Composable
    private fun ChatContent() {
        Box(
            modifier = Modifier.fillMaxSize()
        ){
            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter),
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                ChatsSection()
                ChatInputSection()
            }
        }
    }

    @Composable
    private fun ColumnScope.ChatsSection() {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
        ){
            if(chats.isEmpty()){

            }
            else{
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    state = state
                ){
                    items(chats){
                        MessageItem(it)
                    }
                }
            }
        }
    }

    @Composable
    private fun LazyItemScope.MessageItem(chat: Chat) {
        val isMe = chat.sender == myId.value
        if(isMe){
            MyMessage(chat)
        }else{
            PeerMessage(chat)
        }
    }

    @Composable
    private fun PeerMessage(chat: Chat) {
        PeerMessageContent(chat)
    }

    @Composable
    private fun PeerMessageContent(chat: Chat) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(
                    horizontal = Style.myChatHorizontalMargin.dp,
                    vertical = Style.myChatVerticalMargin.dp
                )
        ){
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterStart),
                horizontalAlignment = Alignment.Start
            ){
                ChatDate(chat)
                PeerChatText(chat)
                ChatTimeAndStatus(chat)
            }
        }
    }

    @Composable
    private fun PeerChatText(chat: Chat) {
        Card(
            backgroundColor = Style.peerChatCardBackgroundColor,
            shape = RoundedCornerShape(
                topEnd = Style.chatCardCornerRadius.dp,
                topStart = Style.chatCardCornerRadius.dp,
                bottomStart = 0.dp,
                bottomEnd = Style.chatCardCornerRadius.dp
            )
        ){
            Text(
                chat.data?.text?:"",
                color = Style.peerChatTextColor,
                modifier = Modifier.padding(Style.chatTextPadding.dp)
            )
        }
    }

    @Composable
    private fun MyMessage(chat: Chat) {
        MyMessageContent(chat)
    }

    @Composable
    private fun MyMessageContent(chat: Chat) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(
                    horizontal = Style.myChatHorizontalMargin.dp,
                    vertical = Style.myChatVerticalMargin.dp
                )
        ){
            val configuration = LocalConfiguration.current
            Column(
                modifier = Modifier

                    .width((configuration.screenWidthDp * Style.messageCardMaxSizeFactor).dp)
                    .align(Alignment.CenterEnd),
                horizontalAlignment = Alignment.End
            ){
                ChatDate(chat)
                MyChatText(chat)
                ChatTimeAndStatus(chat)
            }
        }
    }

    data class DateTimeAndShowDate(
        val date: String,
        val showDate: Boolean,
        val time: String
    )

    @Composable
    private fun getAndMarkDate(chat: Chat): DateTimeAndShowDate {
        val dateTime = DateTime(chat.createdAt,DateTimeZone.UTC).toDateTime(DateTimeZone.getDefault())
        val time = dateTime.toString(
            DateTimeFormat.forPattern(Style.timeFormat)
        )
        val date = dateTime.toString(
            DateTimeFormat.forPattern(Style.dateDisplayFormat)
        )
        val showDate = chatIdsToShowDate.containsKey(chat.chatId)
        return DateTimeAndShowDate(date, showDate, time)
    }

    @Composable
    private fun ColumnScope.ChatDate(chat: Chat) {
        if(chatIdsToShowDate.containsKey(chat.chatId)){
            Card(
                modifier = Modifier
                    .wrapContentSize()
                    .align(Alignment.CenterHorizontally)
                    .padding(vertical = Style.dateVerticalMargin.dp)
                ,
                backgroundColor = Style.dateCardBackgroundColor,
                shape = CircleShape,
                elevation = Style.chatDateCardElevation.dp
            ){
                Text(
                    chatIdsToShowDate[chat.chatId]?:"",
                    color = Style.dateColor,
                    fontSize = Style.timeFontSize.sp,
                    modifier = Modifier.padding(Style.dateTextMargin.dp)
                )
            }
        }
    }

    @Composable
    private fun MyChatText(chat: Chat) {
        Card(
            backgroundColor = Style.myChatCardBackgroundColor,
            shape = RoundedCornerShape(
                topEnd = Style.chatCardCornerRadius.dp,
                topStart = Style.chatCardCornerRadius.dp,
                bottomStart = Style.chatCardCornerRadius.dp,
                bottomEnd = 0.dp
            )
        ){
            Text(
                chat.data?.text?:"",
                color = Style.myChatTextColor,
                modifier = Modifier.padding(Style.chatTextPadding.dp)
            )
        }
    }

    @Composable
    private fun ChatTimeAndStatus(chat: Chat) {
        Spacer(
            modifier = Modifier.height(Style.timeTopMargin.dp)
        )
        Row(
            verticalAlignment = Alignment.CenterVertically
        ){
            val time = DateTime(chat.createdAt,DateTimeZone.UTC).toDateTime(DateTimeZone.getDefault()).toString(
                DateTimeFormat.forPattern("hh:mm a")
            )
            ChatTime(time)
        }
    }

    @Composable
    private fun MyChatStatus(chat: Chat) {
        when{
            chat.seenAt>0->{
                MyChatSeenStatus()
            }
            chat.receivedAt>0->{
                MyChatReceivedStatus()
            }
            chat.arrivedServerAt>0->{
                MyChatArrivedStatus()
            }
            else->{
                MyChatWaitingStatus()
            }
        }
    }

    @Composable
    private fun MyChatWaitingStatus() {
        Icon(
            imageVector = Icons.Filled.AccessTime,
            tint = Color.Gray,
            contentDescription = "Waiting",
            modifier = Modifier
                .size(24.dp)
                .padding(horizontal = 4.dp)
        )
    }

    @Composable
    private fun MyChatArrivedStatus() {
        Icon(
            imageVector = Icons.Filled.Done,
            tint = Color.Gray,
            contentDescription = "Arrived at server",
            modifier = Modifier
                .size(24.dp)
                .padding(horizontal = 4.dp)
        )
    }

    @Composable
    private fun MyChatReceivedStatus() {
        Icon(
            imageVector = Icons.Filled.DoneAll,
            tint = Color.Gray,
            contentDescription = "Received",
            modifier = Modifier
                .size(24.dp)
                .padding(horizontal = 4.dp)
        )
    }

    @Composable
    private fun MyChatSeenStatus() {
        Icon(
            imageVector = Icons.Filled.DoneAll,
            tint = Color(0xff035efc),
            contentDescription = "Seen",
            modifier = Modifier
                .size(24.dp)
                .padding(horizontal = 4.dp)
        )
    }

    @Composable
    private fun ChatTime(time: String) {
        Text(
            time,
            color = Style.timeColor,
            fontSize = Style.timeFontSize.sp
        )
    }

    private fun formatDisplayDate(dateTime: DateTime): String {
        val displayDate = dateTime.toString(
            DateTimeFormat.forPattern(Style.dateDisplayFormat)
        )
        val parts = displayDate.split(" ")
        var dis = ""
        if(parts.size==3){
            val day = parts[0]
            val suffix = when(day.last()){
                '1'->"st"
                '2'->"nd"
                '3'->"rd"
                else->"th"
            }
            dis = "${parts[0]}$suffix ${parts[1]} ${parts[2]}"
        }
        return dis
    }

    private fun createNewTextChat(text: String): Chat {
        return Chat(
            data = ChatPacketData(text = text),
            sender = myId.value,
            receiver = peerId.value,
            chatId = newUid,
            createdAt = utcTimestamp
        )
    }

    private fun sendMessage(text: String) {
        if(text.isEmpty()){
            return
        }
        chatBox?.insert(createNewTextChat(text.trim()))
    }

    @Composable
    private fun ChatInputSection() {
        SendSection()
        AnimatedAttachmentSelectionSection(attachmentVisible.value)
    }

    @Composable
    private fun SendSection() {
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
                AddOrCloseAttachmentSectionButton()
                Spacer(modifier = Modifier.width(Style.sendSectionButtonAndInputGap.dp))
                TextInputAndSendSection()
            }
        }
    }

    @Composable
    private fun TextInputAndSendSection() {

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
                MessageInput()
                SendIcon()
            }
        }
    }


    @Composable
    private fun RowScope.MessageInput() {
        val customTextSelectionColors = TextSelectionColors(
            handleColor = Style.inputTextSelectionHandleColor,
            backgroundColor = Style.inputTextSelectionBackgroundColor
        )
        CompositionLocalProvider(LocalTextSelectionColors provides customTextSelectionColors) {
            TextField(
                value = inputText.value,
                placeholder = {
                    Text(
                        Style.inputTextPlaceholderText,
                        color = Style.inputTextPlaceholderTextColor
                    )
                },
                onValueChange = {
                    inputText.value = it
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
    private fun SendIcon() {
        IconButton(onClick = {
            sendMessage(inputText.value)
            inputText.value = ""
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

    @Composable
    private fun AddOrCloseAttachmentSectionButton() {
        val color = animateColorAsState(targetValue = if(attachmentVisible.value) Style.floatingActionButtonAttachmentStateColor else Style.floatingActionButtonAddStateColor)
        val rotation = animateFloatAsState(
            targetValue = if(attachmentVisible.value) Style.floatingActionButtonCloseStateRotation else 0f,
            animationSpec = tween(
                durationMillis = Style.attachmentVisibilityAnimationDuration,
                easing = LinearOutSlowInEasing
            ))
        ////////////////////////////////
        FloatingActionButton(
            onClick = {
                attachmentVisible.toggle()
            },
            modifier = Modifier
                .size(Style.floatingActionButtonSize.dp)
                .graphicsLayer {
                    rotationZ = rotation.value
                },
            backgroundColor = color.value,
            elevation = floatingActionButtonElevations()
        ) {
            FloatingActionButtonIcon()
        }
    }

    @Composable
    private fun floatingActionButtonElevations(): FloatingActionButtonElevation {
        return FloatingActionButtonDefaults.elevation(
            defaultElevation = Style.floatingActionButtonElevation.dp,
            pressedElevation = Style.floatingActionButtonPressedElevation.dp
        )
    }

    @Composable
    private fun FloatingActionButtonIcon() {
        Icon(
            imageVector = Icons.Rounded.Add,
            tint = if(attachmentVisible.value) Style.floatingActionButtonIconAddStateColor else Style.FloatingActionButtonCloseStateColor,
            contentDescription = "Show/hide attachment section",
            modifier = Modifier.size(Style.floatingActionButtonIconSize.dp)
        )
    }

    @Composable
    private fun AnimatedAttachmentSelectionSection(visible: Boolean) {
        var heightValue by remember {
            mutableStateOf(0f)
        }
        heightValue = if(visible) Style.attachmentSelectionHeight else 0f
        val height = animateFloatAsState(
            targetValue = heightValue,
            animationSpec = tween(
                durationMillis = Style.attachmentVisibilityAnimationDuration,
                easing = LinearOutSlowInEasing
            )
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(height.value.dp)
                .background(Color.White)
        ){
            AttachmentSelectionSection()
        }
    }

    @Composable
    private fun AttachmentSelectionSection() {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = Style.sendSectionHorizontalPadding.dp)
        ){
            AttachmentFloatingActionButton(Icons.HmdCamera, "Take picture"){

            }
            Spacer(modifier = Modifier.width(Style.floatingActionButtonsGap.dp))
            AttachmentFloatingActionButton(Icons.HmdFile, "Select file"){

            }
            Spacer(modifier = Modifier.width(Style.floatingActionButtonsGap.dp))
            AttachmentFloatingActionButton(Icons.HmdLocation, "Location"){

            }
            Spacer(modifier = Modifier.width(Style.floatingActionButtonsGap.dp))
            AttachmentFloatingActionButton(Icons.HmdUser, "User"){

            }
        }
    }

    @Composable
    private fun AttachmentFloatingActionButton(icon: ImageVector, contentDescription: String, callback: ()->Unit) {
        FloatingActionButton(
            onClick = callback,
            modifier = Modifier.size(Style.floatingActionButtonSize.dp),
            backgroundColor = Style.FloatingActionButtonCloseStateColor,
            elevation = floatingActionButtonElevations()
        ) {
            Icon(
                imageVector = icon,
                tint = Style.floatingActionButtonIconAddStateColor,
                contentDescription = contentDescription,
                modifier = Modifier.size(24.dp)
            )
        }
    }

    @Composable
    private fun UserIdNotFoundContent() {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ){
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                NotFoundAnimation()
                UserNotFoundText(appDelegate?.userNotFoundString?:"")
            }
        }
    }

    @Composable
    private fun UserNotFoundText(text: String) {
        Text(
            text,
            color = Color.Black,
            fontWeight = FontWeight.Black
        )
    }

    @Composable
    private fun NotFoundAnimation() {
        /*val json = appDelegate.notFoundAnimationJson
        LottieView(
            json = json,
            modifier = Modifier.size(Style.notFoundAnimationSize.dp)
        )*/
    }

    @Composable
    private fun PeerIdNotFoundContent() {
        val text = appDelegate.peerNotFoundText?:return
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ){
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                NotFoundAnimation()
                UserNotFoundText(text)
            }
        }
    }

    override fun onDestroy() {
        chatBox?.clear()
        super.onDestroy()
    }
}