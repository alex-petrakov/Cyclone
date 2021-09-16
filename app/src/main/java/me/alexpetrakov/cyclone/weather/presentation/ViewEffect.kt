package me.alexpetrakov.cyclone.weather.presentation

sealed class ViewEffect {
    object OpenAppSettings : ViewEffect()
    object OpenLocationSettings : ViewEffect()
    object RequestLocationAccess : ViewEffect()
    data class ResolveException(val throwable: Throwable?) : ViewEffect()
    object None : ViewEffect()
}