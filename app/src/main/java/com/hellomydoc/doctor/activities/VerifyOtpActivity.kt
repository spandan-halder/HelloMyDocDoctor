package com.hellomydoc.doctor.activities

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.hellomydoc.doctor.*
import com.hellomydoc.doctor.data.resp
import com.hellomydoc.doctor.databinding.ActivityVerifyOtpBinding
import kotlinx.coroutines.launch

class VerifyOtpActivity : AbstractActivity() {
    private var currentOtp = ""
    private var mobile = ""
    private lateinit var binding: ActivityVerifyOtpBinding
    private var keyboardTriggerBehavior: KeyboardTriggerBehavior? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityVerifyOtpBinding.inflate(layoutInflater)
        val root = binding.root
        setContentView(root)

        onCreatedActions()

        if(savedInstanceState==null){
            requestOTP()
        }

    }

    private fun requestOTP() {
        mobile = repository.mobile
        if(mobile.isNotEmpty){
            lifecycleScope.launch {
                wait = true
                try {
                    var response = repository.requestOTP(mobile).resp
                    ////////////////////////////
                    when{
                        response.isError->response.errorMessage.toastLong(this@VerifyOtpActivity)
                        else->{
                            when{
                                response.isSuccess->{
                                    val otpResponse = response.body
                                    when{
                                        otpResponse!=null->{
                                            if(otpResponse.success){
                                                currentOtp = otpResponse.otp
                                            }
                                        }
                                        else->{
                                            getString(R.string.something_went_wrong).toast(this@VerifyOtpActivity)
                                        }
                                    }
                                }
                                else->{
                                    getString(R.string.something_went_wrong).toast(this@VerifyOtpActivity)
                                }
                            }
                        }
                    }
                    ////////////////////////////
                } catch (e: Exception) {
                    e.message?.toast(this@VerifyOtpActivity)
                }
                wait = false
                binding.firstPinView.requestFocus()
            }
        }
    }

    private fun onCreatedActions() {
        setupBackButton()
        setupVerifyButton()
        trackKeyboardState()
    }

    private fun setupVerifyButton() {
        binding.sendOtpBtnVerify.setOnClickListener {
            Log.d("otp_bug","otp_md5=$currentOtp")
            val inputOtp = binding.firstPinView.text.toString()
            when{
                inputOtp.isEmpty->{
                    getString(R.string.please_put_the_otp).toast(this)
                }
                inputOtp.md5!=currentOtp->{
                    getString(R.string.otp_does_not_match).toast(this)
                }
                inputOtp.md5==currentOtp->{
                    repository.mobileVerified = true
                    gotoHome()
                }
            }
        }
    }

    private fun gotoHome() {
        navi()
            .target(HomeActivity::class.java)
            .go()
    }

    private fun trackKeyboardState() {
        keyboardTriggerBehavior = KeyboardTriggerBehavior(this,100).apply {
            observe(this@VerifyOtpActivity){
                when (it) {
                    KeyboardTriggerBehavior.Status.OPEN -> {
                        val lp = binding.filler.layoutParams
                        lp.height = 300.pxToDp
                        binding.filler.layoutParams = lp
                        binding.filler.requestLayout()
                        binding.scvRoot.fullScroll(View.FOCUS_DOWN)
                    }
                    KeyboardTriggerBehavior.Status.CLOSED -> {
                        val lp = binding.filler.layoutParams
                        lp.height = 0
                        binding.filler.layoutParams = lp
                        binding.filler.requestLayout()
                    }
                }
            }
        }
    }

    private fun setupBackButton() {
        binding.addOtpBackBtn.setOnClickListener{
            navi()
                .target(LoginActivity::class.java)
                .back()
        }
    }
}

