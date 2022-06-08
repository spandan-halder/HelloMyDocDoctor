package com.hellomydoc.doctor.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.hellomydoc.doctor.*
import com.hellomydoc.doctor.activities.HomeActivity
import com.hellomydoc.doctor.customViews.AppointmentView
import com.hellomydoc.doctor.data.AppointmentData
import com.hellomydoc.doctor.data.resp
import com.hellomydoc.doctor.databinding.FragmentVideoConsultationBinding
import kotlinx.coroutines.launch

class VideoConsultationFragment : Fragment() {
    private var childCallback: HomeActivity.ChildCallback? = null

    companion object {
        @JvmStatic
        fun newInstance(ChildCallback: HomeActivity.ChildCallback) = VideoConsultationFragment().apply {
            childCallback = ChildCallback
        }
    }
    private var appointments: List<AppointmentData> = listOf()
    private lateinit var binding: FragmentVideoConsultationBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentVideoConsultationBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUpcomingAppointments()
    }

    private fun setupUpcomingAppointments() {
        binding.alvAppointments.shimmerLoading(5)
        lifecycleScope.launch {
            try {
                val response = repository.getVideoUpcomingAppointments(repository.userUid).resp
                when{
                    response.isError->response.errorMessage.toastLong(requireContext())
                    else->{
                        when{
                            response.isSuccess->{
                                val appointmentsResponse = response.body
                                when{
                                    appointmentsResponse!=null->{
                                        if(appointmentsResponse.success){
                                            appointments = appointmentsResponse.appointments
                                            showAppointments()
                                        }
                                        else{
                                            appointmentsResponse.message.toast(requireContext())
                                        }
                                    }
                                    else->{
                                        getString(R.string.something_went_wrong).toast(requireContext())
                                    }
                                }
                            }
                            else->{
                                getString(R.string.something_went_wrong).toast(requireContext())
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                getString(R.string.something_went_wrong).toast(requireContext())
            }
        }
    }

    private fun showAppointments() {
        binding.alvAppointments.renderFromView(appointments,null){pos,appointment->
            AppointmentView(requireContext()).apply {
                layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT)
                data = appointment
                addPrescriptionCallback = {
                    childCallback?.goToPage?.invoke(PAGE.HOME, PAGE.ADD_PRESCRIPTION,Bundle().apply {
                        putString(Constants.APPOINTMENT_ID_KEY,it?.id?:"")
                    })
                }
            }
        }
    }
}