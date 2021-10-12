package me.alexpetrakov.cyclone.common.presentation.extensions

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.inputmethod.InputMethodManager
import androidx.core.view.*

/*
 * Taken from https://developer.squareup.com/blog/showing-the-android-keyboard-reliably/
 */
fun View.focusAndShowKeyboard(flags: Int = InputMethodManager.SHOW_IMPLICIT) {
    /*
     * This is to be called when the window already has focus.
     */
    fun View.showTheKeyboardNow() {
        if (isFocused) {
            post {
                // We still post the call, just in case we are being notified of the windows focus
                // but InputMethodManager didn't get properly setup yet.
                val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE)
                        as InputMethodManager
                imm.showSoftInput(this, flags)
            }
        }
    }

    requestFocus()
    if (hasWindowFocus()) {
        showTheKeyboardNow()
    } else {
        // We need to wait until the window gets focus.
        viewTreeObserver.addOnWindowFocusChangeListener(
            object : ViewTreeObserver.OnWindowFocusChangeListener {
                override fun onWindowFocusChanged(hasFocus: Boolean) {
                    // This notification will arrive just before the InputMethodManager gets set up.
                    if (hasFocus) {
                        this@focusAndShowKeyboard.showTheKeyboardNow()
                        // It’s very important to remove this listener once we are done.
                        viewTreeObserver.removeOnWindowFocusChangeListener(this)
                    }
                }
            })
    }
}


fun View.extendBottomPaddingWithSystemInsets() {
    doOnApplyWindowInsets { view, windowInsets, _, paddingState ->
        view.updatePadding(
            bottom = paddingState.bottom + windowInsets.systemBars.bottom
        )
        WindowInsetsCompat.CONSUMED
    }
}

fun View.extendBottomMarginWithSystemInsets() {
    doOnApplyWindowInsets { view, windowInsets, marginState, _ ->
        view.updateLayoutParams<ViewGroup.MarginLayoutParams> {
            bottomMargin = marginState.bottom + windowInsets.systemBars.bottom
        }
    }
}

private val WindowInsetsCompat.systemBars get() = getInsets(WindowInsetsCompat.Type.systemBars())


/*
 * Idea adopted from https://chris.banes.dev/insets-listeners-to-layouts/
 */
fun View.doOnApplyWindowInsets(f: (View, WindowInsetsCompat, MarginState, PaddingState) -> Unit) {
    val initialMargin = this.getMarginState()
    val initialPadding = this.getPaddingState()
    ViewCompat.setOnApplyWindowInsetsListener(this) { v, insets ->
        f(v, insets, initialMargin, initialPadding)
        insets
    }
    requestApplyInsetsWhenAttached()
}

data class PaddingState(
    val left: Int,
    val top: Int,
    val right: Int,
    val bottom: Int,
    val start: Int,
    val end: Int
)

private fun View.getPaddingState(): PaddingState {
    return PaddingState(
        paddingLeft,
        paddingTop,
        paddingRight,
        paddingBottom,
        paddingStart,
        paddingEnd
    )
}

data class MarginState(
    val left: Int,
    val top: Int,
    val right: Int,
    val bottom: Int,
    val start: Int,
    val end: Int
) {
    constructor(all: Int) : this(all, all, all, all, all, all)
}

private fun View.getMarginState(): MarginState {
    val layoutParams = (layoutParams as? ViewGroup.MarginLayoutParams)
    return if (layoutParams == null) {
        MarginState(0)
    } else {
        MarginState(
            layoutParams.leftMargin,
            layoutParams.topMargin,
            layoutParams.rightMargin,
            layoutParams.bottomMargin,
            layoutParams.marginStart,
            layoutParams.marginEnd
        )
    }
}

fun View.requestApplyInsetsWhenAttached() {
    if (isAttachedToWindow) {
        requestApplyInsets()
    } else {
        addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
            override fun onViewAttachedToWindow(v: View) {
                v.removeOnAttachStateChangeListener(this)
                v.requestApplyInsets()
            }

            override fun onViewDetachedFromWindow(v: View) = Unit
        })
    }
}