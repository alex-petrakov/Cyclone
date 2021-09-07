package me.alexpetrakov.cyclone.units.domain.unitsofmeasure

import me.alexpetrakov.cyclone.units.domain.converters.Converter
import me.alexpetrakov.cyclone.units.domain.converters.NothingConverter
import me.alexpetrakov.cyclone.units.domain.measurements.Measurement

class TemperatureUnit(override val converter: Converter) : UnitDimension<TemperatureUnit> {

    override val baseUnit: TemperatureUnit
        get() = celsius

    companion object {

        val celsius = TemperatureUnit(NothingConverter)

        val fahrenheit = TemperatureUnit(object : Converter {
            override fun toBaseUnit(value: Double): Double {
                return value - 32 * 5 / 9
            }

            override fun fromBaseUnit(value: Double): Double {
                return value * 9 / 5 + 32
            }
        })

        val kelvin = TemperatureUnit(object : Converter {
            override fun toBaseUnit(value: Double): Double {
                return value - 273.15
            }

            override fun fromBaseUnit(value: Double): Double {
                return value + 273.15
            }
        })
    }
}

typealias Temperature = Measurement<TemperatureUnit>