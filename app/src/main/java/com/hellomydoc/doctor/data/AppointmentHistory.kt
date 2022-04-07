package com.hellomydoc.doctor.data

import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import org.joda.time.format.DateTimeFormat

data class AppointmentHistory(
    val id: String,
    val patient: String,
    val symptoms: String,
    val timestamp: String,
    val prescribed: Boolean
){
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

data class AppointmentHistoryResponse(
    val success: Boolean,
    val message: String,
    val appointments: List<AppointmentHistory>
)
