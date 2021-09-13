package me.alexpetrakov.cyclone.locations.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.github.terrakok.cicerone.Router
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import me.alexpetrakov.cyclone.AppScreens
import me.alexpetrakov.cyclone.R
import me.alexpetrakov.cyclone.common.presentation.SingleLiveEvent
import me.alexpetrakov.cyclone.common.presentation.TextResource
import me.alexpetrakov.cyclone.common.presentation.asTextResource
import me.alexpetrakov.cyclone.locations.domain.Coordinates
import me.alexpetrakov.cyclone.locations.domain.Location
import me.alexpetrakov.cyclone.locations.domain.LocationsRepository

class LocationsViewModel(
    private val router: Router,
    private val locationsRepository: LocationsRepository
) : ViewModel() {

    val viewState = locationsRepository.getLocationsStream()
        .map { list -> ViewState(list.map { it.toUiModel() }) }
        .asLiveData()

    private val locations = listOf(
        Location.StoredLocation(
            0,
            "Moscow",
            Coordinates(55.751244, 37.618423)
        ),
        Location.StoredLocation(
            0,
            "Bordeaux",
            Coordinates(44.836151, -0.580816)
        ),
        Location.StoredLocation(
            0,
            "Chicago",
            Coordinates(41.881832, -87.623177)
        )
    )

    private val _viewEffect = SingleLiveEvent<ViewEffect>()

    val viewEffect: LiveData<ViewEffect> get() = _viewEffect


    fun onAddLocation() {
        viewModelScope.launch {
            locationsRepository.createLocation(locations.random())
        }
    }

    fun onUpdateLocationsOrder(currentList: List<LocationUiItem>) {
        viewModelScope.launch {
            val ids = currentList.drop(1).map { it.id }
            locationsRepository.updateLocationsOrder(ids)
        }
    }

    fun onTryToRemoveLocation(location: LocationUiItem) {
        _viewEffect.value = ViewEffect.DisplayRemovalConfirmation(
            TextResource.from(R.string.locations_removal_confirmation_template, location.name),
            location.id
        )
    }

    fun onConfirmLocationRemoval(locationId: Int) {
        viewModelScope.launch {
            locationsRepository.removeLocationById(locationId)
        }
    }

    fun onNavigateBack() {
        router.exit()
    }

    private fun Location.toUiModel(): LocationUiItem {
        return when (this) {
            Location.CurrentLocation -> LocationUiItem.fromCurrentLocation(this.id)
            is Location.StoredLocation -> LocationUiItem.fromSavedLocation(
                id,
                name.asTextResource(),
                true
            )
        }
    }

    fun onSelectLocation(item: LocationUiItem) {
        viewModelScope.launch {
            locationsRepository.selectLocation(item.id)
            router.newRootScreen(AppScreens.weather())
        }
    }
}