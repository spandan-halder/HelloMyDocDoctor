package com.hellomydoc.doctor.activities

import android.app.Application
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView
import com.google.android.material.navigation.NavigationView
import com.hellomydoc.chat.presentation.activity.ChatActivity
import com.hellomydoc.doctor.*
import com.hellomydoc.doctor.data.Profile
import com.hellomydoc.doctor.data.resp
import com.hellomydoc.doctor.dialogs.CustomDialog
import com.hellomydoc.doctor.fragments.ChatConsultationFragment
import com.hellomydoc.doctor.fragments.HomeFragment
import com.hellomydoc.doctor.fragments.VideoConsultationFragment
import com.hellomydoc.doctor.fragments.VoiceConsultationFragment
import com.hellomydoc.videocall.VideoBox
import com.hellomydoc.videocall.models.AllowedResponse
import com.hellomydoc.videocall.models.Ids
import kotlinx.coroutines.launch

class HomeActivity : AbstractActivity() {
    data class ChildCallback(
        val goToPage: (PAGE,PAGE,Bundle?)->Unit
    )

    private var profile: Profile? = null
    private val childCallback = ChildCallback(
        goToPage = {source,target,bundle->
            goToPage(target,bundle)
        }
    )
    private var bottom_navigation: BottomNavigationView? = null
    private var navigation_view: NavigationView? = null
    private var iv_menu: ImageView? = null
    private var dl_side_drawer: DrawerLayout? = null
    private var drawerOnSelectedListener = NavigationView.OnNavigationItemSelectedListener {
        if(dl_side_drawer?.isOpen==true){
            dl_side_drawer?.closeDrawer(GravityCompat.START)
        }
        return@OnNavigationItemSelectedListener onDrawerSelected(it.itemId)
    }
    private var bottomNavigationOnSelectedListener = NavigationBarView.OnItemSelectedListener {
        return@OnItemSelectedListener onDrawerSelected(it.itemId)
    }

    private fun onDrawerSelected(id: Int): Boolean {
        try {
            releaseNavigationMenuListener()
            navigation_view?.setCheckedItem(id)
            bottom_navigation?.selectedItemId = id
            return true
        } finally {
            applyMenuSelected(id)
            setupNavigationMenuListener()
        }
    }

    private fun applyMenuSelected(id: Int) {
        when(id){
            R.id.menu_video_consultation->goToPage(PAGE.VIDEO_CONSULTATIONS, null)
            R.id.menu_home->goToPage(PAGE.HOME, null)
            R.id.menu_voice_consultation->goToPage(PAGE.VOICE_CONSULTATION, null)
            R.id.menu_chat_consultation->goToPage(PAGE.CHAT_CONSULTATIONS, null)
            R.id.menu_prescriptions->goToPage(PAGE.PRESCRIPTIONS, null)
            R.id.menu_appointment_history->goToPage(PAGE.APPOINTMENT_HISTORY, null)
            R.id.menu_logout->askLogout()
        }
    }

    private fun askLogout() {
        CustomDialog
            .create(this)
            .layout(R.layout.confirm_logout_dialog_layout)
            .noBackground()
            .with {
                findViewById<View>(R.id.bt_yes).setOnClickListener {
                    dismiss()
                    logout()
                }
                findViewById<View>(R.id.bt_no).setOnClickListener {
                    dismiss()
                }
            }
            .show()
    }

    private fun logout() {
        Prefs.clear()
        navi().target(LoginActivity::class.java).back()
    }

    private fun goToPage(page: PAGE, bundle: Bundle?=null) {
        when(page.pageType){
            PAGE_TYPE.FRAGMENT -> {
                val fm = supportFragmentManager
                fm.commit {
                    replace(R.id.fragmentContainerView, getFragmentByPage(page)!!)
                }
            }
            PAGE_TYPE.ACTIVITY -> {
                if(page==PAGE.VIDEO_CALLING){
                    gotoVideoCall(bundle)
                }
                else{
                    navi().target(getActivityClass(page)).finish(false)?.bundle(bundle)?.go()
                }
            }
        }
    }

