package com.hellomydoc.doctor.data

data class TimeUnitsResponse(
    val success: Boolean,
    val message: String,
    val units: List<String>
)