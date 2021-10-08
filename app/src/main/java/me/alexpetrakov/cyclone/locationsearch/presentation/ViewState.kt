package me.alexpetrakov.cyclone.locationsearch.presentation

import me.alexpetrakov.cyclone.locations.domain.model.Coordinates

sealed class SearchResultsViewState {

    abstract val isLoading: Boolean

    data class Content(
        override val isLoading: Boolean,
        val searchResults: List<SearchResultUiItem>
    ) : SearchResultsViewState()

    data class Empty(override val isLoading: Boolean) : SearchResultsViewState()

    data class Error(override val isLoading: Boolean) : SearchResultsViewState()
}

data class SearchResultUiItem(
    val placeName: String,
    val countryName: String,
    val coordinates: Coordinates
)