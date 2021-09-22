package me.alexpetrakov.cyclone.locationsearch.presentation

sealed class ViewState {
    data class Content(val searchResults: List<SearchResultUiItem>) : ViewState()
    object Empty : ViewState()
}