package me.alexpetrakov.cyclone.units.domain

import kotlinx.coroutines.flow.Flow

interface UnitsRepository {

    suspend fun getPreferredUnits(): PreferredUnits

    fun observePreferredUnits(): Flow<PreferredUnits>
}