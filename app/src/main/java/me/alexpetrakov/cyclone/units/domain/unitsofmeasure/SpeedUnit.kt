package me.alexpetrakov.cyclone.units.domain.unitsofmeasure

import me.alexpetrakov.cyclone.units.domain.converters.Converter
import me.alexpetrakov.cyclone.units.domain.converters.LinearConverter
import me.alexpetrakov.cyclone.units.domain.converters.NothingConverter
import me.alexpetrakov.cyclone.units.domain.measurements.Measurement

enum class SpeedUnit(
    override val symbol: String,
    override val converter: Converter
) : UnitDimension<SpeedUnit> {

    MetersPerSecond("m/s", NothingConverter),

    KilometersPerHour("km/h", object : Converter {
        override fun toBaseUnit(value: Double): Double {
            return value * 1000 / 3600
        }

        override fun fromBaseUnit(value: Double): Double {
            return value * 3600 / 1000
        }
    }),

    MilesPerHour("mph", LinearConverter(0.447));

    override val baseUnit: SpeedUnit
        get() = MetersPerSecond

    companion object {

        fun from(symbol: String): SpeedUnit {
            return values().find { it.symbol == symbol }
                ?: throw IllegalArgumentException("Unknown speed unit symbol: $symbol")
        }
    }
}

typealias Speed = Measurement<SpeedUnit>
