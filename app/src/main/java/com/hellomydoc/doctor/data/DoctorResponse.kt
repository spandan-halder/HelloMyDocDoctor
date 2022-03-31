package com.hellomydoc.doctor.data

data class Doctor(
    val email: String,
    val id: String,
    val image: String,
    val mobile: String,
    val name: String,
    val user_id: String
)

data class DoctorResponse(
    val success: Boolean,
    val message: String,
    val doctor: Doctor
)
