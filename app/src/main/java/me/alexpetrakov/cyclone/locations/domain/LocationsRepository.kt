package me.alexpetrakov.cyclone.locations.domain

import kotlinx.coroutines.flow.Flow

interface LocationsRepository {

    fun getLocationsStream(): Flow<List<Location>>

    suspend fun getSelectedLocation(): Location

    fun observeSelectedLocation(): Flow<Location>
}