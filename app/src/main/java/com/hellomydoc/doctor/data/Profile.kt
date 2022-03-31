package com.hellomydoc.doctor.data

data class ProfileResponse(
    val success: Boolean,
    val message: String,
    val profile: Profile
)
data class Profile(
    val id: String,
    val name: String,
    val image: String
)
