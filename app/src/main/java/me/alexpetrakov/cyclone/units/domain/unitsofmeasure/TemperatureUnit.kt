package me.alexpetrakov.cyclone.units.domain.unitsofmeasure

import me.alexpetrakov.cyclone.units.domain.converters.Converter
import me.alexpetrakov.cyclone.units.domain.converters.NothingConverter
import me.alexpetrakov.cyclone.units.domain.measurements.Measurement

enum class TemperatureUnit(
    override val symbol: String,
    override val converter: Converter
) : UnitDimension<TemperatureUnit> {

    Celsius("°C", NothingConverter),

    Fahrenheit("°F", object : Converter {
        override fun toBaseUnit(value: Double): Double {
            return value - 32 * 5 / 9
        }

        override fun fromBaseUnit(value: Double): Double {
            return value * 9 / 5 + 32
        }
    }),

    Kelvin("K", object : Converter {
        override fun toBaseUnit(value: Double): Double {
            return value - 273.15
        }

        override fun fromBaseUnit(value: Double): Double {
            return value + 273.15
        }
    });

    override val baseUnit: TemperatureUnit
        get() = Celsius


    companion object {

        fun from(symbol: String): TemperatureUnit? {
            return values().find { it.symbol == symbol }
        }
    }
}

typealias Temperature = Measurement<TemperatureUnit>