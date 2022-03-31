package com.hellomydoc.doctor.data

data class PrescribedMedicine(
    val name: String,
    val doseValue: Float,
    val doseUnit: String,
    val timePeriodValue: Int,
    val timePeriodUnit: String,
    val frequency: String
)
