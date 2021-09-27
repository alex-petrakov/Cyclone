package me.alexpetrakov.cyclone.units.domain.unitsofmeasure

import me.alexpetrakov.cyclone.units.domain.converters.Converter
import me.alexpetrakov.cyclone.units.domain.converters.LinearConverter
import me.alexpetrakov.cyclone.units.domain.converters.NothingConverter
import me.alexpetrakov.cyclone.units.domain.measurements.Measurement

class LengthUnit(
    override val symbol: String,
    override val converter: Converter
) : UnitDimension<LengthUnit> {

    override val baseUnit: LengthUnit
        get() = meter

    companion object {

        val meter = LengthUnit("m", NothingConverter)

        val kilometer: LengthUnit = LengthUnit("km", LinearConverter(1000.0))

        val mile = LengthUnit("mi", LinearConverter(1609.344))
    }
}

typealias Length = Measurement<LengthUnit>
typealias Distance = Length