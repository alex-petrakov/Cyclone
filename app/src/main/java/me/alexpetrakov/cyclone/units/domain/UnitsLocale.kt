package me.alexpetrakov.cyclone.units.domain

import me.alexpetrakov.cyclone.weather.domain.units.LengthUnit
import me.alexpetrakov.cyclone.weather.domain.units.SpeedUnit
import me.alexpetrakov.cyclone.weather.domain.units.TemperatureUnit

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