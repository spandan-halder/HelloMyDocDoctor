package com.hellomydoc.doctor.data

import com.hellomydoc.doctor.Dt

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

data class AppointmentHistoryResponse(
    val success: Boolean,
    val message: String,
    val appointments: List<AppointmentHistory>
)
