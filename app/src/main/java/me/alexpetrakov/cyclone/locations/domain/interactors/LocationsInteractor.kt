package me.alexpetrakov.cyclone.locations.domain.interactors

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import me.alexpetrakov.cyclone.locations.domain.model.Coordinates
import me.alexpetrakov.cyclone.locations.domain.model.Location
import me.alexpetrakov.cyclone.locations.domain.repositories.LocationsRepository
import javax.inject.Inject

class LocationsInteractor @Inject constructor(private val locationsRepository: LocationsRepository) {

    val savedLocationsStream: Flow<List<Location>>
        get() = locationsRepository.getLocationsStream()

    val selectedLocationStream: Flow<Location>
        get() = locationsRepository.getSelectedLocationStream()

    suspend fun saveLocation(name: String, coordinates: Coordinates) {
        if (isLocationsLimitReached()) {
            return
        }
        locationsRepository.createLocation(Location.StoredLocation(0, name, coordinates))
    }

    suspend fun isLocationsLimitReached(): Boolean {
        return locationsRepository.getLocationsStream()
            .first().size >= MAX_NUMBER_OF_SAVED_LOCATIONS
    }

    suspend fun updateLocationsOrder(ids: List<Int>) {
        val filteredIds = ids.filter { it != Location.CurrentLocation.id }
        locationsRepository.updateLocationsOrder(filteredIds)
    }

    suspend fun removeLocation(id: Int) {
        locationsRepository.removeLocationById(id)
    }

    suspend fun selectLocation(id: Int) {
        locationsRepository.selectLocation(id)
    }

    suspend fun getSelectedLocation(): Location {
        return locationsRepository.getSelectedLocation()
    }

    companion object {
        private const val MAX_NUMBER_OF_SAVED_LOCATIONS = 6
    }
}