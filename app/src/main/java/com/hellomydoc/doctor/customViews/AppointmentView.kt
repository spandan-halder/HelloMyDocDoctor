package com.hellomydoc.doctor.customViews

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.hellomydoc.doctor.R
import com.hellomydoc.doctor.data.AppointmentData
import com.hellomydoc.doctor.databinding.AppointmentViewLayoutBinding
import com.hellomydoc.doctor.setImage

class AppointmentView : LinearLayout {
    private lateinit var binding: AppointmentViewLayoutBinding

    //declare views here
    @JvmOverloads
    constructor(context: Context?, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes) {
        init()
    }

    private fun init() {
        /*inflate(context, R.layout.appointment_view_layout, this)*/
        binding = AppointmentViewLayoutBinding.inflate(LayoutInflater.from(context))
        binding.root.layoutParams = LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT)
        addView(binding.root)

        setupViews()
    }

    public override fun onSaveInstanceState(): Parcelable? {
        val bundle = Bundle()
        bundle.putParcelable("superState", super.onSaveInstanceState())
        //bundle.putInt("stuff", this.stuff); // ... save stuff
        return bundle
    }

    public override fun onRestoreInstanceState(state: Parcelable) {
        var state: Parcelable? = state
        if (state is Bundle) // implicit null check
        {
            //this.stuff = bundle.getInt("stuff"); // ... load stuff
            state = state.getParcelable("superState")
        }
        super.onRestoreInstanceState(state)
    }

    var addPrescriptionCallback: (AppointmentData?)->Unit = {}
    var callCallback: (AppointmentData?)->Unit = {}
    private fun setupViews() {
        data = null
        binding.tvAddPrescription.setOnClickListener {
            addPrescriptionCallback(_data)
        }
        binding.llvType.setOnClickListener {
            callCallback(_data)
        }
    }

    private var _data: AppointmentData? = null

    var data: AppointmentData?
        get(){
            return _data
        }
        set(value){
            _data = value
            onDataChanged()
        }

    private fun onDataChanged() {
        binding.tvName.text = _data?.patient?.name
        binding.tvGenderAge.text = "(${_data?.patient?.gender?.toShortGender}-${_data?.patient?.age})"
        binding.tvSymptoms.text = _data?.symptoms
        binding.tvDate.text = _data?.date
        binding.tvTime.text = _data?.time
        binding.tvType.text = _data?.type?.callingType
        binding.ivType.setImageResource(mapTypeImage(_data?.type))
        binding.ivProfileImage.setImage(_data?.patient?.icon, R.drawable.ic_baseline_person_24)
    }

    private fun mapTypeImage(type: String?): Int {
        return when(type?.uppercase()){
            "VIDEO"->R.drawable.ic_baseline_videocam_24
            "VOICE"->R.drawable.ic_baseline_phone_24
            else -> R.drawable.ic_baseline_chat_24
        }
    }
}

val String.toShortGender: String
        get(){
            return when(this.uppercase()){
                "MALE"->"M"
                "FEMALE"->"F"
                else -> "O"
            }
        }

val String.callingType: String
        get(){
            return when(this.uppercase()){
                "VIDEO"->"Video"
                "VOICE"->"Voice"
                else -> "Chat"
            }
        }