package com.hellomydoc.doctor.activities

import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.hellomydoc.doctor.Constants
import com.hellomydoc.doctor.R
import com.hellomydoc.doctor.repository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SplashActivity : AbstractActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            delayAndGo()
        }
    }

    private fun delayAndGo() {
        lifecycleScope.launch {
            delay(Constants.SPLASH_DURATION)
            if(repository.loginDone){
                if(repository.mobileVerified){
                    navi()
                        .target(HomeActivity::class.java)
                        .go()
                }
                else{
                    navi()
                        .target(VerifyOtpActivity::class.java)
                        .go()
                }
            }
            else{
                navi()
                    .target(LoginActivity::class.java)
                    .go()
            }
        }
    }
}