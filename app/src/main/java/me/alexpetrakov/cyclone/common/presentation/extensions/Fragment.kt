package me.alexpetrakov.cyclone.common.presentation.extensions

import androidx.fragment.app.Fragment
import me.alexpetrakov.cyclone.AppComponent

fun Fragment.hideKeyboard() {
    requireActivity().hideKeyboard()
}

fun Fragment.requireAppComponent(): AppComponent {
    return requireActivity().requireAppComponent()
}