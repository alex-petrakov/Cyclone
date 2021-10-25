package me.alexpetrakov.cyclone.units.domain.interactors

import kotlinx.coroutines.flow.Flow
import me.alexpetrakov.cyclone.units.domain.model.PreferredUnits
import me.alexpetrakov.cyclone.units.domain.model.unitsofmeasure.LengthUnit
import me.alexpetrakov.cyclone.units.domain.model.unitsofmeasure.PressureUnit
import me.alexpetrakov.cyclone.units.domain.model.unitsofmeasure.SpeedUnit
import me.alexpetrakov.cyclone.units.domain.model.unitsofmeasure.TemperatureUnit
import me.alexpetrakov.cyclone.units.domain.repositories.UnitsRepository
import javax.inject.Inject

class UnitsInteractor @Inject constructor(private val unitsRepository: UnitsRepository) {

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