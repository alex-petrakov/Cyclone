package me.alexpetrakov.cyclone.weather.presentation

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.AttrRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.annotation.StyleRes
import androidx.core.content.res.use
import androidx.core.view.isVisible
import me.alexpetrakov.cyclone.R
import kotlin.math.roundToInt


class ErrorView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    @AttrRes defStyleAttr: Int = 0,
    @StyleRes defStyleRes: Int = 0
) : FrameLayout(context, attrs, defStyleAttr, defStyleRes) {

    private val iconImageView: ImageView

    private val messageTextView: TextView

    private val primaryButton: Button

    private val secondaryButton: Button

    val icon: Drawable
        get() = iconImageView.drawable

    val message: CharSequence
        get() = messageTextView.text

    val primaryActionText: CharSequence
        get() = primaryButton.text

    val secondaryActionText: CharSequence
        get() = secondaryButton.text

    var secondaryActionIsVisible: Boolean
        get() = secondaryButton.isVisible
        set(value) {
            secondaryButton.isVisible = value
        }

    init {
        inflate(context, R.layout.layout_error_view, this)
        setPadding(16.dp, 16.dp, 16.dp, 16.dp)

        iconImageView = findViewById(R.id.icon_image_view)
        messageTextView = findViewById(R.id.message_text_view)
        primaryButton = findViewById(R.id.primary_button)
        secondaryButton = findViewById(R.id.secondary_button)

        context.obtainStyledAttributes(attrs, R.styleable.ErrorView, 0, 0).use { a ->
            messageTextView.text = a.getText(R.styleable.ErrorView_evMessageText) ?: ""
            primaryButton.text = a.getText(R.styleable.ErrorView_evPrimaryActionText) ?: ""
            secondaryButton.apply {
                text = a.getText(R.styleable.ErrorView_evSecondaryActionText) ?: ""
                isVisible = a.getBoolean(
                    R.styleable.ErrorView_evSecondaryActionIsVisible,
                    false
                )
            }
            val iconDrawable = a.getDrawable(R.styleable.ErrorView_evIcon)
            iconImageView.setImageDrawable(iconDrawable)
        }
    }

    private val Int.dp
        get() = (resources.displayMetrics.density * this).roundToInt()

    fun setIcon(@DrawableRes iconResId: Int) {
        iconImageView.setImageResource(iconResId)
    }

    fun setIcon(drawable: Drawable) {
        iconImageView.setImageDrawable(drawable)
    }

    fun setMessage(@StringRes stringResId: Int) {
        messageTextView.setText(stringResId)
    }

    fun setMessage(text: CharSequence) {
        messageTextView.text = text
    }

    fun setPrimaryActionText(@StringRes stringResId: Int) {
        primaryButton.setText(stringResId)
    }

    fun setPrimaryActionText(text: CharSequence) {
        primaryButton.text = text
    }

    fun setSecondaryActionText(@StringRes stringResId: Int) {
        secondaryButton.setText(stringResId)
    }

    fun setSecondaryActionText(text: CharSequence) {
        secondaryButton.text = text
    }

    fun setPrimaryAction(action: ((View) -> Unit)?) {
        primaryButton.setOnClickListener(action)
    }

    fun setSecondaryAction(action: ((View) -> Unit)?) {
        secondaryButton.setOnClickListener(action)
    }
}