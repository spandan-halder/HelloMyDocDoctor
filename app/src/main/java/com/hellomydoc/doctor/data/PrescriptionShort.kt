package com.hellomydoc.doctor.data

import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import org.joda.time.format.DateTimeFormat

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
                //Dt.millis_to_date(timestamp.toLong(),"EEE, MMM d, yyyy")
                val df = DateTimeFormat.forPattern("EEE, MMM d, yyyy")
                val dt = DateTime(timestamp.toLong(), DateTimeZone.UTC)
                df.print(dt)
            } else{
                ""
            }
        }
    val time: String
        get(){
            return if(timestamp!=null){
                //Dt.millis_to_date(timestamp.toLong(),"K:mm a")
                val df = DateTimeFormat.forPattern("HH:mm a")
                val dt = DateTime(timestamp.toLong(), DateTimeZone.UTC)
                df.print(dt)
            } else{
                ""
            }
        }
}
