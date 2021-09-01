package me.alexpetrakov.cyclone.locations.domain

import kotlinx.coroutines.flow.Flow

interface LocationsRepository {

    suspend fun getSelectedLocation(): Location

    fun observeSelectedLocation(): Flow<Location>
}