package me.alexpetrakov.cyclone.locations.domain

sealed class Location {

    abstract val id: Int

    object CurrentLocation : Location() {
        override val id get() = 0
    }

    class StoredLocation(
        override val id: Int,
        val name: String,
        val coordinates: Coordinates
    ) : Location()
}