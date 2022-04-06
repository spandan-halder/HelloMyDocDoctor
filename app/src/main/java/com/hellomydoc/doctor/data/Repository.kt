package com.hellomydoc.doctor.data

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.google.gson.GsonBuilder
import com.hellomydoc.doctor.App
import com.hellomydoc.doctor.Constants
import com.hellomydoc.doctor.Metar
import com.hellomydoc.doctor.Prefs
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST


interface Apis {
    @FormUrlEncoded
    @POST("doctors/byIdAndPass")
    suspend fun getDoctor(@Field("user_id") user_id: String, @Field("pass") pass: String): Response<DoctorResponse>

    @FormUrlEncoded
    @POST("sendOtpForNumberValidation")
    suspend fun sendOtpForNumberValidation(@Field("mobile") mobile: String): Response<OTPResponse>

    @FormUrlEncoded
    @POST("doctorAppointments/fewUpcoming")
    suspend fun getFewUpcomingAppointments(@Field("user_id") userId: String): Response<AppointmentsResponse>

    @FormUrlEncoded
    @POST("doctorAppointments/details")
    suspend fun getAppointmentDetails(@Field("appointment_id") appointmentId: String): Response<AppointmentResponse>

    @POST("submitPrescription")
    suspend fun submitPrescription(@Body prescription: Prescription): Response<PrescriptionSubmittedResponse>

    @FormUrlEncoded
    @POST("doctorAppointments/videoUpcoming")
    suspend fun getVideoUpcomingAppointments(@Field("user_id") userId: String): Response<AppointmentsResponse>

    @FormUrlEncoded
    @POST("prescriptions")
    suspend fun getPrescriptions(@Field("user_id") userId: String): Response<PrescriptionsResponse>

    @FormUrlEncoded
    @POST("doctorAppointments/history")
    suspend fun getAppointmentsHistory(@Field("user_id") userId: String): Response<AppointmentHistoryResponse>

    @FormUrlEncoded
    @POST("videoCallAllowed")
    suspend fun checkVideoCallAllowed(
        @Field("userId") userId: String,
        @Field("userType") userType: String,
        @Field("appointmentId") appointmentId: String,
    ): Response<VideoCallAllowedResponse>

    @FormUrlEncoded
    @POST("doctorAppointments/voiceUpcoming")
    suspend fun getVoiceUpcomingAppointments(@Field("user_id") userId: String): Response<AppointmentsResponse>

    @FormUrlEncoded
    @POST("doctorAppointments/chatUpcoming")
    suspend fun getChatUpcomingAppointments(@Field("user_id") userId: String): Response<AppointmentsResponse>

    @FormUrlEncoded
    @POST("doctors/profile")
    suspend fun getProfile(@Field("user_id") userId: String): Response<ProfileResponse>

    @POST("doseUnits")
    suspend fun getDoseUnits(): Response<DoseUnitsResponse>
    @POST("timeUnits")
    suspend fun getTimeUnits(): Response<TimeUnitsResponse>
    @POST("doses")
    suspend fun getDoses(): Response<DosesResponse>
}

private fun httpClient(): OkHttpClient {
    return OkHttpClient.Builder()
        .addInterceptor(
            ChuckerInterceptor.Builder(App.instance as Context)
                .collector(ChuckerCollector(App.instance as Context))
                .maxContentLength(Constants.MAX_CONTENT_LENGHT)
                .redactHeaders(emptySet())
                .alwaysReadResponseBody(false)
                .build()
        )
        .build()
}

object ApiService{
    fun get(): Apis {
        val gson = GsonBuilder()
            .create()
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(Metar[Constants.BASE_URL])
            .client(httpClient())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
        val apiService = retrofit.create(Apis::class.java)
        return apiService
    }
}

