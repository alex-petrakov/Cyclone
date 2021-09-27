package me.alexpetrakov.cyclone.units.domain

import me.alexpetrakov.cyclone.units.domain.unitsofmeasure.LengthUnit
import me.alexpetrakov.cyclone.units.domain.unitsofmeasure.PressureUnit
import me.alexpetrakov.cyclone.units.domain.unitsofmeasure.SpeedUnit
import me.alexpetrakov.cyclone.units.domain.unitsofmeasure.TemperatureUnit
import java.util.*

enum class UnitsLocale(
    val id: Int,
    val temperatureUnit: TemperatureUnit,
    val lengthUnit: LengthUnit,
    val speedUnit: SpeedUnit,
    val pressureUnit: PressureUnit
) {
    INTERNATIONAL(
        0,
        TemperatureUnit.Celsius,
        LengthUnit.Kilometers,
        SpeedUnit.MetersPerSecond,
        PressureUnit.Millibars
    ),
    CANADA(
        1,
        TemperatureUnit.Celsius,
        LengthUnit.Kilometers,
        SpeedUnit.KilometersPerHour,
        PressureUnit.InchesOfMercury
    ),
    RUSSIA(
        2,
        TemperatureUnit.Celsius,
        LengthUnit.Kilometers,
        SpeedUnit.MetersPerSecond,
        PressureUnit.MillimetersOfMercury
    ),
    US(
        3,
        TemperatureUnit.Fahrenheit,
        LengthUnit.Miles,
        SpeedUnit.MilesPerHour,
        PressureUnit.InchesOfMercury
    ),
    UK(
        4,
        TemperatureUnit.Celsius,
        LengthUnit.Miles,
        SpeedUnit.MilesPerHour,
        PressureUnit.Millibars
    );

    companion object {

        fun getDefault(): UnitsLocale {
            return get(Locale.getDefault())
        }

        fun get(locale: Locale): UnitsLocale {
            return when (locale) {
                Locale.CANADA -> CANADA
                Locale("ru") -> RUSSIA
                Locale.US -> US
                Locale.UK -> UK
                else -> INTERNATIONAL
            }
        }
    }
}