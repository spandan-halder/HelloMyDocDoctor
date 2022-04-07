package com.hellomydoc.doctor.data

import com.hellomydoc.doctor.customViews.toShortGender
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import org.joda.time.format.DateTimeFormat

data class Patient(
    val id: String,
    val icon: String,
    val name: String,
    val age: Int,
    val gender: String
)

data class AppointmentData(
    val id: String,
    val patient: Patient,
    val type: String,
    val symptoms: String,
    val timestamp: String,
    val prescribed: Boolean,
    val prescription: Prescription?
)
{
    val shortDescription: String
    get(){
        return "${patient.name} (${patient.gender.toShortGender}-${patient.age})"
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

data class AppointmentsResponse(
    val success: Boolean,
    val message: String,
    val appointments: List<AppointmentData>
)

data class AppointmentResponse(
    val success: Boolean,
    val message: String,
    val appointment: AppointmentData
)