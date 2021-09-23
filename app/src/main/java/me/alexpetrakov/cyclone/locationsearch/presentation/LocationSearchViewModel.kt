package me.alexpetrakov.cyclone.locationsearch.presentation

import androidx.lifecycle.*
import com.github.terrakok.cicerone.Router
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import me.alexpetrakov.cyclone.locations.domain.Location
import me.alexpetrakov.cyclone.locations.domain.LocationsRepository
import me.alexpetrakov.cyclone.locationsearch.domain.Fail
import me.alexpetrakov.cyclone.locationsearch.domain.LocationSearchRepository
import me.alexpetrakov.cyclone.locationsearch.domain.SearchResult

class LocationSearchViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val locationSearchRepository: LocationSearchRepository,
    private val locationsRepository: LocationsRepository,
    private val router: Router
) : ViewModel() {

    private val _searchResultsViewState = MutableLiveData<SearchResultsViewState>().apply {
        value = SearchResultsViewState.Content(isLoading = false, emptyList())
    }

    val searchResultsViewState: LiveData<SearchResultsViewState> get() = _searchResultsViewState

    private val _queryViewState = MutableLiveData<String>().apply {
        value = savedStateHandle[STATE_QUERY] ?: ""
    }

    val queryViewState: LiveData<String> get() = _queryViewState

    init {
        if (savedStateHandle.contains(STATE_QUERY)) {
            onPerformSearch()
        }
    }

    fun onQueryTextChanged(text: String) {
        if (_queryViewState.value == text) {
            return
        }
        _queryViewState.value = text
        savedStateHandle[STATE_QUERY] = text
    }

    fun onPerformSearch() {
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
            _searchResultsViewState.value = locationSearchRepository.searchLocations(text)
                .fold(
                    { results -> mapSearchResultsToViewState(results) },
                    ::mapFailureToViewState
                )
        }
    }

    fun onAddSearchResultToSavedLocations(searchResult: SearchResultUiItem) {
        viewModelScope.launch {
            locationsRepository.createLocation(
                Location.StoredLocation(0, searchResult.placeName, searchResult.coordinates)
            )
            router.exit()
        }
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
        return SearchResultsViewState.Error(isLoading = false)
    }

    fun onNavigateBack() {
        router.exit()
    }

    companion object {
        private const val STATE_QUERY = "QUERY"
    }
}
