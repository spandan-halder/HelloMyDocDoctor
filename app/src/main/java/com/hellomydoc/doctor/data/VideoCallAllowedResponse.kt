package com.hellomydoc.doctor.data

data class VideoCallAllowedResponse(
    val success: Boolean,
    val message: String,
    val startTime: Long,
    val timeSpan: Long
)
