package me.alexpetrakov.cyclone.common.presentation.extensions

fun String.withCapitalizedFirstChar(): String {
    return replaceFirstChar { it.titlecase() }
}