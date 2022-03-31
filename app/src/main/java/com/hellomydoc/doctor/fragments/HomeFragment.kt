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
import com.hellomydoc.doctor.data.Profile
import com.hellomydoc.doctor.data.resp
import com.hellomydoc.doctor.databinding.FragmentHomeBinding
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {
    private var childCallback: HomeActivity.ChildCallback? = null

    companion object {
        @JvmStatic
        fun newInstance(ChildCallback: HomeActivity.ChildCallback) = HomeFragment().apply {
            childCallback = ChildCallback
        }
    }
    private var profile: Profile? = null
    private var appointments: List<AppointmentData> = listOf()
    private lateinit var binding: FragmentHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fetchProfile()
        setupUpcomingAppointments()
    }

    private fun fetchProfile() {
        lifecycleScope.launch {
            try {
                val response = repository.getProfile(repository.userUid).resp
                when{
                    response.isError->response.errorMessage.toastLong(requireContext())
                    else->{
                        when{
                            response.isSuccess->{
                                val profileResponse = response.body
                                when{
                                    profileResponse!=null->{
                                        if(profileResponse.success){
                                            profile = profileResponse.profile
                                            renderProfile()
                                        }
                                        else{
                                            profileResponse.message.toast(requireContext())
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

    private fun renderProfile() {
        binding.tvDoctorName.text = profile?.name
        binding.ivProfileImage.setImage(profile?.image)
        publish(Message(Constants.MESSAGE_PROFILE,profile))
    }

    private fun setupUpcomingAppointments() {
        binding.alvUpcomingAppointments.shimmerLoading(5)
        lifecycleScope.launch {
            try {
                val response = repository.getFewUpcomingAppointments(repository.userUid).resp
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
        binding.alvUpcomingAppointments.renderFromView(appointments, { pos,view->
            (view as? AppointmentView)?.apply {
                addPrescriptionCallback = {
                    childCallback?.goToPage?.invoke(PAGE.HOME,PAGE.ADD_PRESCRIPTION,Bundle().apply {
                        putString(Constants.APPOINTMENT_ID_KEY,it?.id?:"")
                    })
                }
                callCallback = {
                    when(it?.type?.uppercase()){
                        "VIDEO"->goToVideoCall(it)
                        "CHAT"->goToChat(it.patient.id)
                    }
                }
            }
        }){pos,appointment->
            AppointmentView(requireContext()).apply {
                layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT)
                data = appointment
            }
        }
    }

    private fun goToChat(patientId: String) {
        childCallback?.goToPage?.invoke(PAGE.HOME,PAGE.CHAT,Bundle().apply {
            putString(Constants.PEER_ID_KEY,"client_$patientId")
            putString(Constants.MY_ID,(requireActivity().application as App).chatUserId)
        })
    }

    private fun goToVideoCall(ad: AppointmentData) {
        childCallback?.goToPage?.invoke(PAGE.HOME,PAGE.VIDEO_CALLING,Bundle().apply {
            putString(Constants.APPOINTMENT_ID_KEY,ad.id)
            putString(Constants.USER_UID, repository.userUid)
            putString(Constants.PEER_ID_KEY, ad.patient.id)
            putString(Constants.PEER_NAME_KEY, ad.patient.name)
            putString(Constants.USER_NAME, "DOCTOR")
        })
    }
}