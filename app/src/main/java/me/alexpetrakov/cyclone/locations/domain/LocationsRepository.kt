package me.alexpetrakov.cyclone.locations.domain

import kotlinx.coroutines.flow.Flow

interface LocationsRepository {

    fun getLocationsStream(): Flow<List<Location>>

    suspend fun createLocation(location: Location.StoredLocation)

    suspend fun updateLocationsOrder(ids: List<Int>)

    suspend fun getSelectedLocation(): Location

    fun observeSelectedLocation(): Flow<Location>
}