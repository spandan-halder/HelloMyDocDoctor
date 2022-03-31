package com.hellomydoc.doctor.data

import com.hellomydoc.doctor.Dt

data class PrescriptionShort(
    val patientSummary: String,
    val appointmentType: String,
    val timestamp: String,
    val prescriptionId: String,
    val appointmentId: String
){
    val appointmentTypeDesc: String
    get(){
        return when(appointmentType.uppercase()){
            "VIDEO"->"Video Call Appointments"
            "VOICE"->"Voice Call Appointments"
            "CHAT"->"Chat Appointments"
            else -> {appointmentType}
        }
    }

    val date: String
        get(){
            return if(timestamp!=null){
                Dt.millis_to_date(timestamp.toLong(),"EEE, MMM d, yyyy")
            } else{
                ""
            }
        }
    val time: String
        get(){
            return if(timestamp!=null){
                Dt.millis_to_date(timestamp.toLong(),"K:mm a")
            } else{
                ""
            }
        }
}
