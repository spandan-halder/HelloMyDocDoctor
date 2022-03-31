package com.hellomydoc.doctor.activities

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.commit
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.hellomydoc.doctor.*
import com.hellomydoc.doctor.R
import com.hellomydoc.doctor.anyListView.AnyListView
import com.hellomydoc.doctor.anyListView.CustomDividerItemDecoration
import com.hellomydoc.doctor.anyListView.CustomItemDecorator
import com.hellomydoc.doctor.data.AppointmentData
import com.hellomydoc.doctor.data.PrescribedMedicine
import com.hellomydoc.doctor.data.Prescription
import com.hellomydoc.doctor.data.resp
import com.hellomydoc.doctor.fragments.AddLabTestFragment
import com.hellomydoc.doctor.fragments.AddMedicineFragment
import kotlinx.coroutines.launch

class AddPrescriptionActivity : AbstractActivity() {
    private var bt_prescribe: AppCompatButton? = null
    private var pb_patient_details: View? = null
    private var iv_menu: View? = null
    private var tv_patient_details: TextView? = null
    private var tv_medicine_title: TextView? = null
    private var tv_test_title: TextView? = null
    private var medicineFragment: AddMedicineFragment? = null
    private var labTestFragment: AddLabTestFragment? = null
    data class ChildCallback(
        val onEvent: (String,String,Any?)->Unit
    )

    private var fragmentContainerView: FrameLayout? = null
    private var bt_add_medicine: Button? = null
    private var bt_add_lab_test: Button? = null
    private var iv_medicine_toggle: View? = null
    private var iv_test_toggle: View? = null
    private var llv_medicine_contents: LinearLayoutCompat? = null
    private var llv_test_contents: LinearLayoutCompat? = null

    private var prescribedMedicines: MutableList<PrescribedMedicine> = mutableListOf()
    private var labTests: MutableList<String> = mutableListOf()

