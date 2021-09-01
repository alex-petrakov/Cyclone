package me.alexpetrakov.cyclone.units.domain

import me.alexpetrakov.cyclone.units.domain.unitsofmeasure.LengthUnit
import me.alexpetrakov.cyclone.units.domain.unitsofmeasure.SpeedUnit
import me.alexpetrakov.cyclone.units.domain.unitsofmeasure.TemperatureUnit

enum class UnitsLocale(
    val temperatureUnit: TemperatureUnit,
    val lengthUnit: LengthUnit,
    val speedUnit: SpeedUnit
) {
    INTERNATIONAL(TemperatureUnit.celsius, LengthUnit.kilometer, SpeedUnit.meterPerSecond),
    US(TemperatureUnit.fahrenheit, LengthUnit.mile, SpeedUnit.milePerHour),
    UK(TemperatureUnit.celsius, LengthUnit.mile, SpeedUnit.milePerHour),
    CANADA(TemperatureUnit.celsius, LengthUnit.kilometer, SpeedUnit.kilometerPerHour)
}