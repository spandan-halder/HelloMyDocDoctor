package com.hellomydoc.chat.uploader

import android.content.Context
import android.util.Log
import com.hellomydoc.chat.ChatBox
import net.gotev.uploadservice.data.UploadInfo
import net.gotev.uploadservice.exceptions.UploadError
import net.gotev.uploadservice.exceptions.UserCancelledUploadException
import net.gotev.uploadservice.network.ServerResponse
import net.gotev.uploadservice.observer.request.RequestObserverDelegate

class GlobalRequestObserverDelegate : RequestObserverDelegate {
    override fun onProgress(context: Context, uploadInfo: UploadInfo) {
        ChatBox.onUploadProgress(context, uploadInfo)
        Log.e("RECEIVER", "Progress: $uploadInfo")
    }

    override fun onSuccess(
        context: Context,
        uploadInfo: UploadInfo,
        serverResponse: ServerResponse
    ) {
        ChatBox.onUploadSuccess(context, uploadInfo, serverResponse)
        Log.e("RECEIVER", "Success: $serverResponse")
    }

    override fun onError(context: Context, uploadInfo: UploadInfo, exception: Throwable) {
        when (exception) {
            is UserCancelledUploadException -> {
                ChatBox.onUserCancelledUpload(context, uploadInfo)
                Log.e("RECEIVER", "Error, user cancelled upload: $uploadInfo")
            }

            is UploadError -> {
                ChatBox.onUploadError(context, uploadInfo)
                Log.e("RECEIVER", "Error, upload error: ${exception.serverResponse}")
            }

            else -> {
                ChatBox.onUploadUnknownError(context, uploadInfo)
                Log.e("RECEIVER", "Error: $uploadInfo", exception)
            }
        }
    }

    override fun onCompleted(context: Context, uploadInfo: UploadInfo) {
        ChatBox.onUploadCompleted(context, uploadInfo)
        Log.e("RECEIVER", "Completed: $uploadInfo")
    }

    override fun onCompletedWhileNotObserving() {
        ChatBox.onUploadCompleteWhileNotObserving()
        Log.e("RECEIVER", "Completed while not observing")
    }
}