    private fun updateMedicineListRendering() {
        updateAsMedicinesCount()
        findViewById<AnyListView>(R.id.alv_medicines)
            .configure(
                AnyListView.Configurator(
                    isExpanded = {true},
                    state = AnyListView.STATE.DATA,
                    itemCount = {prescribedMedicines.size},
                    itemType = {
                        it
                    },
                    itemDecorations = {
                        listOf(
                            CustomItemDecorator().apply{
                                itemOffsetter={pos,rect->
                                    if(pos==0){
                                        rect.top = 8.dpToPx
                                    }
                                    rect.bottom = 8.dpToPx
                                }
                                mDivider = R.drawable.text_box_border.drawable()
                            },
                            CustomDividerItemDecoration(this, LinearLayoutManager.VERTICAL)
                        )
                    },
                    fromLayout = {false},
                    itemView = {pos->
                        ComposeView(this).apply {
                            layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)

                            // The setContent call takes a Composable lambda extension which can render Composable UI.
                            setContent {
                                var data = prescribedMedicines.getOrNull(pos)
                                val bold400 = Font(R.font.roboto_bold, FontWeight.W400)
                                val italic = Font(R.font.roboto_italic)
                                Column(modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(data?.name?.uppercase()?:"",style = TextStyle(
                                            fontFamily = FontFamily(bold400),
                                            fontSize = 20.sp,
                                            color = colorResource(R.color.blue)
                                        )
                                        )
                                        IconButton(onClick = {
                                            prescribedMedicines.removeAt(pos)
                                            updateMedicineListRendering()
                                        }) {
                                            Icon(
                                                painter = painterResource(id = R.drawable.ic_baseline_delete_forever_24),
                                                tint = Color.Red,
                                                contentDescription = null // decorative element
                                            )
                                        }

                                    }

                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.Bottom) {


                                        Text("${data?.doseValue} ${data?.doseUnit}",
                                            style = TextStyle(
                                                fontFamily = FontFamily(italic),
                                                color = colorResource(R.color.green)
                                            )
                                        )

                                        Text(data?.frequency?:"",style = TextStyle(
                                            color = colorResource(id = R.color.orange)
                                        )
                                        )

                                        Text("${data?.timePeriodValue} ${data?.timePeriodUnit}",style = TextStyle(
                                            fontFamily = FontFamily(bold400),
                                            fontSize = 20.sp,
                                        )
                                        )
                                    }
                                }
                            }
                        }
                    },
                    onView = {pos,view->

                    }
                ))
    }

    private fun updateAsMedicinesCount() {
        val count = prescribedMedicines.size
        tv_medicine_title?.text = "${R.string.medicines.string}(${count})"
    }

    private var appointmentId = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_prescription)

        appointmentId = intent?.extras?.getString(Constants.APPOINTMENT_ID_KEY,"")?:""


        tv_medicine_title = findViewById(R.id.tv_medicine_title)
        tv_patient_details = findViewById(R.id.tv_patient_details)
        pb_patient_details = findViewById(R.id.pb_patient_details)
        bt_prescribe = findViewById(R.id.bt_prescribe)
        tv_test_title = findViewById(R.id.tv_test_title)
        iv_menu = findViewById(R.id.iv_menu)

        bt_prescribe?.setOnClickListener {
            tryAddPrescription()
        }

        intent?.getBooleanExtra(Constants.MODE_EDITING,false)?.apply {
            if(this){
                bt_prescribe?.text = getString(R.string.update)
            }
        }

        iv_menu?.setOnClickListener {
            finish()
        }

        if(savedInstanceState==null){
            fetchAppointmentData()
        }

        updateMedicineListRendering()
        updateLabTestsListRendering()

        Messanger.subscribe(myClassName,AddMedicineFragment::class.java.simpleName){it ->
            when(it?.key){
                Constants.MESSAGE_DESTROY_OK->{
                    closeAddMedicinePage()
                }
                Constants.MESSAGE_DESTROYED->{
                    medicineFragment = null
                    fragmentContainerView?.visibility = View.GONE
                }
                Constants.MESSAGE_MEDICINES->{
                    prescribedMedicines = (it.data as? MutableList<PrescribedMedicine>)?: mutableListOf()
                    updateMedicineListRendering()
                }
                Constants.MESSAGE_FINISHED->{
                    closeAddMedicinePage()
                }
            }
        }

        subscribe(AddLabTestFragment::class.java.simpleName){
            when(it?.key){
                Constants.MESSAGE_FINISHED->{
                    closeAddLabTestPage()
                }
                Constants.MESSAGE_DESTROYED->{
                    labTestFragment = null
                    fragmentContainerView?.visibility = View.GONE
                }
                Constants.MESSAGE_LAB_TESTS->{
                    labTests = (it.data as? MutableList<String>)?: mutableListOf()
                    updateLabTestsListRendering()
                }
            }
        }

        bt_add_medicine = findViewById(R.id.bt_add_medicine)
        bt_add_lab_test = findViewById(R.id.bt_add_lab_test)
        fragmentContainerView = findViewById(R.id.fragmentContainerView)

        iv_medicine_toggle = findViewById(R.id.iv_medicine_toggle)
        iv_test_toggle = findViewById(R.id.iv_test_toggle)

        llv_medicine_contents = findViewById(R.id.llv_medicine_contents)
        llv_test_contents = findViewById(R.id.llv_test_contents)

        iv_medicine_toggle?.setOnClickListener {
            if(iv_medicine_toggle?.rotation==0f){
                iv_medicine_toggle?.rotation = 180f
                llv_medicine_contents?.visibility = View.VISIBLE
            }
            else{
                iv_medicine_toggle?.rotation = 0f
                llv_medicine_contents?.visibility = View.GONE
            }
        }

        iv_test_toggle?.setOnClickListener {
            if(iv_test_toggle?.rotation==0f){
                iv_test_toggle?.rotation = 180f
                llv_test_contents?.visibility = View.VISIBLE
            }
            else{
                iv_test_toggle?.rotation = 0f
                llv_test_contents?.visibility = View.GONE
            }
        }

        bt_add_medicine?.setOnClickListener {
            openAddMedicinePage()
        }

        bt_add_lab_test?.setOnClickListener {
            openAddLabTestPage()
        }

        fragmentContainerView?.setOnClickListener {  }
    }

    private fun tryAddPrescription() {
        if(appointment!=null && (prescribedMedicines.size!=0 || labTests.size!=0)){
            submitPrescription()
        }
        else{
            R.string.can_not_proceed.string.toast(this)
        }
    }

    private fun submitPrescription() {
        val prescription = Prescription(
            appointmentId = appointment?.id?:"",
            medicines = prescribedMedicines,
            labTests = labTests
        )
        wait = true
        lifecycleScope.launch {
            try {
                val response = repository.submitPrescription(prescription).resp
                wait = false
                pb_patient_details?.visibility = View.GONE
                when{
                    response.isError->response.errorMessage.toastLong(this@AddPrescriptionActivity)
                    else->{
                        when{
                            response.isSuccess->{
                                val r = response.body
                                when{
                                    r!=null->{
                                        if(r.success){
                                            onPrescriptionSubmitted()
                                        }
                                        else{
                                            r.message.toast(this@AddPrescriptionActivity)
                                        }
                                    }
                                    else->{
                                        getString(R.string.something_went_wrong).toast(this@AddPrescriptionActivity)
                                    }
                                }
                            }
                            else->{
                                getString(R.string.something_went_wrong).toast(this@AddPrescriptionActivity)
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                getString(R.string.something_went_wrong).toast(this@AddPrescriptionActivity)
            }
        }
    }

    private fun onPrescriptionSubmitted() {
        R.string.prescription_submitted_successfully.string.toast(this)
        finish()
    }

    private var appointment: AppointmentData? = null
    private fun fetchAppointmentData() {
        pb_patient_details?.visibility = View.VISIBLE
        lifecycleScope.launch {
            try {
                val response = repository.getAppointmentDetails(appointmentId).resp
                pb_patient_details?.visibility = View.GONE
                when{
                    response.isError->response.errorMessage.toastLong(this@AddPrescriptionActivity)
                    else->{
                        when{
                            response.isSuccess->{
                                val appointmentsResponse = response.body
                                when{
                                    appointmentsResponse!=null->{
                                        if(appointmentsResponse.success){
                                            appointment = appointmentsResponse.appointment
                                            prescribedMedicines = appointmentsResponse.appointment.prescription?.medicines?.toMutableList()?: mutableListOf()
                                            labTests = appointmentsResponse.appointment.prescription?.labTests?.toMutableList()?: mutableListOf()
                                            showAppointment()
                                        }
                                        else{
                                            appointmentsResponse.message.toast(this@AddPrescriptionActivity)
                                        }
                                    }
                                    else->{
                                        getString(R.string.something_went_wrong).toast(this@AddPrescriptionActivity)
                                    }
                                }
                            }
                            else->{
                                getString(R.string.something_went_wrong).toast(this@AddPrescriptionActivity)
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                getString(R.string.something_went_wrong).toast(this@AddPrescriptionActivity)
            }
        }
    }

    private fun showAppointment() {
        tv_patient_details?.text = appointment?.shortDescription

        updateMedicineListRendering()
        updateLabTestsListRendering()
    }

    private fun updateLabTestsListRendering() {
        updateAsLabTestsCount()
        labTestsRender()
    }

    private fun labTestsRender() {
        findViewById<AnyListView>(R.id.alv_lab_tests).composeRender(
            labTests,
            isExpanded = true,
        decorators = listOf(
            CustomItemDecorator().apply{
                itemOffsetter = {pos,rect->
                    if(pos==0){
                        rect.top = 8.dpToPx
                    }
                    rect.bottom = 8.dpToPx
                }
            },
            CustomDividerItemDecoration(this,LinearLayoutManager.VERTICAL)
        )){pos,data->
            val bold400 = Font(R.font.roboto_bold, FontWeight.W400)
            Row(
                modifier = Modifier.fillMaxWidth().padding(12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(data?.toString()?.uppercase()?:"",style = TextStyle(
                    fontFamily = FontFamily(bold400),
                    fontSize = 20.sp,
                    color = colorResource(R.color.blue)
                )
                )
                IconButton(onClick = {
                    labTests.removeAt(pos)
                    updateLabTestsListRendering()
                }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_baseline_delete_forever_24),
                        tint = Color.Red,
                        contentDescription = null // decorative element
                    )
                }

            }
        }
    }

    private fun updateAsLabTestsCount() {
        tv_test_title?.text = "${R.string.lab_tests.string}(${labTests.size})"
    }

    private fun closeAddLabTestPage() {
        val fm = supportFragmentManager
        fm.commit {
            labTestFragment?.let {
                setCustomAnimations(
                    R.anim.enter_anim,
                    R.anim.exit_anim,
                    R.anim.close_enter,
                    R.anim.close_exit
                )
                remove(it)
            }
        }
    }

    private fun openAddMedicinePage() {
        fragmentContainerView?.visibility = View.VISIBLE
        val fm = supportFragmentManager
        fm.commit {
            setCustomAnimations(
                R.anim.enter_anim,
                R.anim.exit_anim,
                R.anim.close_enter,
                R.anim.exit_anim
            )
            medicineFragment = AddMedicineFragment.newInstance(prescribedMedicines)
            replace(R.id.fragmentContainerView, medicineFragment!!)
        }
    }

    private fun openAddLabTestPage() {
        fragmentContainerView?.visibility = View.VISIBLE
        val fm = supportFragmentManager
        fm.commit {
            setCustomAnimations(
                R.anim.enter_anim,
                R.anim.exit_anim,
                R.anim.close_enter,
                R.anim.exit_anim
            )
            labTestFragment = AddLabTestFragment.newInstance(labTests)
            replace(R.id.fragmentContainerView, labTestFragment!!)
        }
    }

    private fun closeAddMedicinePage() {
        val fm = supportFragmentManager
        fm.commit {
            medicineFragment?.let {
                setCustomAnimations(
                    R.anim.enter_anim,
                    R.anim.exit_anim,
                    R.anim.close_enter,
                    R.anim.close_exit
                )
                remove(it)
            }
        }
    }

    override fun onBackPressed() {
        if(medicineFragment!=null||labTestFragment!=null){
            Messanger.publish(myClassName, Message(Constants.MESSAGE_PLEASE_DESTROY))
        }
        else{
            super.onBackPressed()
        }
    }

    override fun onDestroy() {
        Messanger.cancel(myClassName)
        super.onDestroy()
    }
}