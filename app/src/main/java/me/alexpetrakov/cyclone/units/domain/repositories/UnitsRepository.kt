package me.alexpetrakov.cyclone.units.domain.repositories

import kotlinx.coroutines.flow.Flow
import me.alexpetrakov.cyclone.units.domain.model.PreferredUnits
import me.alexpetrakov.cyclone.units.domain.model.unitsofmeasure.LengthUnit
import me.alexpetrakov.cyclone.units.domain.model.unitsofmeasure.PressureUnit
import me.alexpetrakov.cyclone.units.domain.model.unitsofmeasure.SpeedUnit
import me.alexpetrakov.cyclone.units.domain.model.unitsofmeasure.TemperatureUnit

interface UnitsRepository {

    val temperatureUnits: List<TemperatureUnit>

    val defaultTemperatureUnit: TemperatureUnit

    val distanceUnits: List<LengthUnit>

    val defaultDistanceUnit: LengthUnit

    val speedUnits: List<SpeedUnit>

    val defaultSpeedUnit: SpeedUnit

    val pressureUnits: List<PressureUnit>

    val defaultPressureUnit: PressureUnit

    suspend fun getPreferredUnits(): PreferredUnits

    fun getPreferredUnitsStream(): Flow<PreferredUnits>

    companion object {
        const val PREF_KEY_TEMPERATURE_UNIT = "temp_unit"
        const val PREF_KEY_DISTANCE_UNIT = "dist_unit"
        const val PREF_KEY_SPEED_UNIT = "speed_unit"
        const val PREF_KEY_PRESSURE_UNIT = "pressure_unit"
    }
}