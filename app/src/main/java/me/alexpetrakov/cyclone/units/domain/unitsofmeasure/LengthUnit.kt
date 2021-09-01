package me.alexpetrakov.cyclone.units.domain.unitsofmeasure

import me.alexpetrakov.cyclone.units.domain.converters.Converter
import me.alexpetrakov.cyclone.units.domain.converters.LinearConverter
import me.alexpetrakov.cyclone.units.domain.converters.NothingConverter
import me.alexpetrakov.cyclone.units.domain.measurements.Measurement

class LengthUnit(override val converter: Converter) : UnitDimension<LengthUnit> {

    override val baseUnit: LengthUnit
        get() = meter

    companion object {

        val meter = LengthUnit(NothingConverter)

        val kilometer: LengthUnit = LengthUnit(LinearConverter(1000.0))

        val mile = LengthUnit(LinearConverter(1609.344))
    }
}

typealias Length = Measurement<LengthUnit>
typealias Distance = Length