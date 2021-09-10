package me.alexpetrakov.cyclone.common.presentation

import android.content.res.Resources
import androidx.annotation.PluralsRes
import androidx.annotation.StringRes

sealed class TextResource {
    companion object {
        fun from(string: String): TextResource = StringTextResource(string)

        fun from(@StringRes resId: Int, vararg formatArgs: Any): TextResource =
            SimpleTextResource(resId, formatArgs.toList())

        fun from(@PluralsRes resId: Int, quantity: Int, vararg formatArgs: Any): TextResource =
            PluralTextResource(resId, quantity, formatArgs.toList())
    }
}

private data class StringTextResource(val string: String) : TextResource()

private data class SimpleTextResource(
    @StringRes val resId: Int,
    val formatArgs: List<Any>
) : TextResource()

private data class PluralTextResource(
    @PluralsRes val resId: Int,
    val quantity: Int,
    val formatArgs: List<Any>
) : TextResource()

fun TextResource.asString(resources: Resources): String {
    return when (this) {
        is StringTextResource -> string
        is SimpleTextResource -> {
            resources.getString(resId, *resolveArguments(formatArgs, resources).toTypedArray())
        }
        is PluralTextResource -> resources.getQuantityString(
            resId,
            quantity,
            *resolveArguments(formatArgs, resources).toTypedArray()
        )
    }
}

fun String.asTextResource(): TextResource = TextResource.from(this)

private fun resolveArguments(formatArguments: List<Any>, resources: Resources): List<Any> {
    return formatArguments.map { arg ->
        when (arg) {
            is TextResource -> arg.asString(resources)
            else -> arg
        }
    }
}