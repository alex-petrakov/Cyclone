package me.alexpetrakov.cyclone.locations.domain.model

data class Coordinates(val lat: Double, val lon: Double) {
    init {
        require(lat in -90.0..90.0)
        require(lon in -180.0..180.0)
    }
}