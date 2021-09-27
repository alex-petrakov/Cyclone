package me.alexpetrakov.cyclone.units.domain

import me.alexpetrakov.cyclone.units.domain.unitsofmeasure.LengthUnit
import me.alexpetrakov.cyclone.units.domain.unitsofmeasure.PressureUnit
import me.alexpetrakov.cyclone.units.domain.unitsofmeasure.SpeedUnit
import me.alexpetrakov.cyclone.units.domain.unitsofmeasure.TemperatureUnit

data class PreferredUnits(
    val temperatureUnit: TemperatureUnit,
    val lengthUnit: LengthUnit,
    val speedUnit: SpeedUnit,
    val pressureUnit: PressureUnit
) {
    companion object {
        fun of(unitsLocale: UnitsLocale): PreferredUnits {
            return PreferredUnits(
                unitsLocale.temperatureUnit,
                unitsLocale.lengthUnit,
                unitsLocale.speedUnit,
                unitsLocale.pressureUnit
            )
        }
    }
}
