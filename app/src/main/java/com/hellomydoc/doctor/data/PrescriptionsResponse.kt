package com.hellomydoc.doctor.data

data class PrescriptionsResponse(
    val success: Boolean,
    val message: String,
    val prescriptions: List<PrescriptionShort>
)
