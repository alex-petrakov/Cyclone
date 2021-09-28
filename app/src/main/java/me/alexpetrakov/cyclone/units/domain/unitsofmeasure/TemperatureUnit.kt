package me.alexpetrakov.cyclone.units.domain.unitsofmeasure

import me.alexpetrakov.cyclone.units.domain.converters.Converter
import me.alexpetrakov.cyclone.units.domain.converters.LinearConverter
import me.alexpetrakov.cyclone.units.domain.converters.NothingConverter
import me.alexpetrakov.cyclone.units.domain.measurements.Measurement

enum class TemperatureUnit(
    override val symbol: String,
    override val converter: Converter
) : UnitDimension<TemperatureUnit> {

    Celsius("°C", LinearConverter(1.0, 273.15)),

    Fahrenheit("°F", LinearConverter(0.55555555555556, 255.37222222222427)),

    Kelvin("K", NothingConverter);

    override val baseUnit: TemperatureUnit
        get() = Kelvin


    companion object {

        fun from(symbol: String): TemperatureUnit? {
            return values().find { it.symbol == symbol }
        }
    }
}

typealias Temperature = Measurement<TemperatureUnit>