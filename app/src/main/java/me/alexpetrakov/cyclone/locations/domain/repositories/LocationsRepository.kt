package me.alexpetrakov.cyclone.locations.domain.repositories

import kotlinx.coroutines.flow.Flow
import me.alexpetrakov.cyclone.locations.domain.model.Location

interface LocationsRepository {

    fun getLocationsStream(): Flow<List<Location>>

    suspend fun createLocation(location: Location.StoredLocation)

    suspend fun updateLocationsOrder(ids: List<Int>)

    suspend fun removeLocationById(id: Int)

    suspend fun selectLocation(id: Int)

    suspend fun getSelectedLocation(): Location

    fun getSelectedLocationStream(): Flow<Location>
}