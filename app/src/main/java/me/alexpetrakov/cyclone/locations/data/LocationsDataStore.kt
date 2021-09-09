package me.alexpetrakov.cyclone.locations.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import me.alexpetrakov.cyclone.locations.domain.Coordinates
import me.alexpetrakov.cyclone.locations.domain.Location
import me.alexpetrakov.cyclone.locations.domain.LocationsRepository

class LocationsDataStore : LocationsRepository {

    private val moscow = Location.SavedLocation(
        1,
        "Moscow",
        Coordinates(55.751244, 37.618423)
    )

    private val savedLocations = listOf(
        Location.SavedLocation(1, "Moscow", Coordinates(55.751244, 37.618423)),
        Location.SavedLocation(2, "Berlin", Coordinates(52.520008, 13.404954)),
        Location.SavedLocation(2, "Chicago", Coordinates(41.881832, -87.623177))
    )

    override fun getLocationsStream(): Flow<List<Location>> {
        return flowOf(listOf(Location.CurrentLocation) + savedLocations)
    }

    override suspend fun getSelectedLocation(): Location {
        return moscow
    }

    override fun observeSelectedLocation(): Flow<Location> {
        return flowOf(moscow)
    }
}