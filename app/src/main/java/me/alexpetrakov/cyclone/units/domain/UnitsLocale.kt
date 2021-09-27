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
        TemperatureUnit.celsius,
        LengthUnit.kilometer,
        SpeedUnit.meterPerSecond,
        PressureUnit.millibar
    ),
    CANADA(
        1,
        TemperatureUnit.celsius,
        LengthUnit.kilometer,
        SpeedUnit.kilometerPerHour,
        PressureUnit.inchOfMercury
    ),
    RUSSIA(
        2,
        TemperatureUnit.celsius,
        LengthUnit.kilometer,
        SpeedUnit.meterPerSecond,
        PressureUnit.millimeterOfMercury
    ),
    US(
        3,
        TemperatureUnit.fahrenheit,
        LengthUnit.mile,
        SpeedUnit.milePerHour,
        PressureUnit.inchOfMercury
    ),
    UK(4, TemperatureUnit.celsius, LengthUnit.mile, SpeedUnit.milePerHour, PressureUnit.millibar);

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