package com.hellomydoc.doctor.activities

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import com.hellomydoc.doctor.Navi
import com.hellomydoc.doctor.R
import com.hellomydoc.doctor.Tyfo.overrideFont
import com.hellomydoc.doctor.screenHeight
import com.hellomydoc.doctor.screenWidth


open class AbstractActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setTypeFace()
    }

    fun navi(): Navi {
        return Navi(this)
    }

    val activityResultCallbacks =
        ArrayList<((requestCode: Int, resultCode: Int, data: Intent?) -> Unit)?>()

    fun addActivityResultCallback(callback: ((requestCode: Int, resultCode: Int, data: Intent?) -> Unit)?) {
        activityResultCallbacks.add(callback)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        for (i in activityResultCallbacks.indices) {
            var callback = activityResultCallbacks.removeAt(i)
            if (callback != null) {
                callback(requestCode, resultCode, data)
            }
        }

    }

    private var _wait = false
    var wait: Boolean
        get() = _wait
        set(value){
            _wait = value
            if(value){
                showWaiting()
            }
            else{
                hideWaiting()
            }
        }

    private var loaderDialog: Dialog? = null
    private fun hideWaiting() {
        loaderDialog?.dismiss()
    }

    private fun showWaiting() {
        loaderDialog = Dialog(this)
        /////////////////////////////
        loaderDialog?.apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setCancelable(false)
            setContentView(R.layout.loader_dialog_layout)
            window?.apply {
                setBackgroundDrawableResource(android.R.color.transparent)
                val wlp = attributes
                wlp.flags = wlp.flags and WindowManager.LayoutParams.FLAG_DIM_BEHIND.inv()
                attributes = wlp
                setLayout(screenWidth, screenHeight)
            }
        }
        /////////////////////////////
        loaderDialog?.show()
    }

    protected fun openKeyboard(view: View){
        view.requestFocus()
        view.postDelayed({
            val keyboard = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            keyboard.showSoftInput(view, 0)
        },200)
    }

    override fun onDestroy() {
        loaderDialog?.dismiss()
        loaderDialog = null
        super.onDestroy()
    }

    private fun setTypeFace() {
        val font = "fonts/Roboto-Regular.ttf"
        overrideFont(this, "DEFAULT", font)
        overrideFont(this, "MONOSPACE", font)
        overrideFont(this, "SERIF", font)
        overrideFont(this, "SANS_SERIF", font)
    }
}