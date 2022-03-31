package com.hellomydoc.doctor.data

data class DoseUnitsResponse(
    val success: Boolean,
    val message: String,
    val units: List<String>
)