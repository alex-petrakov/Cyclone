package me.alexpetrakov.cyclone.units.domain

import kotlinx.coroutines.flow.Flow

class UnitsInteractor(private val unitsRepository: UnitsRepository) {

    val preferredUnitsStream: Flow<PreferredUnits>
        get() = unitsRepository.getPreferredUnitsStream()

    suspend fun getPreferredUnits(): PreferredUnits {
        return unitsRepository.getPreferredUnits()
    }
}