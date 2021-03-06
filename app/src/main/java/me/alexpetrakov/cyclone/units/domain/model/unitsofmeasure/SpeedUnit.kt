package me.alexpetrakov.cyclone.units.domain.model.unitsofmeasure

import me.alexpetrakov.cyclone.units.domain.model.converters.Converter
import me.alexpetrakov.cyclone.units.domain.model.converters.LinearConverter
import me.alexpetrakov.cyclone.units.domain.model.converters.NothingConverter
import me.alexpetrakov.cyclone.units.domain.model.measurements.Measurement

enum class SpeedUnit(
    override val symbol: String,
    override val converter: Converter
) : UnitDimension<SpeedUnit> {

    MetersPerSecond("m/s", NothingConverter),

    KilometersPerHour("km/h", LinearConverter(0.277778)),

    MilesPerHour("mph", LinearConverter(0.447));

    override val baseUnit: SpeedUnit
        get() = MetersPerSecond

    companion object {

        fun from(symbol: String): SpeedUnit? {
            return values().find { it.symbol == symbol }
        }
    }
}

typealias Speed = Measurement<SpeedUnit>
