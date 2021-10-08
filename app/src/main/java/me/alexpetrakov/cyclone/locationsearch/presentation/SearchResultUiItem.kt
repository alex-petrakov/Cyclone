package me.alexpetrakov.cyclone.locationsearch.presentation

import me.alexpetrakov.cyclone.locations.domain.model.Coordinates

data class SearchResultUiItem(
    val placeName: String,
    val countryName: String,
    val coordinates: Coordinates
)
