
package com.hellomydoc.chat

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import com.hellomydoc.chat.models.Chat
import com.hellomydoc.chat.models.ChatPacketAttachment
import com.hellomydoc.chat.models.ChatPacketData
import com.hellomydoc.chat.models.Location
import com.hellomydoc.chat.navigation.RouteNavigator
import com.hellomydoc.chat.presentation.activity.ChatActivity
import com.hellomydoc.chat.utilities.newUid
import com.hellomydoc.chat.utilities.utcTimestamp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import net.gotev.uploadservice.protocols.multipart.MultipartUploadRequest
import java.io.ByteArrayOutputStream
import javax.inject.Inject


enum class AttachmentType{
    IMAGE,
    FILE,
    LOCATION,
    CONTACT,
    NONE
}
data class Contact(
    val phone: String? = null,
    val name: String? = null
)
data class Attachment(
    val type: AttachmentType = AttachmentType.NONE,
    val uri: Uri? = null,
    val path: String? = null,
    val latLng: LatLng? = null,
    val contact: Contact? = null
)

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val routeNavigator: RouteNavigator,
    private val appDelegate: AppDelegate
) : ViewModel(), RouteNavigator by routeNavigator {
    val currentlyShowingImageUrl = mutableStateOf("")
    val peerStatus = mutableStateOf("Last seen and status")
    val peerName = mutableStateOf("Peer name")
    val peerProfileImage = mutableStateOf("https://randomuser.me/api/portraits/men/82.jpg")
    val placeHolderImageId: Int
    get() = R.drawable.ic_image_svgrepo_com
    val mapPlaceholderImageId: Int
    get() = R.drawable.ic_map_svgrepo_com
    private val newFileId: String
    get() = "file_$newUid"
    val locationPicking = mutableStateOf(false)
    val finish = mutableStateOf(false)
    //////////////////////////////////////////////////////
    val attachment = mutableStateOf(Attachment())
    val peerNotFoundText: String
    get() = appDelegate.peerNotFoundText
    val userNotFoundString: String
    get() = appDelegate.userNotFoundString?:""
    var inputText = mutableStateOf("")
    val attachmentVisible = mutableStateOf(false)
    val myId = mutableStateOf("")
    val peerId = mutableStateOf("")
    val chats = mutableStateListOf<Chat>()
    var state:LazyListState? = LazyListState()
    val chatIdsToShowDate = mutableMapOf<String,String>()
    private var chatBox: ChatBox? = null

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

    @OptIn(ExperimentalMaterialApi::class)
    override fun onCleared() {
        chatBox?.clear()
        chatBox = null
        super.onCleared()
    }



    fun onStart() {

    }

    @OptIn(ExperimentalMaterialApi::class)
    fun onStop() {
        scope = null
        modalBottomSheetState = null
        state = null
    }

    @OptIn(ExperimentalMaterialApi::class)
    fun onBackClick(): Boolean {
        if(locationPicking.value){
            locationPicking.value = false
            return true
        }
        if(currentlyShowingImageUrl.value.isNotEmpty()){
            currentlyShowingImageUrl.value = ""
            return true
        }
        if(modalBottomSheetState?.isAnimationRunning==true){
            return true
        }
        if(modalBottomSheetState?.isVisible==true){
            scope?.launch {
                modalBottomSheetState?.hide()
            }
            return true
        }
        finish.value = true
        return false
    }

    fun insert(context: Context, text: String) {
        when(attachment.value.type){
            AttachmentType.IMAGE -> sendImage(context,text)
            AttachmentType.FILE -> sendFile(text)
            AttachmentType.LOCATION -> sendLocation(text)
            AttachmentType.CONTACT -> sendContact(text)
            AttachmentType.NONE -> sendTextOnly(text)
        }
    }

    private fun sendTextOnly(text: String) {
        if(text.isEmpty()){
            return
        }
        chatBox?.insert(
            createNewTextChat(
                myId.value,
                peerId.value,
                text.trim()
            )
        )
    }

    private fun sendContact(text: String) {
        val name = attachment.value.contact?.name?:return
        val phone = attachment.value.contact?.phone?:return
        if(name.isNotEmpty()&&phone.isNotEmpty()){
            val cp = ChatPacketAttachment(
                type = "contact",
                name = name,
                phone = phone
            )

            val chat = Chat(
                data = ChatPacketData(text = text, attachment = cp),
                sender = myId.value,
                receiver = peerId.value,
                chatId = newUid,
                createdAt = utcTimestamp
            )
            chatBox?.insert(
                chat
            )
            attachment.value = Attachment()
        }
    }

    private fun sendLocation(text: String) {
        val lat = attachment.value.latLng?.latitude?:return
        val lng = attachment.value.latLng?.longitude?:return
        val cp = ChatPacketAttachment(
            type = "location",
            location = Location(
                lat = lat,
                lng = lng
            )
        )

        val chat = Chat(
            data = ChatPacketData(text = text, attachment = cp),
            sender = myId.value,
            receiver = peerId.value,
            chatId = newUid,
            createdAt = utcTimestamp
        )
        chatBox?.insert(
            chat
        )
        attachment.value = Attachment()
    }

    private fun sendFile(text: String) {
        val path = attachment.value.path?:return
        attachment.value = Attachment()
        val fileId = newFileId
        val chatId = newUid
        val chat = Chat(
            data = ChatPacketData(
                text = text,
                attachment = ChatPacketAttachment(
                    type = "doc",
                    name = fileId,
                )
            ),
            sender = myId.value,
            receiver = peerId.value,
            chatId = chatId,
            createdAt = utcTimestamp
        )
        chatBox?.insert(
            chat
        )
        val uploadId = uploadFile(path,room,chatId,fileId)
        saveUploadToken(room,chatId, fileId, uploadId)
    }

    private fun sendImage(context: Context, text: String) {
        val path = attachment.value.path?:return
        //val thumbnail = createThumbnail(path)
        attachment.value = Attachment()
        val fileId = newFileId
        val chatId = newUid
        val chat = Chat(
            data = ChatPacketData(
                text = text,
                attachment = ChatPacketAttachment(
                    type = "image",
                    name = fileId,
                    //thumbnail = thumbnail
                )
            ),
            sender = myId.value,
            receiver = peerId.value,
            chatId = chatId,
            createdAt = utcTimestamp
        )
        chatBox?.insert(
            chat
        )
        val uploadId = uploadFile(path,room,chatId,fileId)
        saveUploadToken(room,chatId, fileId, uploadId)
    }

    private fun createThumbnail(path: String): String {
        val original: Bitmap = BitmapFactory.decodeFile(path)

        val maxSize = 50
        val outWidth: Int
        val outHeight: Int
        val inWidth: Int = original.width
        val inHeight: Int = original.height
        if (inWidth > inHeight) {
            outWidth = maxSize
            outHeight = inHeight * maxSize / inWidth
        } else {
            outHeight = maxSize
            outWidth = inWidth * maxSize / inHeight
        }

        val resizedBitmap = Bitmap.createScaledBitmap(original, outWidth, outHeight, false)
        val out = ByteArrayOutputStream()
        resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 2, out)
        val byteArray = out.toByteArray()
        return String(byteArray)
        /*Bitmap.createScaledBitmap(original,)
        val out = ByteArrayOutputStream()
        original.compress(Bitmap.CompressFormat.JPEG, 2, out)
        val byteArray = out.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)*/
        //val decoded = BitmapFactory.decodeStream(ByteArrayInputStream(out.toByteArray()))
    }

    private fun saveUploadToken(roomId: String, chatId: String, fileId: String, uploadId: String) {
        ChatBox.saveUploadToken(roomId, chatId,fileId,uploadId)
    }

    private fun uploadFile(file: String, roomId: String, chatId: String, fileId: String): String {
        return MultipartUploadRequest(appDelegate.app, serverUrl = "https://hellomydoc.com/api/v1/welcome/storeChatAssetsWithName")
            .setMethod("POST")
            .addParameter("roomId",roomId)
            .addParameter("chatId",chatId)
            .addParameter("fileIds",fileId)
            .addFileToUpload(
                filePath = file,
                parameterName = "files[]"
            ).startUpload()
    }

    private fun createNewTextChat(myId: String, peerId: String,text: String): Chat {
        return Chat(
            data = ChatPacketData(text = text),
            sender = myId,
            receiver = peerId,
            chatId = newUid,
            createdAt = utcTimestamp
        )
    }

    private fun scrollToBottomForce(){
        CoroutineScope(Dispatchers.Main).launch {
            if(chats.isNotEmpty()){
                state?.scrollToItem(chats.size-1)
            }
        }
    }
    private fun scrollToBottom() {
        if(chats.size>0){
            CoroutineScope(Dispatchers.Main).launch {
                val last = state?.layoutInfo?.visibleItemsInfo?.lastOrNull()?.index?:-1
                if(last==chats.size-2){
                    scrollToBottomForce()
                }
            }
        }
    }

    fun setData(data: Any?) {
        if(data is ChatActivity.UserIds){
            myId.value = data.userId
            peerId.value = data.peerId
            chatBox?.clear()
            chatBox = null
            chats.clear()
            chatIdsToShowDate.clear()
            chatBox = ChatBox(
                appDelegate,
                room,
                chats,
                chatIdsToShowDate,
                myId.value
            ){chat->
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
    }

    fun onNewImageUri(uri: Uri, uriFilePath: String?) {
        attachment.value = Attachment(AttachmentType.IMAGE,uri,uriFilePath)
    }

    fun onClosePreviewAttachmentClick() {
        attachment.value = Attachment()
    }

    fun onNewFileUri(path: String) {
        attachment.value = Attachment(AttachmentType.FILE,path=path)
    }

    @OptIn(ExperimentalMaterialApi::class)
    fun onLocationPickClick() {
        locationPicking.value = true
    }

    fun onLocationPickCancel(): Boolean {
        locationPicking.value = false
        return true
    }

    fun onMarker(latLng: LatLng?) {
        attachment.value = Attachment(AttachmentType.LOCATION, latLng = latLng)
    }

    private var scope: CoroutineScope? = null
    fun onScope(scope: CoroutineScope) {
        this.scope = scope
    }

    @OptIn(ExperimentalMaterialApi::class)
    private var modalBottomSheetState: ModalBottomSheetState? = null
    @OptIn(ExperimentalMaterialApi::class)
    fun onBottomSheetState(modalBottomSheetState: ModalBottomSheetState) {
        this.modalBottomSheetState = modalBottomSheetState
    }

    fun onLocationPicked() {
        locationPicking.value = false
    }

    fun onContactPickClick() {

    }

    fun onContactPicked(phoneNo: String, name: String) {
        attachment.value = Attachment(AttachmentType.CONTACT, contact = Contact(name = name, phone = phoneNo))
    }

    fun sayHi() {
        sendTextOnly("Hi \uD83D\uDC4B")
    }

    fun markAsSeen(chat: Chat) {
        chatBox?.markSeen(chat)
    }

    fun showDetails(chat: Chat) {

    }

    fun deleteChat(chat: Chat) {
        chatBox?.delete(chat.chatId)
    }

    fun showImage(url: String) {
        currentlyShowingImageUrl.value = url
    }

    fun hideImageViewer() {
        currentlyShowingImageUrl.value = ""
    }
}