class ApiRepository {
    suspend fun getDoctor(user_id: String, pass: String) = ApiService.get().getDoctor(user_id,pass)
    suspend fun sendOtpForNumberValidation(mobile: String) = ApiService.get().sendOtpForNumberValidation(mobile)
    suspend fun getFewUpcomingAppointments(userId: String) = ApiService.get().getFewUpcomingAppointments(userId)
    suspend fun getProfile(userId: String) = ApiService.get().getProfile(userId)
    suspend fun getVideoUpcomingAppointments(userId: String) = ApiService.get().getVideoUpcomingAppointments(userId)
    suspend fun getVoiceUpcomingAppointments(userId: String) = ApiService.get().getVoiceUpcomingAppointments(userId)
    suspend fun getChatUpcomingAppointments(userId: String) = ApiService.get().getChatUpcomingAppointments(userId)
    suspend fun getDoseUnits() = ApiService.get().getDoseUnits()
    suspend fun getTimeUnits() = ApiService.get().getTimeUnits()
    suspend fun getDoses() = ApiService.get().getDoses()
    suspend fun getAppointmentDetails(appointmentId: String) = ApiService.get().getAppointmentDetails(appointmentId)
    suspend fun submitPrescription(prescription: Prescription) = ApiService.get().submitPrescription(prescription)
    suspend fun getPrescriptions(userId: String) = ApiService.get().getPrescriptions(userId)
    suspend fun getAppointmentsHistory(userId: String) = ApiService.get().getAppointmentsHistory(userId)
    suspend fun checkVideoCallAllowed(userId: String,userType: String,appointmentId: String) =
        ApiService.get().checkVideoCallAllowed(userId,userType,appointmentId)
}


class Repository {
    var loginDone: Boolean
        get() = Prefs.getBoolean(Constants.LOGIN_DONE)
        set(value) {
            Prefs.putBoolean(Constants.LOGIN_DONE,value)
        }

    suspend fun getDoctor(userId: String, pass: String): Response<DoctorResponse> {
        return ApiRepository().getDoctor(userId,pass)
    }

    var userUid: String
        get() = Prefs.getString(Constants.USER_UID)?:""
        set(value) {
            Prefs.putString(Constants.USER_UID,value)
            if(value.isNotEmpty()&&value.matches("^[{]?[0-9a-fA-F]{8}-([0-9a-fA-F]{4}-){3}[0-9a-fA-F]{12}[}]?$".toRegex())){
                App.instance.syncFcmToken()
            }
        }

    suspend fun requestOTP(mobile: String): Response<OTPResponse> {
        return ApiRepository().sendOtpForNumberValidation(mobile)
    }

    suspend fun getFewUpcomingAppointments(userUid: String): Response<AppointmentsResponse> {
        return ApiRepository().getFewUpcomingAppointments(userUid)
    }

    suspend fun getProfile(userUid: String): Response<ProfileResponse> {
        return ApiRepository().getProfile(userUid)
    }

    suspend fun getVideoUpcomingAppointments(userUid: String): Response<AppointmentsResponse> {
        return ApiRepository().getVideoUpcomingAppointments(userUid)
    }

    suspend fun getVoiceUpcomingAppointments(userUid: String): Response<AppointmentsResponse> {
        return ApiRepository().getVoiceUpcomingAppointments(userUid)
    }

    suspend fun getChatUpcomingAppointments(userUid: String): Response<AppointmentsResponse> {
        return ApiRepository().getChatUpcomingAppointments(userUid)
    }

    suspend fun getDoseUnits(): Response<DoseUnitsResponse> {
        return ApiRepository().getDoseUnits()
    }

    suspend fun getTimeUnits(): Response<TimeUnitsResponse> {
        return ApiRepository().getTimeUnits()
    }

    suspend fun getDoses(): Response<DosesResponse> {
        return ApiRepository().getDoses()
    }

    suspend fun getAppointmentDetails(appointmentId: String): Response<AppointmentResponse> {
        return ApiRepository().getAppointmentDetails(appointmentId)
    }

    suspend fun submitPrescription(prescription: Prescription): Response<PrescriptionSubmittedResponse> {
        return ApiRepository().submitPrescription(prescription)
    }

    suspend fun getPrescriptions(userUid: String): Response<PrescriptionsResponse> {
        return ApiRepository().getPrescriptions(userUid)
    }

    suspend fun getAppointmentsHistory(userUid: String): Response<AppointmentHistoryResponse> {
        return ApiRepository().getAppointmentsHistory(userUid)
    }

    suspend fun checkVideoCallAllowed(appointmentId: String): Response<VideoCallAllowedResponse> {
        return ApiRepository().checkVideoCallAllowed(userUid,"DOCTOR",appointmentId)
    }

    var mobileVerified: Boolean
        get() = Prefs.getBoolean(Constants.MOBILE_VERIFIED)
        set(value) {
            Prefs.putBoolean(Constants.MOBILE_VERIFIED,value)
        }

    var mobile: String
        get() = Prefs.getString(Constants.MOBILE_KEY)?:""
        set(value) {
            Prefs.putString(Constants.MOBILE_KEY,value)
        }
}