package me.alexpetrakov.cyclone.locationsearch.presentation

sealed class ViewState {

    abstract val isLoading: Boolean

    data class Content(
        override val isLoading: Boolean,
        val searchResults: List<SearchResultUiItem>
    ) : ViewState()

    data class Empty(override val isLoading: Boolean) : ViewState()
}