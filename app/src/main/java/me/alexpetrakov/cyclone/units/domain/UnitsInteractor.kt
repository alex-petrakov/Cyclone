package me.alexpetrakov.cyclone.units.domain

import kotlinx.coroutines.flow.Flow
import me.alexpetrakov.cyclone.units.domain.unitsofmeasure.LengthUnit
import me.alexpetrakov.cyclone.units.domain.unitsofmeasure.PressureUnit
import me.alexpetrakov.cyclone.units.domain.unitsofmeasure.SpeedUnit
import me.alexpetrakov.cyclone.units.domain.unitsofmeasure.TemperatureUnit

class UnitsInteractor(private val unitsRepository: UnitsRepository) {

    val temperatureUnits: List<TemperatureUnit> get() = unitsRepository.temperatureUnits

    val defaultTemperatureUnit: TemperatureUnit get() = unitsRepository.defaultTemperatureUnit

    val distanceUnits: List<LengthUnit> get() = unitsRepository.distanceUnits

    val defaultDistanceUnit: LengthUnit get() = unitsRepository.defaultDistanceUnit

    val speedUnits: List<SpeedUnit> get() = unitsRepository.speedUnits

    val defaultSpeedUnit: SpeedUnit get() = unitsRepository.defaultSpeedUnit

    val pressureUnits: List<PressureUnit> get() = unitsRepository.pressureUnits

    val defaultPressureUnit: PressureUnit get() = unitsRepository.defaultPressureUnit

    val preferredUnitsStream: Flow<PreferredUnits>
        get() = unitsRepository.getPreferredUnitsStream()

    suspend fun getPreferredUnits(): PreferredUnits {
        return unitsRepository.getPreferredUnits()
    }
}