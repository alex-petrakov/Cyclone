package me.alexpetrakov.cyclone.locationsearch.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.terrakok.cicerone.Router
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import me.alexpetrakov.cyclone.locations.domain.Location
import me.alexpetrakov.cyclone.locations.domain.LocationsRepository
import me.alexpetrakov.cyclone.locationsearch.domain.LocationSearchRepository
import me.alexpetrakov.cyclone.locationsearch.domain.SearchResult

class LocationSearchViewModel(
    private val locationSearchRepository: LocationSearchRepository,
    private val locationsRepository: LocationsRepository,
    private val router: Router
) : ViewModel() {

    // TODO: Preserve search results across process death
    private val _viewState = MutableLiveData<ViewState>().apply {
        value = ViewState.Content(emptyList())
    }

    val viewState: LiveData<ViewState> get() = _viewState

    fun onQueryChanged(text: String) {
        if (text.isBlank()) {
            return
        }
        viewModelScope.launch {
            // TODO: Add a progress indicator
            _viewState.value = locationSearchRepository.searchLocations(text)
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

    private suspend fun mapSearchResultsToViewState(searchResults: List<SearchResult>): ViewState {
        return withContext(Dispatchers.Default) {
            when {
                searchResults.isEmpty() -> ViewState.Empty
                else -> ViewState.Content(searchResults.map { it.toUiModel() })
            }
        }
    }

    private fun SearchResult.toUiModel(): SearchResultUiItem {
        return SearchResultUiItem(placeName, countryName, coordinates)
    }

    private fun mapFailureToViewState(throwable: Throwable): ViewState {
        return ViewState.Empty // TODO: Display an error view and provide Retry action
    }

    fun onNavigateBack() {
        router.exit()
    }
}
