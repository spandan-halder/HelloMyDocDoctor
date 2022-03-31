package com.hellomydoc.doctor.activities

import android.os.Bundle
import android.view.View
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope
import com.hellomydoc.doctor.*
import com.hellomydoc.doctor.R
import com.hellomydoc.doctor.data.PrescriptionShort
import com.hellomydoc.doctor.data.resp
import kotlinx.coroutines.launch

class PrescriptionsActivity : AbstractActivity() {
    private var iv_menu: View? = null
    private var cv_list: ComposeView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_prescriptions)

        iv_menu = findViewById<View>(R.id.iv_menu)?.apply {
            setOnClickListener {
                finish()
            }
        }

        cv_list = findViewById(R.id.cv_list)

        if(savedInstanceState==null){
            fetchPrescriptions()
        }
    }

    private var prescriptions: List<PrescriptionShort> = listOf()
    private fun fetchPrescriptions() {
        wait = true
        lifecycleScope.launch {
            try {
                val response = repository.getPrescriptions(repository.userUid).resp
                wait = false
                when{
                    response.isError->response.errorMessage.toastLong(this@PrescriptionsActivity)
                    else->{
                        when{
                            response.isSuccess->{
                                val r = response.body
                                when{
                                    r!=null->{
                                        if(r.success){
                                            prescriptions = r.prescriptions
                                            renderPrescriptions()
                                        }
                                        else{
                                            r.message.toast(this@PrescriptionsActivity)
                                        }
                                    }
                                    else->{
                                        getString(R.string.something_went_wrong).toast(this@PrescriptionsActivity)
                                    }
                                }
                            }
                            else->{
                                getString(R.string.something_went_wrong).toast(this@PrescriptionsActivity)
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                getString(R.string.something_went_wrong).toast(this@PrescriptionsActivity)
            }
        }
    }

    @OptIn(ExperimentalMaterialApi::class)
    private fun renderPrescriptions() {
        cv_list?.setContent {
            listContents()
        }
    }

    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    private fun listContents() {
        MaterialTheme{
            LazyColumn(contentPadding = PaddingValues(8.dp)){
                items(prescriptions){p->
                    Card(
                        modifier = Modifier.padding(4.dp),
                        shape = RoundedCornerShape(8.dp),
                        elevation = 2.dp,
                        border = BorderStroke(1.dp, Color(250,250,250)),
                        onClick = {
                            onClickPrescription(p)
                        }
                    ){
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight()
                                .padding(8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.wrapContentHeight()) {
                                Text(
                                    p.patientSummary,
                                    fontSize = 18.sp,
                                    color = Color.Black,
                                    fontWeight = FontWeight.W600
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(p.appointmentTypeDesc, fontSize = 16.sp)
                                Spacer(modifier = Modifier.height(8.dp))
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        modifier = Modifier
                                            .size(18.dp),
                                        tint = Color.Gray,
                                        painter = painterResource(id = R.drawable.ic_baseline_calendar_month_24),
                                        contentDescription = null // decorative element
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(p.date)
                                }
                            }
                            Row {
                                Image(
                                    ImageVector.vectorResource(
                                        id = R.drawable.ic_prescription_svgrepo_com
                                    ),
                                    "Localized description",
                                    modifier = Modifier.size(48.dp),
                                )
                                IconButton(onClick = { onSharePrescription(p) }) {
                                    Icon(
                                        modifier = Modifier
                                            .size(48.dp)
                                            .padding(8.dp),
                                        tint = Color.Gray,
                                        imageVector = Icons.Default.Share,
                                        contentDescription = null // decorative element
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun onSharePrescription(p: PrescriptionShort) {

    }

    private fun onClickPrescription(p: PrescriptionShort) {
        navi()
            .target(AddPrescriptionActivity::class.java)
            .add(Constants.APPOINTMENT_ID_KEY,p.appointmentId)
            ?.add(Constants.MODE_EDITING,true)
            ?.finish(false)
            ?.go()
    }
}