package me.alexpetrakov.cyclone.weather.domain.units

import me.alexpetrakov.cyclone.weather.domain.units.base.Converter
import me.alexpetrakov.cyclone.weather.domain.units.base.Measurement
import me.alexpetrakov.cyclone.weather.domain.units.base.UnitDimension

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