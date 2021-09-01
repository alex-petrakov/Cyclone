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

    override suspend fun getSelectedLocation(): Location {
        return moscow
    }

    override fun observeSelectedLocation(): Flow<Location> {
        return flowOf(moscow)
    }
}