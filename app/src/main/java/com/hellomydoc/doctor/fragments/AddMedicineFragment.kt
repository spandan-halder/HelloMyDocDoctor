package com.hellomydoc.doctor.fragments

import android.content.Context
import android.os.Bundle
import android.text.TextWatcher
import android.view.*
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.TextView.OnEditorActionListener
import androidx.appcompat.widget.AppCompatButton
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
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
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.hellomydoc.doctor.*
import com.hellomydoc.doctor.R
import com.hellomydoc.doctor.activities.AddPrescriptionActivity
import com.hellomydoc.doctor.anyListView.AnyListView
import com.hellomydoc.doctor.anyListView.CustomItemDecorator
import com.hellomydoc.doctor.anyListView.FlowLayoutManager
import com.hellomydoc.doctor.data.PrescribedMedicine
import com.hellomydoc.doctor.data.resp
import com.hellomydoc.doctor.databinding.AddMedicineFragmentLayoutBinding
import com.hellomydoc.doctor.dialogs.CustomDialog
import kotlinx.coroutines.launch
import java.net.URL


class AddMedicineFragment : Fragment() {
    private var prescribedMedicines: MutableList<PrescribedMedicine> = mutableListOf()
    private var dose = ""
    private var units = listOf(
        "Ml",
        "Mg",
        "Piece",
        "Spoon",
        "Drop(s)",
        "Litre",
        "Times",
        "Glass",
        "Bottle",
        "Packet",
        "Unit",
    )

    private var timeUnits = listOf(
        "Day(s)",
        "Week(s)",
        "Month(s)",
        "Hour(s)",
        "Minute(s)"
    )

