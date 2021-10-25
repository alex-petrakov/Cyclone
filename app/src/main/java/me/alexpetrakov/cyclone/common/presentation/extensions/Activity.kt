package me.alexpetrakov.cyclone.common.presentation.extensions

import android.app.Activity
import android.content.Context
import android.view.inputmethod.InputMethodManager
import me.alexpetrakov.cyclone.AppComponent
import me.alexpetrakov.cyclone.CycloneApp

fun Activity.hideKeyboard() {
    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    val focus = currentFocus ?: return
    imm.hideSoftInputFromWindow(focus.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
}

fun Activity.requireAppComponent(): AppComponent {
    return requireApp().appComponent
}

private fun Activity.requireApp(): CycloneApp {
    return application as CycloneApp
}