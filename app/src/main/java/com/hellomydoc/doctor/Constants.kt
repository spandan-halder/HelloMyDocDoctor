package com.hellomydoc.doctor

object Constants {
    const val MY_ID = "MY_ID"
    const val USER_NAME = "USER_NAME"
    const val PEER_NAME_KEY = "PEER_NAME_KEY"
    const val PEER_ID_KEY = "PEER_ID_KEY"
    const val PATIENT_ID = "PATIENT_ID"
    const val MODE_EDITING = "MODE_EDITING"
    const val SPLASH_DURATION = 2000L
    const val LOGIN_DONE = "LOGIN_DONE"
    const val BASE_URL = "base_url"
    const val USER_UID = "USER_UID"
    const val MOBILE_KEY = "MOBILE_KEY"
    const val APPOINTMENT_ID_KEY = "APPOINTMENT_ID_KEY"
    const val MOBILE_VERIFIED = "MOBILE_VERIFIED"

    const val FCM_SYNCED = "fcm_synced"
    const val FCM_TOKEN = "fcm_token"

    val MAX_CONTENT_LENGHT: Long = 250000L

    const val MESSAGE_PROFILE = "profile"
    const val MESSAGE_LAB_TESTS = "lab_tests"
    const val MESSAGE_CREATED = "created"
    const val MESSAGE_MEDICINES = "medicines"
    const val MESSAGE_DESTROY_OK = "destroy_ok"
    const val MESSAGE_DESTROYED = "destroyed"
    const val MESSAGE_PLEASE_DESTROY = "please_destroy"
    const val MESSAGE_FINISHED = "finished"

    const val MOBILE_REGEX = "^\\d{10}\$"
    const val EMAIL_REGEX = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}\$"
    const val PERSON_REGEX = "^[a-zA-Z]+(([',. -][a-zA-Z ])?[a-zA-Z]*)*\$"
    const val PASSWORD_REGEX = "(?=^.{8,}\$)((?=.*\\d)|(?=.*\\W+))(?![.\\n])(?=.*[A-Z])(?=.*[a-z]).*\$"

    /////
    const val CHAT_PROJECT_ID_KEY = "chat_project_id"
    const val CHAT_APPLICATION_ID_KEY = "chat_application_id"
    const val CHAT_API_KEY_KEY = "chat_api_key"
    const val CHAT_FIREBASE_APP_NAME = "chat_firebase_app_name"
    const val CHAT_DATABASE_URL = "chat_database_url"
}