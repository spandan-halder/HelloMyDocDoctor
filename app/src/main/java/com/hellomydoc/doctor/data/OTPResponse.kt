package com.hellomydoc.doctor.data

data class OTPResponse(
    val success: Boolean,
    val message: String,
    val otp: String,
)