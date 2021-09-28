package me.alexpetrakov.cyclone.units.domain.unitsofmeasure

import me.alexpetrakov.cyclone.units.domain.converters.Converter
import me.alexpetrakov.cyclone.units.domain.converters.LinearConverter
import me.alexpetrakov.cyclone.units.domain.converters.NothingConverter
import me.alexpetrakov.cyclone.units.domain.measurements.Measurement

enum class LengthUnit(
    override val symbol: String,
    override val converter: Converter
) : UnitDimension<LengthUnit> {

    Meters("m", NothingConverter),

    Kilometers("km", LinearConverter(1000.0)),

    Miles("mi", LinearConverter(1609.344));

    override val baseUnit: LengthUnit
        get() = Meters

    companion object {

        fun from(symbol: String): LengthUnit? {
            return values().find { it.symbol == symbol }
        }
    }
}

typealias Length = Measurement<LengthUnit>
typealias Distance = Length