    private fun gotoVideoCall(bundle: Bundle?) {
        val appointmentId = bundle?.getString(Constants.APPOINTMENT_ID_KEY)?:return
        val userId = bundle.getString(Constants.USER_UID)?:return
        val peerId = bundle.getString(Constants.PEER_ID_KEY)?:return
        val peerName = bundle.getString(Constants.PEER_NAME_KEY)?:return
        val userName = bundle.getString(Constants.USER_NAME)?:return
        VideoBox.callback = object: VideoBox.Callback{
            override val ids: Ids
                get() = Ids(userId,peerId,appointmentId,userName,peerName)
            override val appContext: Application
                get() = this@HomeActivity.application

            override fun onApproving() {

            }

            override suspend fun checkAllowed(
                channelId: String,
                userId: String
            ): AllowedResponse? {
                val startTime = System.currentTimeMillis()//DateTime(System.currentTimeMillis(), DateTimeZone.UTC).toDateTime(DateTimeZone.getDefault()).millis
                return AllowedResponse(true,"allowed",startTime,15*60*1000)
            }

        }
        VideoBox.start(this,appointmentId,userId)
    }

    private fun getActivityClass(page: PAGE): Class<*>? {
        return when(page){
            PAGE.CHAT -> ChatActivity::class.java
            //PAGE.VIDEO_CALLING -> com.hellomydoc.videocalling.VideoCallingActivity::class.java
            PAGE.HOME_ROOT -> HomeActivity::class.java
            PAGE.ADD_PRESCRIPTION -> AddPrescriptionActivity::class.java
            PAGE.PRESCRIPTIONS -> PrescriptionsActivity::class.java
            PAGE.APPOINTMENT_HISTORY -> AppointmentHistoryActivity::class.java
            else->null
        }
    }

    private fun getFragmentByPage(page: PAGE): Fragment? {
        return when(page){
            PAGE.HOME -> HomeFragment.newInstance(childCallback)
            PAGE.VIDEO_CONSULTATIONS -> VideoConsultationFragment.newInstance(childCallback)
            PAGE.VOICE_CONSULTATION -> VoiceConsultationFragment.newInstance(childCallback)
            PAGE.CHAT_CONSULTATIONS -> ChatConsultationFragment.newInstance(childCallback)
            else-> null
        }
    }

    private var tv_doctor_name: TextView? = null
    private var iv_profile_image_on_menu: ImageView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        fetchProfile()
        iv_menu = findViewById(R.id.iv_menu)
        dl_side_drawer = findViewById(R.id.dl_side_drawer)
        iv_menu?.setOnClickListener {
            if(dl_side_drawer?.isOpen!=true){
                dl_side_drawer?.open()
            }
        }

        navigation_view = findViewById(R.id.nav_menu)
        bottom_navigation = findViewById(R.id.bottom_navigation)


        val header = navigation_view?.getHeaderView(0)
        tv_doctor_name = header?.findViewById(R.id.tv_doctor_name)
        iv_profile_image_on_menu = header?.findViewById(R.id.iv_profile_image_on_menu)

        setupNavigationMenuListener()

    }

    override fun onResume() {
        super.onResume()
        navigation_view?.setCheckedItem(R.id.menu_home)
        goToPage(PAGE.HOME)
    }

    private fun fetchProfile() {
        lifecycleScope.launch {
            try {
                val response = repository.getProfile(repository.userUid).resp
                when{
                    response.isError->response.errorMessage.toastLong(this@HomeActivity)
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
                                            profileResponse.message.toast(this@HomeActivity)
                                        }
                                    }
                                    else->{
                                        getString(R.string.something_went_wrong).toast(this@HomeActivity)
                                    }
                                }
                            }
                            else->{
                                getString(R.string.something_went_wrong).toast(this@HomeActivity)
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                getString(R.string.something_went_wrong).toast(this@HomeActivity)
            }
        }
    }

    private fun renderProfile() {
        tv_doctor_name?.text = profile?.name
        iv_profile_image_on_menu?.setImage(profile?.image)
    }

    private fun releaseNavigationMenuListener() {
        navigation_view?.setNavigationItemSelectedListener(null)
        bottom_navigation?.setOnItemSelectedListener(null)
    }

    private fun setupNavigationMenuListener() {
        navigation_view?.setNavigationItemSelectedListener(drawerOnSelectedListener)
        bottom_navigation?.setOnItemSelectedListener(bottomNavigationOnSelectedListener)
    }

    override fun onBackPressed() {
        if (dl_side_drawer?.isDrawerOpen(GravityCompat.START)==true) {
            dl_side_drawer?.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
}