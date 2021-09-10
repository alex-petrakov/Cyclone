package me.alexpetrakov.cyclone.locations.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.github.terrakok.cicerone.Router
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import me.alexpetrakov.cyclone.common.presentation.asTextResource
import me.alexpetrakov.cyclone.locations.domain.Coordinates
import me.alexpetrakov.cyclone.locations.domain.Location
import me.alexpetrakov.cyclone.locations.domain.LocationsRepository
import kotlin.random.Random

class LocationsViewModel(
    private val router: Router,
    private val locationsRepository: LocationsRepository
) : ViewModel() {

    val viewState = locationsRepository.getLocationsStream()
        .map { list -> ViewState(list.map { it.toUiModel() }) }
        .asLiveData()

    private val random = Random(System.currentTimeMillis())


    fun onAddLocation() {
        viewModelScope.launch {
            locationsRepository.createLocation(
                Location.StoredLocation(
                    0,
                    "Moscow ${random.nextInt(100)}",
                    Coordinates(55.751244, 37.618423)
                )
            )
        }
    }

    fun onUpdateLocationsOrder(currentList: List<LocationUiItem>) {
        viewModelScope.launch {
            val ids = currentList.drop(1).map { it.id }
            locationsRepository.updateLocationsOrder(ids)
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
}