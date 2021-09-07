package me.alexpetrakov.cyclone.units.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import me.alexpetrakov.cyclone.units.domain.PreferredUnits
import me.alexpetrakov.cyclone.units.domain.UnitsLocale
import me.alexpetrakov.cyclone.units.domain.UnitsRepository
import me.alexpetrakov.cyclone.units.domain.unitsofmeasure.PressureUnit

class UnitsDataStore : UnitsRepository {

    private val russianUnits = PreferredUnits.of(
        UnitsLocale.INTERNATIONAL,
        PressureUnit.millimeterOfMercury
    )

    override suspend fun getPreferredUnits(): PreferredUnits {
        return russianUnits
    }

    override fun observePreferredUnits(): Flow<PreferredUnits> {
        return flowOf(russianUnits)
    }
}