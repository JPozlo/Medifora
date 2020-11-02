package com.misolova.medifora.util

import android.content.Context
import android.graphics.Color
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AlertDialog
import com.google.android.material.snackbar.Snackbar

fun ViewGroup.inflate(@LayoutRes layoutRes: Int, attachToRoot: Boolean = false): View {
    return LayoutInflater.from(context).inflate(layoutRes, this, attachToRoot)
}

fun String.isEmailValid(): Boolean {
    return !TextUtils.isEmpty(this) && android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()
}

fun View.showSingleActionSnackbar(message: String){
    Snackbar.make(
        this,
        message,
        Snackbar.LENGTH_INDEFINITE
    ).setAction("OK"){}.setActionTextColor(Color.MAGENTA).setAnimationMode(Snackbar.ANIMATION_MODE_SLIDE).show()
}

fun View.showShortActionSnackbar(message: String){
    Snackbar.make(
        this,
        message,
        Snackbar.LENGTH_LONG
    ).setAction("OK"){}.setActionTextColor(Color.MAGENTA).setAnimationMode(Snackbar.ANIMATION_MODE_SLIDE).show()
}

fun Context.showAlert(title: String, message: String): AlertDialog.Builder? {
    val builder = AlertDialog.Builder(this)
        .setTitle(title)
        .setMessage(message)
    return builder

}

