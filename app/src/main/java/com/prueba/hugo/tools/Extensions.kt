package com.prueba.hugo.tools

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.view.animation.OvershootInterpolator
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.view.updatePadding
import androidx.databinding.BindingAdapter
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.button.MaterialButton
import com.prueba.hugo.R
import kotlinx.android.synthetic.main.alert_view.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.NumberFormat
import java.util.*


/* Created by
 Victor Hernandez
 on 13/01/2020.
 contact : victoralfonso92@yahoo.com
 */
fun FragmentActivity.launch(action: suspend () -> Unit) {
    lifecycleScope.launch {
        withContext(Dispatchers.Main) {
            action.invoke()
        }
    }
}



fun View.doOnApplyWindowInsets(f: (View, WindowInsets, InitialPadding) -> Unit) {
    // Create a snapshot of the view's padding state
    val initialPadding = recordInitialPaddingForView(this)
    // Set an actual OnApplyWindowInsetsListener which proxies to the given
    // lambda, also passing in the original padding state
    setOnApplyWindowInsetsListener { v, insets ->
        f(v, insets, initialPadding)
        // Always return the insets, so that children can also use them
        insets
    }
    // request some insets
    requestApplyInsetsWhenAttached()
}

fun View.requestApplyInsetsWhenAttached() {
    if (isAttachedToWindow) {
        // We're already attached, just request as normal
        requestApplyInsets()
    } else {
        // We're not attached to the hierarchy, add a listener to
        // request when we are
        addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
            override fun onViewAttachedToWindow(v: View) {
                v.removeOnAttachStateChangeListener(this)
                v.requestApplyInsets()
            }

            override fun onViewDetachedFromWindow(v: View) = Unit
        })
    }
}

data class InitialPadding(
    val left: Int, val top: Int,
    val right: Int, val bottom: Int
)

private fun recordInitialPaddingForView(view: View) = InitialPadding(
    view.paddingLeft, view.paddingTop, view.paddingRight, view.paddingBottom
)

fun FragmentActivity.setTransparentStatusBar() {
    window.apply {
        clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        statusBarColor = Color.TRANSPARENT
    }
}

fun View.marginUpdate() {
    systemUiVisibility =
        android.view.View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                android.view.View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
    setPadding(0, 0, 0, 0)
    doOnApplyWindowInsets { view, insets, padding ->
        view.updatePadding(bottom = padding.bottom + insets.systemWindowInsetBottom)
        //insets
    }

}

//Inline function for starting an Activity
inline fun <reified T : FragmentActivity> FragmentActivity.launchActivity(
    closeCurrent: Boolean = false,
    noinline init: Intent.() -> Unit = {}
) {
    val i = Intent(this, T::class.java)
    i.init()
    startActivity(i)
    overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
    if (closeCurrent) {
        finish()
    }
}

fun ViewModel.launchAPIRequest(action: suspend () -> Unit) {
    viewModelScope.launch {
        withContext(Dispatchers.IO) {
            action.invoke()
        }
    }
}
fun ViewModel.launch(action: suspend () -> Unit) {
    viewModelScope.launch {
        withContext(Dispatchers.Main) {
            action.invoke()
        }
    }
}

//Configure simple vertical or horizontal RecyclerView
fun <T> RecyclerView.configureRecyclerBinding(
    adapter: DynamicBindingAdapter<T>,
    isVertical: Boolean = true,
    isgrid: Boolean = false,
    numColumns: Int = 2
) {
    itemAnimator = androidx.recyclerview.widget.DefaultItemAnimator()
    if (isgrid) {
        layoutManager = androidx.recyclerview.widget.GridLayoutManager(context, numColumns)
    } else {
        layoutManager = LinearLayoutManager(
            context,
            if (isVertical) RecyclerView.VERTICAL else RecyclerView.HORIZONTAL, false
        )
    }

    this.adapter = adapter
}

fun MaterialButton.changeState(ctx: Context, state:Boolean = false){
    backgroundTintList = ctx.resources.getColorStateList(R.color.colorMetalBlack)
    setTextColor(ctx.resources.getColor(R.color.colorWhite))
    if(state){
        backgroundTintList = ctx.resources.getColorStateList(R.color.colorYellow)
        setTextColor(ctx.resources.getColor(R.color.colorMetalBlack))
    }

}

