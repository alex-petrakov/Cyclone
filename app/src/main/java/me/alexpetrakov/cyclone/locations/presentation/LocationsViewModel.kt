package me.alexpetrakov.cyclone.locations.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.github.terrakok.cicerone.Router
import kotlinx.coroutines.flow.map
import me.alexpetrakov.cyclone.common.presentation.asTextResource
import me.alexpetrakov.cyclone.locations.domain.Location
import me.alexpetrakov.cyclone.locations.domain.LocationsRepository

class LocationsViewModel(
    private val router: Router,
    locationsRepository: LocationsRepository
) : ViewModel() {

    val viewState = locationsRepository.getLocationsStream()
        .map { list -> ViewState(list.map { it.toUiModel() }) }
        .asLiveData()


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
}