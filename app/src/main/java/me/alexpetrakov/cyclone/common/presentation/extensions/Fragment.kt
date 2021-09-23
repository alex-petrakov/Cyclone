package me.alexpetrakov.cyclone.common.presentation.extensions

import androidx.fragment.app.Fragment

fun Fragment.hideKeyboard() {
    requireActivity().hideKeyboard()
}