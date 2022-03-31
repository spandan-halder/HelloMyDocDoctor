package com.hellomydoc.doctor.activities

import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.hellomydoc.dialogs.RoundBottomSheet
import com.hellomydoc.doctor.*
import com.hellomydoc.doctor.data.resp
import com.hellomydoc.doctor.databinding.ActivityLoginBinding
import com.hellomydoc.doctor.databinding.ForgotPasswordLayoutBinding
import kotlinx.coroutines.launch

class LoginActivity : AbstractActivity() {
    var forgotPasswordDialogShown = false
    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val root = binding.root
        setContentView(root)
        onCreatedTasks()
    }

    private fun onCreatedTasks() {
        setupLoginClickAction()
        setupForgotPasswordClickAction()
    }

    private fun setupForgotPasswordClickAction() {
        binding.tvForgotPassword.setOnClickListener{
            showForgotPasswordDialog()
        }
    }



    private fun setupLoginClickAction() {
        binding.loginBtn.setOnClickListener {
            onLoginClick()
        }
    }

    private fun onLoginClick() {
        val userId = binding.etUserIdCore.text.toString()
        val password = binding.etPasswordCore.text.toString()

        when{
            userId.isEmpty->{
                warn(getString(R.string.please_put_a_eamil_or_mobile))
                return
            }
            userId.isNotMobile&&userId.isNotEmail->{
                warn(getString(R.string.please_put_a_eamil_or_mobile))
                return
            }
            password.isEmpty->{
                warn(getString(R.string.please_enter_password))
                return
            }
            password.isNotPassword->{
                warn(getString(R.string.wrong_password_pattern))
                return
            }
        }
        doLogin(userId,password)
    }

    private fun warn(s: String) {
        Toast.makeText(this, s, Toast.LENGTH_LONG).show()
    }

    private fun doLogin(userId: String, pass: String) {
        lifecycleScope.launch {
            wait = true
            try {
                val response = repository.getDoctor(userId,pass).resp
                when{
                    response.isError->response.errorMessage.toastLong(this@LoginActivity)
                    else->{
                        when{
                            response.isSuccess->{
                                val userResponse = response.body
                                when{
                                    userResponse!=null->{
                                        if(userResponse.success){
                                            if(userResponse.doctor.mobile.isNotEmpty
                                                &&userResponse.doctor.mobile.isMobile
                                            ){
                                                val uid = userResponse.doctor.user_id
                                                repository.userUid = uid
                                                repository.loginDone = true
                                                repository.mobile = userResponse.doctor.mobile
                                                goToVerifyMobile()
                                                wait = false
                                                return@launch
                                            }
                                        }
                                        else{
                                            userResponse.message.toast(this@LoginActivity)
                                        }
                                    }
                                    else->{
                                        getString(R.string.something_went_wrong).toast(this@LoginActivity)
                                    }
                                }
                            }
                            else->{
                                getString(R.string.something_went_wrong).toast(this@LoginActivity)
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                getString(R.string.something_went_wrong).toast(this@LoginActivity)
            }
            wait = false
        }
    }

    private fun goToVerifyMobile() {
        navi()
            .target(VerifyOtpActivity::class.java)
            .go()
    }

    private fun gotoHome() {

    }

    override fun onBackPressed() {
    }


    private fun showForgotPasswordDialog() {
        if(forgotPasswordDialogShown){
            return
        }
        forgotPasswordDialogShown = true
        val bottomSheetDialog = RoundBottomSheet(this){
            forgotPasswordDialogShown = false
        }

        val binding = ForgotPasswordLayoutBinding.inflate(layoutInflater)
        val root = binding.root
        bottomSheetDialog.setContentView(root)

        bottomSheetDialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED

        binding.btSendOtp.setOnClickListener {
            val mobile = binding.etUserIdForgot.text?.string?:""
            if(mobile.isEmpty){
                R.string.please_put_a_mobile_number.string.toast(this)
                return@setOnClickListener
            }
            if(mobile.isNotMobile){
                R.string.please_put_a_valid_mobile_number.string.toast(this)
                return@setOnClickListener
            }

        }

        bottomSheetDialog.show()
    }
}




