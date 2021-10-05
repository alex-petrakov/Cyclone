package me.alexpetrakov.cyclone.weather.presentation

sealed class ViewEffect {
    object OpenAppSettings : ViewEffect()
    object OpenLocationSettings : ViewEffect()
    object CheckLocationAccess : ViewEffect()
    object ShowLocationPermissionRationale : ViewEffect()
    object ShowLocationPermissionRequest : ViewEffect()
    data class ResolveException(val throwable: Throwable?) : ViewEffect()
    object None : ViewEffect()
}