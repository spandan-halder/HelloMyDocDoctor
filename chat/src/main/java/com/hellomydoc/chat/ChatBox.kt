package com.hellomydoc.chat

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.provider.Settings.Secure.ANDROID_ID
import android.util.Log
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.google.firebase.database.DataSnapshot
import com.google.gson.Gson
import com.hellomydoc.chat.models.Chat
import com.hellomydoc.chat.presentation.userInterface.Style
import com.hellomydoc.chat.uploader.CustomPlaceholdersProcessor
import com.hellomydoc.chat.uploader.GlobalRequestObserverDelegate
import com.hellomydoc.chat.utilities.utcTimestamp
import io.paperdb.Paper
import net.gotev.uploadservice.BuildConfig
import net.gotev.uploadservice.UploadServiceConfig
import net.gotev.uploadservice.data.RetryPolicyConfig
import net.gotev.uploadservice.data.UploadInfo
import net.gotev.uploadservice.network.ServerResponse
import net.gotev.uploadservice.observer.request.GlobalRequestObserver
import net.gotev.uploadservice.okhttp.OkHttpStack
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import org.joda.time.format.DateTimeFormat
import java.util.concurrent.TimeUnit


class ChatBox(
    private val appDelegate: AppDelegate,
    private val room: String,
    private val list: SnapshotStateList<Chat>,
    private val chatIdsToShowDate: MutableMap<String, String>,
    private val myId: String,
    private val onAdd: (Chat?)->Unit
) {
    private val chatIdTimestamp = mutableMapOf<String,Long>()
    companion object{
        var CHANNEL = "HelloMyDocChatAssetUploadChannel"
        val bucket = "messages"
        fun updateFcmToken(appDelegate: AppDelegate) {
            val userId = appDelegate.userId
            if(userId.isEmpty()){
                return
            }
            var android_id = appDelegate.getPrefString(ANDROID_ID)?:""
            if(android_id.isEmpty()){
                android_id = "default"
            }
            val fcmToken = appDelegate.fcmToken?:""
            if(fcmToken.isEmpty()){
                return
            }
            val path = "fcm_tokens/$userId/$android_id"
            Firepo.set(appDelegate, path, fcmToken)
            appDelegate.setFcmTokenSynced(true)
        }

        fun updateFcmTokenFromApp(appDelegate: AppDelegate) {
            val id = appDelegate.androidId
            appDelegate.setAndroidId(id)
            val fcmSynced = appDelegate.fcmSynced
            if(!fcmSynced){
                val fcmToken = appDelegate.fcmToken?:""
                if(fcmToken.isEmpty()){
                    appDelegate.fetchFcmToken{
                        if(it.isNotEmpty()){
                            appDelegate.setFcmToken(it)
                            appDelegate.setFcmTokenSynced(false)
                            updateFcmToken(appDelegate)
                        }
                    }
                }
                else{
                    updateFcmToken(appDelegate)
                }
            }
        }

        fun onNewToken(appDelegate: AppDelegate,p0: String) {
            appDelegate.setFcmToken(p0)
            appDelegate.setFcmTokenSynced(false)
            updateFcmToken(appDelegate)
        }

        private val okHttpClient: OkHttpClient
            get() = OkHttpClient.Builder()
                .followRedirects(true)
                .followSslRedirects(true)
                .retryOnConnectionFailure(true)
                .connectTimeout(15, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .addInterceptor(Interceptor { chain: Interceptor.Chain ->
                    chain.proceed(
                        chain.request().newBuilder()
                            .header("User-Agent", UploadServiceConfig.defaultUserAgent)
                            .build()
                    )
                })
                /*.addNetworkInterceptor(Interceptor { chain: Interceptor.Chain ->
                    chain.proceed(
                        chain.request().newBuilder()
                            .addHeader("myheader", "myvalue")
                            .addHeader("mysecondheader", "mysecondvalue")
                            .build()
                    )
                })
                .addInterceptor(HttpLoggingInterceptor { message: String? ->
                    Log.d("OkHttp", message.orEmpty())
                }.setLevel(HttpLoggingInterceptor.Level.HEADERS))*/
                .cache(null)
                .build()

        fun onAppInit(app: Application) {
            Paper.init(app)
            /////////////////////
            createNotificationChannel(app)
            UploadServiceConfig.initialize(app, CHANNEL, BuildConfig.DEBUG)
            UploadServiceConfig.httpStack = OkHttpStack(okHttpClient)
            UploadServiceConfig.retryPolicy = RetryPolicyConfig(
                initialWaitTimeSeconds = 1,
                maxWaitTimeSeconds = 10,
                multiplier = 2,
                defaultMaxRetries = 3
            )
            UploadServiceConfig.placeholdersProcessor = CustomPlaceholdersProcessor()
            GlobalRequestObserver(app, GlobalRequestObserverDelegate())
        }

        private fun createNotificationChannel(app: Application) {
            if (Build.VERSION.SDK_INT >= 26) {
                val notificationManager = app.getSystemService(Application.NOTIFICATION_SERVICE) as NotificationManager
                val channel = NotificationChannel(
                    CHANNEL,
                    "HelloMyDoc Upload Service",
                    NotificationManager.IMPORTANCE_LOW
                )
                notificationManager.createNotificationChannel(channel)
            }
        }

        fun saveUploadToken(roomId: String, chatId: String, fileId: String, uploadId: String) {
            val token = UploadToken(
                roomId,
                chatId,
                fileId
            )
            Paper.book().write(uploadId, token)
        }

        fun onUploadProgress(context: Context, uploadInfo: UploadInfo) {
            val uploadId = uploadInfo.uploadId
            setUploadProgress(context, uploadId,uploadInfo.progressPercent)
        }

        private fun saveKeyValueToFirebase(context: Context, key: String, value: Int) {
            Firepo.set(context.applicationContext as AppDelegate,key,value)
        }

        fun onUploadSuccess(
            context: Context,
            uploadInfo: UploadInfo,
            serverResponse: ServerResponse
        ) {
            val uploadId = uploadInfo.uploadId
            setUploadProgress(context, uploadId,100)
        }

        fun onUploadCompleteWhileNotObserving() {

        }

        fun onUploadCompleted(context: Context, uploadInfo: UploadInfo) {
            //val uploadId = uploadInfo.uploadId
            //setUploadProgress(context, uploadId,100)
        }

        private fun setUploadProgress(context: Context, uploadId: String, progress: Int) {
            val uploadToken: UploadToken = Paper.book().read(uploadId)?:return
            val roomId = uploadToken.roomId?:return
            val chatId = uploadToken.chatId?:return
            if(roomId.isEmpty()||chatId.isEmpty()){
                return
            }
            val key = "messages/$roomId/$chatId/data/attachment/progress"
            val value = progress
            saveKeyValueToFirebase(context,key,value)
        }

        fun onUploadUnknownError(context: Context, uploadInfo: UploadInfo) {
            val uploadId = uploadInfo.uploadId
            setUploadProgress(context, uploadId,FileUploadError.unknown)
        }

        fun onUploadError(context: Context, uploadInfo: UploadInfo) {
            val uploadId = uploadInfo.uploadId
            setUploadProgress(context, uploadId,FileUploadError.error)
        }

        fun onUserCancelledUpload(context: Context, uploadInfo: UploadInfo) {
            val uploadId = uploadInfo.uploadId
            setUploadProgress(context, uploadId,FileUploadError.cancelled)
        }
    }

    object FileUploadError{
        const val error = -1
        const val unknown = -2
        const val cancelled = -3
        const val success = 100
    }

    data class UploadToken(
        val roomId: String? = "",
        val chatId: String? = "",
        val fileId: String? = ""
    )

    val chatIdToDate = mutableMapOf<String,String>()
    private val firepo = Firepo(appDelegate,"$bucket/$room")
    enum class ChatEvent{
        ADDED,
        REMOVED,
        CHANGED,
        MOVED,
        CANCELLED
    }
    private val DataSnapshot.toChat: Chat?
        get() {
            val value = value ?: ""
            val ms = Gson().toJson(value)
            val chat = try {
                Gson().fromJson(ms, Chat::class.java)
            } catch (e: Exception) {
                null
            }
            return chat
        }
    private fun onChildEvent(event: ChatEvent, chat: Chat?){
        when(event){
            ChatEvent.ADDED -> {
                if(chat!=null){
                    val index = list.indexOfFirst {
                        it.chatId==chat.chatId
                    }
                    if(index > -1){
                        chatIdTimestamp[chat.chatId] = chat.createdAt
                        trackDate()
                        list[index] = chat
                    }
                    else{
                        chatIdTimestamp[chat.chatId] = chat.createdAt
                        trackDate()
                        list.add(chat)
                        sortList()
                        onAdd(chat)
                        if(chat.sender!=myId&&chat.receivedAt==0L){
                            chat.receivedAt= utcTimestamp
                            update("${chat.chatId}/receivedAt",chat.receivedAt)
                        }
                    }
                }
            }
            ChatEvent.REMOVED -> {
                if(chat!=null){
                    val index = list.indexOfFirst {
                        it.chatId == chat.chatId
                    }
                    if(index > -1){
                        chatIdTimestamp.remove(chat.chatId)
                        trackDate()
                        list.removeAt(index)
                    }
                }
            }
            ChatEvent.CHANGED -> {
                if(chat!=null){
                    val index = list.indexOfFirst {
                        it.chatId == chat.chatId
                    }
                    if(index > -1){
                        chatIdTimestamp[chat.chatId] = chat.createdAt
                        trackDate()
                        list[index] = chat
                    }
                }
            }
            ChatEvent.MOVED -> {

            }
            ChatEvent.CANCELLED -> {

            }
        }
        childEventListener?.invoke(event,chat)
    }


    private var timeZone = ""
    private val timestampToDate = mutableMapOf<Long,String>()
    private fun trackDate() {
        chatIdsToShowDate.clear()
        val currentTimezone = getTimezone()
        val timezoneChanged = timeZone!=currentTimezone
        timeZone = currentTimezone
        if(timezoneChanged){
            timestampToDate.clear()
        }
        chatIdTimestamp.entries.sortedBy {
            it.value
        }.forEach{
            val chatId = it.key
            val timestamp = it.value
            var display = timestampToDate[timestamp]?:""
            if(display.isEmpty()){
                val dateTime = DateTime(timestamp,DateTimeZone.UTC).toDateTime(DateTimeZone.getDefault())
                val date = dateTime.toString(
                    DateTimeFormat.forPattern(Style.dateDisplayFormat)
                )
                display = date
                timestampToDate[timestamp] = date
            }
            if(!chatIdsToShowDate.containsValue(display)){
                chatIdsToShowDate[chatId] = display
            }
        }
    }

    private fun getTimezone(): String {
        return DateTimeZone.getDefault().toTimeZone().displayName
    }

    private fun sortList() {
        list.sortBy {
            it.createdAt
        }
    }

    var childEventListener: ((ChatEvent, Chat?)->Unit)? = null
    init {
        firepo.childEvent = {
            when(it.event){
                Firepo.ChildEventType.CHANGED ->{
                    val chat = it.dataSnapshot?.toChat
                    onChildEvent(ChatEvent.CHANGED,chat)
                }
                Firepo.ChildEventType.REMOVED ->{
                    val chat = it.dataSnapshot?.toChat
                    onChildEvent(ChatEvent.REMOVED,chat)
                }
                Firepo.ChildEventType.MOVED ->{
                    val chat = it.dataSnapshot?.toChat
                    onChildEvent(ChatEvent.MOVED,chat)
                }
                Firepo.ChildEventType.ADDED -> {
                    val chat = it.dataSnapshot?.toChat
                    onChildEvent(ChatEvent.ADDED,chat)
                }
                Firepo.ChildEventType.CANCELLED -> {
                    onChildEvent(ChatEvent.CANCELLED,null)
                }
            }
        }
        Log.d("ffsflsjfs",this.hashCode().toString())
        startListening()
    }

    fun clear(){
        stopListening()
    }

    suspend fun chats(): List<Chat>{
        val snapshot = firepo.snapshot() ?: return emptyList()
        val value = snapshot.value
        val list = mutableListOf<Chat>()
        if(value is HashMap<*,*>){
            value.forEach {
                val s = it.value
                val js = Gson().toJson(s)
                val chat = Chat.fromString(js)
                if(chat!=null){
                    list.add(chat)
                }
            }
        }
        return list
    }

    fun insert(chat: Chat){
        firepo.put(chat.chatId,chat)
    }

    fun update(key: String,value: Any){
        firepo.put(key,value)
    }

    private fun startListening(){
        /*CoroutineScope(Dispatchers.IO).launch {
            val s = System.currentTimeMillis()
            val chats = chats()
            val e = System.currentTimeMillis()
            val d = e - s
            Log.d("time_elapsed",d.toString())
            list.clear()
            list.addAll(chats)
            sortList()
            onAdd(null)
            trackDate()
            firepo.startListening()
        }*/
        firepo.startListening()
    }

    private fun stopListening(){
        firepo.stopListening()
    }

    fun markSeen(chat: Chat) {
        update("${chat.chatId}/seenAt", utcTimestamp)
    }

    fun delete(chatId: String) {
        update("$chatId/deleted", true)
    }
}