package com.hellomydoc.doctor

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.Context
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.text.Editable
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.forEachGesture
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.input.pointer.pointerInput
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory
import com.hellomydoc.doctor.data.Repository
import com.hellomydoc.doctor.data.Resp
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.math.BigInteger
import java.security.MessageDigest


val String.md5: String
    get(){
        val md = MessageDigest.getInstance("MD5")
        return BigInteger(1, md.digest(this.toByteArray())).toString(16).padStart(32, '0')
    }

fun View.hide() {
    visibility = View.GONE
}
fun View.show() {
    visibility = View.VISIBLE
}

fun String.log(tag: String){
    Log.d(tag,this)
}

fun String.toast(context: Context){
    Toast.makeText(context,this,Toast.LENGTH_SHORT).show()
}

fun String.toastLong(context: Context){
    Toast.makeText(context,this,Toast.LENGTH_LONG).show()
}

val screenWidth:Int
    get() = Resources.getSystem().displayMetrics.widthPixels

val screenHeight:Int
    get() = Resources.getSystem().displayMetrics.heightPixels

val repository: Repository
    get() = Repository()

val Int.pxToDp: Int
    get() = (this / Resources.getSystem().displayMetrics.density).toInt()
val Int.dpToPx: Int
    get() = (this * Resources.getSystem().displayMetrics.density).toInt()


/**extensions for globally accessible resources by id**/
val Int.string: String
    get() = App.instance.stringResource(this)

val Editable.string: String
    get() = this.toString()

fun Int.drawable(): Drawable?
{
    return App.instance.drawableResource(this)
}

fun Int.dimension(): Float
{
    return try {
        App.instance.dimensionResource(this)!!
    } catch (e: Exception) {
        0f
    }
}

val Int.color: Int
    get() = App.instance.colorResource(this)

enum class ApiDispositionStatus{
    EXCEPTION,
    ERROR,
    FAILED,
    BODY_NULL,
    UNKNOWN,
    RESPONSE
}

data class ApiDisposition<T>(
    val status: ApiDispositionStatus,
    val exception: java.lang.Exception? = null,
    val error: String? = null,
    val failed: Any? = null,
    val response: T? = null,
)

suspend inline fun <reified T> processApi(api: suspend ()-> Resp<T>): ApiDisposition<T> {
    try {
        val response = api()
        when{
            response.isError->return ApiDisposition(
                ApiDispositionStatus.ERROR,
                error = response.errorMessage
            )
            else->{
                when{
                    response.isSuccess->{
                        val registrationResponse = response.body
                        when{
                            registrationResponse!=null->{
                                return ApiDisposition(
                                    ApiDispositionStatus.RESPONSE,
                                    response = registrationResponse
                                )
                            }
                            else->{
                                return ApiDisposition(
                                    ApiDispositionStatus.BODY_NULL
                                )
                            }
                        }
                    }
                    else->{
                        return ApiDisposition(
                            ApiDispositionStatus.FAILED
                        )
                    }
                }
            }
        }
    } catch (e: Exception) {
        return ApiDisposition(
            ApiDispositionStatus.EXCEPTION,
            exception = e
        )
    }
}

/**
 * Consumer Key: UX5FVQgfJeCmbp7mAchqDCjT4Weuerfi
 * Consumer Secret: C4iIOFAQ07QvtG5g
 */

fun ImageView.setImage(url: String?,@DrawableRes default: Int = 0) {
    if(url==null || url.isEmpty){
        return
    }
    val factory = DrawableCrossFadeFactory.Builder().setCrossFadeEnabled(true).build()
    Glide
        .with(App.instance)
        .load(url)
        .apply {
            if(default!=0){
                placeholder(default.drawable())
            }
        }
        //.transition(withCrossFade(factory))
        .into(this)
}

fun ImageView.setImage(url: String?) {
    if(url==null){
        return
    }
    val factory = DrawableCrossFadeFactory.Builder().setCrossFadeEnabled(true).build()
    Glide
        .with(App.instance)
        .load(url)
        .listener(object: RequestListener<Drawable>{
            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: Target<Drawable>?,
                isFirstResource: Boolean
            ): Boolean {
                Log.d("glide_debug",e?.message?:"")
                return false
            }

            override fun onResourceReady(
                resource: Drawable?,
                model: Any?,
                target: Target<Drawable>?,
                dataSource: com.bumptech.glide.load.DataSource?,
                isFirstResource: Boolean
            ): Boolean {
                Log.d("glide_debug","ready")
                return false
            }

        })
        //.transition(withCrossFade(factory))
        .into(this)
}

fun View.heightAnim(to: Float,duration:Long,listener: ()->Unit){
    val va = ValueAnimator.ofFloat(0f, 1f)
    va.duration = duration
    val mFromHeight = height
    val mToHeight = to
    va.addUpdateListener { animation ->
        val newHeight: Int
        newHeight = (mFromHeight + (mToHeight - mFromHeight) * (animation.animatedValue as Float)).toInt()
        Log.d("height_animation","height=$newHeight")
        layoutParams.height = newHeight
        requestLayout()
    }
    va.addListener(object: Animator.AnimatorListener{
        override fun onAnimationStart(p0: Animator?) {

        }

        override fun onAnimationEnd(p0: Animator?) {
            listener()
        }

        override fun onAnimationCancel(p0: Animator?) {

        }

        override fun onAnimationRepeat(p0: Animator?) {

        }

    })
    va.start()
}

fun Float.map(sa:Float,sb:Float,ta:Float,tb:Float): Float{
    val sd = sb - sa
    val rd = this - sa
    var p = rd/sd

    val td = tb - ta
    val tv = td*p
    var t = ta + tv
    return t
}

val Any.myClassName: String
    get(){
        return this::class.java.simpleName
    }

fun ViewGroup.findViewsByTag(tag: String): List<View>{
    val views = mutableListOf<View>()
    val count = childCount
    for(i in 0 until count){
        val child = getChildAt(i)
        val t = child.tag
        if(t==tag){
            views.add(child)
        }
        (child as? ViewGroup)?.findViewsByTag(tag)?.apply {
            views.addAll(this)
        }
    }
    return views
}

fun Fragment.toastShort(message: String){
    try {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    } catch (e: Exception) {
    }
}

fun Fragment.toastLong(message: String){
    try {
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
    } catch (e: Exception) {
    }
}
/********************messanger extensions************************/
fun AppCompatActivity.subscribe(channel: String, callback: ((Message?)->Unit)?)
    = Messanger.subscribe(myClassName,channel,callback)

fun AppCompatActivity.publish(message: Message?=null) = Messanger.publish(myClassName,message)

fun AppCompatActivity.cancelSubscription(channel: String) = Messanger.cancel(myClassName,channel)

fun Fragment.subscribe(channel: String, callback: ((Message?)->Unit)?)
    = Messanger.subscribe(myClassName,channel,callback)

fun Fragment.publish(message: Message?=null) = Messanger.publish(myClassName,message)

fun Fragment.cancelSubscription(channel: String?=null) = Messanger.cancel(myClassName,channel)
/********************messanger extensions end************************/


