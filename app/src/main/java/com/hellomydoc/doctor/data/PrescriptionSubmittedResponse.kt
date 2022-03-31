package com.hellomydoc.doctor.data

data class PrescriptionSubmittedResponse(
    val success: Boolean,
    val message: String,
    val prescriptionSubmitted:Boolean,
    val prescriptionId: String
)