package me.alexpetrakov.cyclone.locationsearch.domain

import me.alexpetrakov.cyclone.locations.domain.Coordinates

data class SearchResult(
    val placeName: String,
    val countryName: String,
    val coordinates: Coordinates
)