@BindingAdapter("image_local")
fun ImageView.fondoLocal(resource:Int) {
    try {
        val options = RequestOptions()
            .priority(Priority.NORMAL)
            .format(DecodeFormat.PREFER_RGB_565)
            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
            .fitCenter()

        Glide.with(this)
            .asBitmap()
            .load(resource)
            .apply(options)
            .into(this)
    } catch (e: Exception) {
    }
}

fun EditText.changeText():String {
    var text = ""
    this.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            text = "P $p0"
        }

        @SuppressLint("SetTextI18n")
        override fun afterTextChanged(s: Editable?) {

        }
    })
return text
}

fun Double.ConvertAmount(): String {
    // creating the locale for US dollars UU
    val US = Locale("en", "US")
    // Creating number format object
    val form: NumberFormat = NumberFormat.getCurrencyInstance(US)
    // return dollars formatted to verify
    return (form.format(this))
}


fun String.BillParking():String{
    return  when(this){
        Cons.OFICIAL ->{
            "${Cons.BILL_OFICIAL.ConvertAmount()}/min"
        }
        Cons.RESIDENTE ->{
            "${Cons.BILL_RESIDENTE.ConvertAmount()}/min"
        }
        Cons.NO_RESIDENTE ->{
            "${Cons.BILL_NO_RESIDENTE.ConvertAmount()}/min"
        }
        else -> "${Cons.BILL_OFICIAL.ConvertAmount()}/min"
    }
}

fun FragmentActivity.showAlert( message: String,delete:Boolean = false, onSafeClick: (View) -> Unit = {},onCancelClick: (View) -> Unit = {}) {
    val inflater: LayoutInflater = LayoutInflater.from(this)
    val view = inflater.inflate(R.layout.alert_view, null)
    val dialog = Dialog(this, R.style.hidetitle)
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
    dialog.setContentView(view)
    dialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
    dialog.setCancelable(false)
    dialog.setCanceledOnTouchOutside(false)
    dialog.window!!.setLayout(
        LinearLayout.LayoutParams.MATCH_PARENT,
        LinearLayout.LayoutParams.MATCH_PARENT
    )
    dialog.show()
    view.txtDescripcion.gravity = Gravity.CENTER
    view.txtDescripcion.text = message
    view.btnAcept.setOnClickListener{
        onSafeClick.invoke(it)
        dialog.dismiss()
    }
    view.btnCancel.setOnClickListener{
        onCancelClick.invoke(it)
        dialog.dismiss()
    }
    if(delete){
        view.btnCancel.makeVisibility(true)
    }
}

fun View.makeVisibility(visibilidad:Boolean = false){
    this.visibility = android.view.View.GONE
    if(visibilidad){
        this.visibility = android.view.View.VISIBLE
    }
}

@BindingAdapter("custom_progress")
fun CustomProgressBar.startAnimation(progress:Float = 50.0f) {
    val localProgressBarAnimation = ProgressBarAnimation(0.0f, progress,this)
    localProgressBarAnimation.interpolator = OvershootInterpolator(0.5f)
    localProgressBarAnimation.duration = 4000L
    startAnimation(localProgressBarAnimation)
}

fun View.setSafeOnClickListener(onSafeClick: (View) -> Unit) {
    val safeClickListener = SafeClickListener { view ->
        onSafeClick(view)
    }
    setOnClickListener(safeClickListener)
}

//navigations

fun View.GotoSpecificFragmentClear(action: Int){
    Navigation.findNavController(this)
        .navigate(action,
            null,
            NavOptions.Builder()
                .setPopUpTo(action,
                    true).build())
}

fun View.GotoNavigate(action: Int, arg: Bundle? = null) {
    Navigation.findNavController(this)
        .navigate(action, arg)
}
fun View.GotoBack() {
    Navigation.findNavController(this).popBackStack()

}

fun <T> RecyclerView.configureRecyclerRealm(adapter: DynamicAdapterRealm<T>, isVertical: Boolean = true, isgrid: Boolean = false, numColumns:Int = 2) {
    itemAnimator = androidx.recyclerview.widget.DefaultItemAnimator()
    if (isgrid) {
        layoutManager = androidx.recyclerview.widget.GridLayoutManager(context, numColumns)
    } else {
        layoutManager = LinearLayoutManager(context,
            if (isVertical) RecyclerView.VERTICAL else RecyclerView.HORIZONTAL, false)
    }

    this.adapter = adapter
}


