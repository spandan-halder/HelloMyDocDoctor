package com.hellomydoc.doctor.activities

import android.os.Bundle
import android.view.View
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope
import com.hellomydoc.doctor.*
import com.hellomydoc.doctor.R
import com.hellomydoc.doctor.data.AppointmentHistory
import com.hellomydoc.doctor.data.resp
import com.hellomydoc.doctor.views.Pops
import kotlinx.coroutines.launch

class AppointmentHistoryActivity : AbstractActivity() {
    private var iv_menu: View? = null
    private var iv_sort: View? = null
    private var cv_list: ComposeView? = null
    private var appointments: List<AppointmentHistory> = listOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_appointment_history)

        iv_menu = findViewById(R.id.iv_menu)
        iv_sort = findViewById(R.id.iv_sort)
        cv_list = findViewById(R.id.cv_list)

        iv_menu?.setOnClickListener {
            finish()
        }

        iv_sort?.setOnClickListener {
            sortAppointments()
        }

        showAppointments()
        fetchAppointments()
    }

    private fun fetchAppointments() {
        wait = true
        lifecycleScope.launch {
            try {
                val response = repository.getAppointmentsHistory(repository.userUid).resp
                wait = false
                when{
                    response.isError->response.errorMessage.toastLong(this@AppointmentHistoryActivity)
                    else->{
                        when{
                            response.isSuccess->{
                                val r = response.body
                                when{
                                    r!=null->{
                                        if(r.success){
                                            appointments = r.appointments
                                            showAppointments()
                                        }
                                        else{
                                            r.message.toast(this@AppointmentHistoryActivity)
                                        }
                                    }
                                    else->{
                                        getString(R.string.something_went_wrong).toast(this@AppointmentHistoryActivity)
                                    }
                                }
                            }
                            else->{
                                getString(R.string.something_went_wrong).toast(this@AppointmentHistoryActivity)
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                getString(R.string.something_went_wrong).toast(this@AppointmentHistoryActivity)
            }
        }
    }

    private fun showAppointments() {
        cv_list?.setContent {
            LazyColumn(contentPadding = PaddingValues(8.dp)) {
                items(appointments) { p ->
                    Column(modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(vertical = 12.dp)){
                        Row(modifier = Modifier.wrapContentSize(),
                        verticalAlignment = Alignment.CenterVertically){
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .size(64.dp)
                                    .background(
                                        colorResource(id = R.color.very_light_red),
                                        shape = CircleShape
                                    )) {
                                Icon(
                                    modifier = Modifier.size(32.dp),
                                    painter = painterResource(id = R.drawable.ic_medical_prescription),
                                    tint = Color.Red,
                                    contentDescription = null // decorative element
                                )
                            }
                            Spacer(modifier = Modifier.size(12.dp))
                            Column {
                                Text(p.patient,
                                fontWeight = FontWeight.W600,
                                    fontSize = 18.sp
                                )
                                Spacer(modifier = Modifier.size(4.dp))
                                Text(p.symptoms)
                            }
                        }
                        Spacer(modifier = Modifier.size(12.dp))
                        Row(modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween){
                            val textStyle = TextStyle(
                                fontWeight = FontWeight.W600, color = Color.Black, fontSize = 16.sp
                            )
                            Column {
                                Text("DATE")
                                Text(p.date, style = textStyle)
                            }
                            Column {
                                Text("TIME")
                                Text(p.time,style = textStyle)
                            }
                            Column {
                                Text("PRESCRIBED")
                                Text(if(p.prescribed) "Yes" else "No", style = textStyle)
                            }
                        }
                    }
                    Divider(
                        color = Color.Gray,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(0.5.dp)
                    )
                }
            }
        }
    }

    private var lastSortParam = ""
    private var lastSortAscending = true
    private fun sortAppointments() {
        Pops(
            this,
            iv_sort,
            listOf(
                getString(R.string.sort_by_date),
                getString(R.string.sort_by_patient),
                getString(R.string.sort_by_prescribed),
            )
        ){it ->
            lastSortAscending = if(lastSortParam==it) !lastSortAscending else true
            lastSortParam = it
            appointments = when(it){
                R.string.sort_by_date.string->{
                    if(lastSortAscending){
                        appointments.sortedBy {
                            it.timestamp.toLong()
                        }
                    } else{
                        appointments.sortedByDescending {
                            it.timestamp.toLong()
                        }
                    }
                }
                R.string.sort_by_patient.string->{
                    if(lastSortAscending){
                        appointments.sortedBy {
                            it.patient
                        }
                    } else{
                        appointments.sortedByDescending {
                            it.patient
                        }
                    }
                }
                R.string.sort_by_prescribed.string->{
                    if(lastSortAscending){
                        appointments.sortedBy {
                            it.prescribed
                        }
                    } else{
                        appointments.sortedByDescending {
                            it.prescribed
                        }
                    }
                }
                else -> {appointments}
            }
            showAppointments()
        }.show()
    }
}