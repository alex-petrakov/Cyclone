package me.alexpetrakov.cyclone.locationsearch.domain.model

import me.alexpetrakov.cyclone.locations.domain.model.Coordinates

data class SearchResult(
    val placeName: String,
    val countryName: String,
    val coordinates: Coordinates
)