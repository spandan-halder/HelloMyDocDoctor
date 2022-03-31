package com.hellomydoc.doctor.data

data class DosesResponse(
    val success: Boolean,
    val message: String,
    val doses: List<String>
)
