package com.hellomydoc.doctor.data

data class Prescription(
    val appointmentId: String,
    val medicines: List<PrescribedMedicine>,
    val labTests: List<String>,
    val prescriptionId: String = "",
)
