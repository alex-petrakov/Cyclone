package me.alexpetrakov.cyclone.locationsearch.presentation

import androidx.lifecycle.*
import com.github.terrakok.cicerone.Router
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import me.alexpetrakov.cyclone.common.presentation.SingleLiveEvent
import me.alexpetrakov.cyclone.locations.domain.interactors.LocationsInteractor
import me.alexpetrakov.cyclone.locationsearch.domain.interactors.LocationSearchInteractor
import me.alexpetrakov.cyclone.locationsearch.domain.model.Fail
import me.alexpetrakov.cyclone.locationsearch.domain.model.SearchResult

class LocationSearchViewModel @AssistedInject constructor(
    @Assisted private val savedStateHandle: SavedStateHandle,
    private val locationSearchInteractor: LocationSearchInteractor,
    private val locationsInteractor: LocationsInteractor,
    private val router: Router
) : ViewModel() {

    private val _searchResultsViewState = MutableLiveData<SearchResultsViewState>().apply {
        value = SearchResultsViewState.Content(isLoading = false, emptyList())
    }
    val searchResultsViewState: LiveData<SearchResultsViewState> get() = _searchResultsViewState

    val queryViewState: LiveData<String> get() = savedStateHandle.getLiveData(STATE_QUERY, "")

    private val _viewEffect = SingleLiveEvent<ViewEffect>()
    val viewEffect: LiveData<ViewEffect> get() = _viewEffect

    init {
        if (savedStateHandle.contains(STATE_QUERY)) {
            onPerformSearch()
        }
    }

    fun onQueryTextChanged(text: String) {
        if (queryViewState.value == text) {
            return
        }
        savedStateHandle[STATE_QUERY] = text
    }

    fun onPerformSearch() {
        dispatchHideKeyboardEvent()
        val text = queryViewState.value!!
        if (text.isBlank()) {
            return
        }
        val currentState = _searchResultsViewState.value!!
        _searchResultsViewState.value = when (currentState) {
            is SearchResultsViewState.Content -> currentState.copy(isLoading = true)
            is SearchResultsViewState.Empty -> currentState.copy(isLoading = true)
            is SearchResultsViewState.Error -> currentState.copy(isLoading = true)
        }
        viewModelScope.launch {
            _searchResultsViewState.value = locationSearchInteractor.searchLocations(text)
                .fold(
                    { results -> mapSearchResultsToViewState(results) },
                    ::mapFailureToViewState
                )
        }
    }

    fun onAddSearchResultToSavedLocations(searchResult: SearchResultUiItem) {
        dispatchHideKeyboardEvent()
        viewModelScope.launch {
            locationsInteractor.saveLocation(searchResult.placeName, searchResult.coordinates)
            router.exit()
        }
    }

    fun onScrollResults() {
        dispatchHideKeyboardEvent()
    }

    fun onNavigateBack() {
        dispatchHideKeyboardEvent()
        router.exit()
    }

    private fun dispatchHideKeyboardEvent() {
        _viewEffect.value = ViewEffect.HIDE_KEYBOARD
    }

    private suspend fun mapSearchResultsToViewState(searchResults: List<SearchResult>): SearchResultsViewState {
        return withContext(Dispatchers.Default) {
            when {
                searchResults.isEmpty() -> SearchResultsViewState.Empty(isLoading = false)
                else -> SearchResultsViewState.Content(
                    isLoading = false,
                    searchResults.map { it.toUiModel() }
                )
            }
        }
    }

    private fun SearchResult.toUiModel(): SearchResultUiItem {
        return SearchResultUiItem(placeName, countryName, coordinates)
    }

    private fun mapFailureToViewState(fail: Fail): SearchResultsViewState {
        return when (fail) {
            is Fail.NoConnection -> SearchResultsViewState.Error(isLoading = false)
        }
    }

    companion object {
        private const val STATE_QUERY = "QUERY"
    }

    @AssistedFactory
    interface Factory {
        fun create(handle: SavedStateHandle): LocationSearchViewModel
    }
}