    private var doses = listOf(
        "BDAC",
        "BDPC",
        "ODPC",
        "TDPC"
    )
    companion object {
        @JvmStatic
        fun newInstance(pm: MutableList<PrescribedMedicine>) = AddMedicineFragment().apply {
            this.prescribedMedicines = pm
        }
    }
    private lateinit var binding: AddMedicineFragmentLayoutBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Messanger.subscribe(myClassName,AddPrescriptionActivity::class.java.simpleName){
            if(it?.key==Constants.MESSAGE_PLEASE_DESTROY){
                finalizeMedicinesAndBack()
            }
        }


    }

    private fun fetchDoses() {
        lifecycleScope.launch {
            try {
                var response = repository.getDoses().resp
                ////////////////////////////
                when{
                    response.isError->toastShort(response.errorMessage)
                    else->{
                        when{
                            response.isSuccess->{
                                val dosesResponse = response.body
                                when{
                                    dosesResponse!=null->{
                                        if(dosesResponse.success){
                                            doses = dosesResponse.doses
                                            showDoses()
                                        }
                                    }
                                    else->{
                                        toastShort(getString(R.string.something_went_wrong))
                                    }
                                }
                            }
                            else->{
                                toastShort(getString(R.string.something_went_wrong))
                            }
                        }
                    }
                }
                ////////////////////////////
            } catch (e: Exception) {
                toastShort(e.message?:"")
            }
        }
    }

    private fun fetchTimeUnits() {
        lifecycleScope.launch {
            try {
                var response = repository.getTimeUnits().resp
                ////////////////////////////
                when{
                    response.isError->toastShort(response.errorMessage)
                    else->{
                        when{
                            response.isSuccess->{
                                val unitsResponse = response.body
                                when{
                                    unitsResponse!=null->{
                                        if(unitsResponse.success){
                                            timeUnits = unitsResponse.units
                                        }
                                    }
                                    else->{
                                        toastShort(getString(R.string.something_went_wrong))
                                    }
                                }
                            }
                            else->{
                                toastShort(getString(R.string.something_went_wrong))
                            }
                        }
                    }
                }
                ////////////////////////////
            } catch (e: Exception) {
                toastShort(e.message?:"")
            }
        }
    }

    private fun fetchUnits() {
        lifecycleScope.launch {
            try {
                var response = repository.getDoseUnits().resp
                ////////////////////////////
                when{
                    response.isError->toastShort(response.errorMessage)
                    else->{
                        when{
                            response.isSuccess->{
                                val unitsResponse = response.body
                                when{
                                    unitsResponse!=null->{
                                        if(unitsResponse.success){
                                            units = unitsResponse.units
                                        }
                                    }
                                    else->{
                                        toastShort(getString(R.string.something_went_wrong))
                                    }
                                }
                            }
                            else->{
                                toastShort(getString(R.string.something_went_wrong))
                            }
                        }
                    }
                }
                ////////////////////////////
            } catch (e: Exception) {
                toastShort(e.message?:"")
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = AddMedicineFragmentLayoutBinding.inflate(layoutInflater)
        return binding.root
    }

    private var selected = ""
    private var ratvWatcher: TextWatcher? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        updateMedicineListRendering()

        binding.btSave.setOnClickListener {
            finalizeMedicinesAndBack()
        }

        binding.btAddNext.setOnClickListener {
            finalizeMedicines()
        }

        ratvWatcher = binding.ratvMedicines.addTextChangedListener {

        }

        if(savedInstanceState==null){
            fetchUnits()
            fetchTimeUnits()
            fetchDoses()
        }
        showDoses()

        binding.ratvMedicines.apply {
            itemResolver = {list,pos->
                (list?.getOrNull(pos) as? String)?:""
            }
            urlComposer = {
                URL("${Metar[Constants.BASE_URL]}/searchMedicine/$it")
            }
            processor = {
                it.toMutableList()
            }
        }


        binding.tvDoseUnit.setOnClickListener {
            try {
                openUnitDialog()
            } catch (e: Exception) {
            }
        }

        binding.tvDoseValue.setOnClickListener {
            try {
                openValuesDialog()
            } catch (e: Exception) {
            }
        }

        binding.tvTimeValue.setOnClickListener {
            try {
                openTimeValuesDialog()
            } catch (e: Exception) {
            }
        }

        binding.tvTimeUnit.setOnClickListener {
            try {
                openTimeUnitDialog()
            } catch (e: Exception) {
            }
        }
    }

    private fun openTimeUnitDialog() {
        CustomDialog
            .create(requireContext())
            .noBackground()
            .layout(R.layout.unit_dialog_layout)
            .with {
                val tvHead = findViewById<TextView>(R.id.tv_head)
                tvHead.text = getString(R.string.time_unit)
                val btDone = findViewById<View>(R.id.bt_done)
                val etInput = findViewById<EditText>(R.id.et_input)
                val alvUnits = findViewById<AnyListView>(R.id.alv_units)
                fun showUnits(units: List<String>){
                    alvUnits.configure(
                        AnyListView.Configurator(
                            noItemText = getString(R.string.no_items_matched),
                            state = AnyListView.STATE.DATA,
                            itemCount = {units.size},
                            itemType = {
                                it
                            },
                            fromLayout = {true},
                            layoutId = {R.layout.unit_item_layout},
                            viewId = {R.id.cl_root},
                            onView = {pos,view->
                                val bt = (view as? ViewGroup)?.findViewById<AppCompatButton>(R.id.bt_item)
                                bt?.text = units[pos]
                                bt?.setOnClickListener {
                                    val text = bt.text
                                    dismiss()
                                    onTimeUnitSelected(text.toString())
                                }
                            },
                            layoutManager = {
                                FlowLayoutManager()
                            }
                        )
                    )
                }
                showUnits(timeUnits)
                val tw = etInput.addTextChangedListener { it ->
                    val q = it?.toString()?:""
                    if(q.isEmpty){
                        btDone.visibility = View.GONE
                        showUnits(timeUnits)
                    }
                    else{
                        btDone.visibility = View.VISIBLE
                        showUnits(
                            timeUnits.filter {
                                it.lowercase().contains(q.lowercase())
                            }
                        )
                    }
                }
                btDone.setOnClickListener {
                    dismiss()
                    onTimeUnitSelected(etInput.text.toString())
                }
                etInput?.setOnEditorActionListener(OnEditorActionListener { v, actionId, event ->
                    if (actionId == EditorInfo.IME_ACTION_DONE) {
                        val value = etInput.text?.toString()?:""
                        if(value.isEmpty()){
                            dismiss()
                        }
                        else{
                            dismiss()
                            onTimeUnitSelected(value)
                        }
                        return@OnEditorActionListener true
                    }
                    false
                })
                setOnDismissListener {
                    etInput.removeTextChangedListener(tw)
                }
            }.show()
    }

    private fun openTimeValuesDialog() {
        CustomDialog
            .create(requireContext())
            .noBackground()
            .layout(R.layout.values_dialog_layout)
            .with {
                val tvHead = findViewById<TextView>(R.id.tv_head)
                tvHead.text = "Time Value"
                val ivInput = findViewById<View>(R.id.iv_input)
                val etInput = findViewById<EditText>(R.id.et_input)
                val llvNumbers = findViewById<View>(R.id.llv_numbers)
                ivInput.setOnClickListener {
                    if(etInput.visibility==View.GONE){
                        etInput.visibility = View.VISIBLE
                        llvNumbers.visibility = View.GONE
                        (ivInput as? ImageView)?.setColorFilter(R.color.red.color)
                        etInput?.requestFocus()

                        etInput?.performClick()

                        window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)

                        etInput?.setOnEditorActionListener(OnEditorActionListener { v, actionId, event ->
                            if (actionId == EditorInfo.IME_ACTION_DONE) {
                                val value = etInput.text?.toString()?:""
                                if(value.isEmpty()){
                                    dismiss()
                                }
                                else{
                                    dismiss()
                                    onTimeValueSelected(value)
                                }
                                return@OnEditorActionListener true
                            }
                            false
                        })
                    }
                    else{
                        etInput.visibility = View.GONE
                        llvNumbers.visibility = View.VISIBLE
                        (ivInput as? ImageView)?.setColorFilter(R.color.gray.color)
                        etInput?.clearFocus()

                        val imm =
                            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
                        imm!!.hideSoftInputFromWindow(etInput.windowToken, 0)
                    }
                }
                findViewById<ViewGroup>(R.id.cl_root)
                    .findViewsByTag("dose_value_button")
                    .forEach { it ->
                        it.setOnClickListener {
                            val text = (it as? AppCompatButton)?.text.toString()
                            dismiss()
                            onTimeValueSelected(text)
                        }
                    }
            }
            .show()
    }

    private fun openValuesDialog() {
        CustomDialog
            .create(requireContext())
            .noBackground()
            .layout(R.layout.values_dialog_layout)
            .with {

                val ivInput = findViewById<View>(R.id.iv_input)
                val etInput = findViewById<EditText>(R.id.et_input)
                val llvNumbers = findViewById<View>(R.id.llv_numbers)
                ivInput.setOnClickListener {
                    if(etInput.visibility==View.GONE){
                        etInput.visibility = View.VISIBLE
                        llvNumbers.visibility = View.GONE
                        (ivInput as? ImageView)?.setColorFilter(R.color.red.color)
                        etInput?.requestFocus()

                        etInput?.performClick()

                        window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)

                        etInput?.setOnEditorActionListener(OnEditorActionListener { v, actionId, event ->
                            if (actionId == EditorInfo.IME_ACTION_DONE) {
                                val value = etInput.text?.toString()?:""
                                if(value.isEmpty()){
                                    dismiss()
                                }
                                else{
                                    dismiss()
                                    onDoseValueSelected(value)
                                }
                                return@OnEditorActionListener true
                            }
                            false
                        })
                    }
                    else{
                        etInput.visibility = View.GONE
                        llvNumbers.visibility = View.VISIBLE
                        (ivInput as? ImageView)?.setColorFilter(R.color.gray.color)
                        etInput?.clearFocus()

                        val imm =
                            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
                        imm!!.hideSoftInputFromWindow(etInput.windowToken, 0)
                    }
                }
                findViewById<ViewGroup>(R.id.cl_root)
                    .findViewsByTag("dose_value_button")
                    .forEach { it ->
                        it.setOnClickListener {
                            val text = (it as? AppCompatButton)?.text.toString()
                            dismiss()
                            onDoseValueSelected(text)
                        }
                    }
            }
            .show()
    }

    fun onUnitSelected(text: String) {
        binding.tvDoseUnit.text = text
    }

    private fun openUnitDialog() {
        CustomDialog
            .create(requireContext())
            .noBackground()
            .layout(R.layout.unit_dialog_layout)
            .with {
                val btDone = findViewById<View>(R.id.bt_done)
                val etInput = findViewById<EditText>(R.id.et_input)
                val alvUnits = findViewById<AnyListView>(R.id.alv_units)
                fun showUnits(units: List<String>){
                    alvUnits.configure(
                        AnyListView.Configurator(
                            noItemText = "No items matched",
                            state = AnyListView.STATE.DATA,
                            itemCount = {units.size},
                            itemType = {
                                it
                            },
                            fromLayout = {true},
                            layoutId = {R.layout.unit_item_layout},
                            viewId = {R.id.cl_root},
                            onView = {pos,view->
                                val bt = (view as? ViewGroup)?.findViewById<AppCompatButton>(R.id.bt_item)
                                bt?.text = units[pos]
                                bt?.setOnClickListener {
                                    val text = bt.text
                                    dismiss()
                                    onUnitSelected(text.toString())
                                }
                            },
                            layoutManager = {
                                FlowLayoutManager()
                            }
                        )
                    )
                }
                showUnits(units)
                val tw = etInput.addTextChangedListener { it->
                    val q = it?.toString()?:""
                    if(q.isEmpty){
                        btDone.visibility = View.GONE
                        showUnits(units)
                    }
                    else{
                        btDone.visibility = View.VISIBLE
                        showUnits(
                            units.filter {
                                it.lowercase().contains(q.lowercase())
                            }
                        )
                    }
                }
                btDone.setOnClickListener {
                    dismiss()
                    onUnitSelected(etInput.text.toString())
                }
                etInput?.setOnEditorActionListener(OnEditorActionListener { v, actionId, event ->
                    if (actionId == EditorInfo.IME_ACTION_DONE) {
                        val value = etInput.text?.toString()?:""
                        if(value.isEmpty()){
                            dismiss()
                        }
                        else{
                            dismiss()
                            onUnitSelected(value)
                        }
                        return@OnEditorActionListener true
                    }
                    false
                })
                setOnDismissListener {
                    etInput.removeTextChangedListener(tw)
                }
            }.show()
    }

    private fun finalizeMedicines():Boolean {
        val medicine = binding.ratvMedicines.text.toString()
        if(medicine.isEmpty){
            toastShort(R.string.please_select_a_medicine.string)
            return true
        }
        val doseValue = binding.tvDoseValue.text.toString()
        if(doseValue.isEmpty){
            toastShort(R.string.please_select_dose_value.string)
            return true
        }
        val doseUnit = binding.tvDoseUnit.text.toString()
        if(doseUnit.isEmpty){
            toastShort(R.string.please_select_dose_unit.string)
            return true
        }
        val timeValue = binding.tvTimeValue.text.toString()
        if(timeValue.isEmpty){
            toastShort(R.string.please_select_time_value.string)
            return true
        }
        val timeUnit = binding.tvTimeUnit.text.toString()
        if(timeUnit.isEmpty){
            toastShort(R.string.please_select_time_unit.string)
            return true
        }
        if(dose.isEmpty){
            toastShort(R.string.please_select_dose_frquency.string)
            return true
        }
        try {
            prescribedMedicines.add(
                PrescribedMedicine(
                medicine,
                doseValue.toFloat(),
                    doseUnit,
                    timeValue.toInt(),
                    timeUnit,
                    dose
            ))
        } catch (e: Exception) {
            toastShort(e.message?:"")
            return false
        }
        clearInputs()
        updateMedicineListRendering()
        return true
    }

    private fun clearInputs() {
        binding.ratvMedicines.setText("")
        dose = ""
        selected = ""
        showDoses()
    }

    private fun updateMedicineListRendering() {
        binding
            .alvMedicines
            .configure(
                AnyListView.Configurator(
                    state = AnyListView.STATE.DATA,
                    itemCount = {prescribedMedicines.size},
                    itemType = {
                        it
                    },
                    itemDecorations = {
                                      listOf(
                                          CustomItemDecorator().apply{
                                                  itemOffsetter = {pos,rect->
                                                      if(pos==0){
                                                          rect.top = 8.dpToPx
                                                      }
                                                      rect.bottom = 8.dpToPx
                                                  }
                                          }
                                      )
                    },
                    fromLayout = {false},
                    itemView = {pos->
                        try {
                            ComposeView(requireContext()).apply {
                                layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)

                                // The setContent call takes a Composable lambda extension which can render Composable UI.
                                setContent {
                                    var data = prescribedMedicines.getOrNull(pos)
                                    val bold400 = Font(R.font.roboto_bold, FontWeight.W400)
                                    val italic = Font(R.font.roboto_italic)
                                    Card(
                                        modifier = Modifier.padding(4.dp),
                                        shape = RoundedCornerShape(8.dp),
                                        elevation = 2.dp,
                                        border = BorderStroke(1.dp, androidx.compose.ui.graphics.Color(250,250,250))
                                    ) {
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
                                                ))
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
                                                    ))

                                                Text(data?.frequency?:"",style = TextStyle(
                                                    color = colorResource(id = R.color.orange)
                                                ))

                                                Text("${data?.timePeriodValue} ${data?.timePeriodUnit}",style = TextStyle(
                                                    fontFamily = FontFamily(bold400),
                                                    fontSize = 20.sp,
                                                ))
                                            }
                                        }
                                    }
                                }
                            }
                        } catch (e: Exception) {
                            null
                        }
                    },
                    onView = {pos,view->

                    }
            ))
    }

    private fun finalizeMedicinesAndBack() {
        if(binding.ratvMedicines.text.toString().isEmpty || finalizeMedicines()){
            Messanger.publish(myClassName, Message(Constants.MESSAGE_MEDICINES,prescribedMedicines))
            Messanger.publish(myClassName,Message(Constants.MESSAGE_FINISHED))
        }
    }

    private fun showDoses() {
        binding.alvDoses.configure(
            AnyListView.Configurator(
                state = AnyListView.STATE.DATA,
                itemCount = {doses.size},
                itemType = {
                    it
                },
                fromLayout = {false},
                layoutId = {R.layout.dose_layout},
                viewId = {R.id.bt_dose},
                onView = {pos,view->
                    (view as? TextView)?.text = doses.getOrNull(pos)
                    if(doses.getOrNull(pos)==selected){
                        (view as? TextView)?.apply {
                            background = R.drawable.dose_selected_background.drawable()
                            setTextColor(android.graphics.Color.WHITE)
                        }
                    }
                    else{
                        (view as? TextView)?.apply {
                            background = R.drawable.text_box_border.drawable()
                            setTextColor(android.graphics.Color.BLACK)
                        }
                    }
                    view?.setOnClickListener {
                        (view as? TextView)?.apply {
                            background = R.drawable.dose_selected_background.drawable()
                            setTextColor(android.graphics.Color.WHITE)
                        }
                        val prev = selected
                        selected = (view as? TextView)?.text.toString()
                        onDoseChanged((view as? TextView)?.text.toString())
                        if(prev.isNotEmpty){
                            binding.alvDoses.notifyItemChanged(doses.indexOf(prev))
                        }
                    }
                },
                layoutManager = {
                    LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
                },
                itemDecorations = {
                    listOf(
                        CustomItemDecorator().apply{
                                itemOffsetter = { pos, rect ->
                                    val spacing = 8.dpToPx
                                    if (pos < doses.size) {
                                        rect.right = spacing
                                    }
                                }
                        }
                    )
                }
            )
        )
    }

    private fun onDoseChanged(value: String) {
        dose = value
    }

    private fun onTimeUnitSelected(value: String) {
        binding.tvTimeUnit.text = value
    }

    private fun onTimeValueSelected(text: String) {
        binding.tvTimeValue.text = text
    }

    private fun onDoseValueSelected(text: String) {
        binding.tvDoseValue.text = text
    }

    override fun onDestroyView() {
        binding.ratvMedicines.removeTextChangedListener(ratvWatcher)
        Messanger.publish(myClassName,Message(Constants.MESSAGE_DESTROYED))
        Messanger.cancel(myClassName)
        super.onDestroyView()
    }
}