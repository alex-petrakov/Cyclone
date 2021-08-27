package me.alexpetrakov.cyclone.weather.domain.units

import me.alexpetrakov.cyclone.weather.domain.units.base.Converter
import me.alexpetrakov.cyclone.weather.domain.units.base.Measurement
import me.alexpetrakov.cyclone.weather.domain.units.base.UnitDimension

class PressureUnit(override val converter: Converter) : UnitDimension<PressureUnit> {
    override val baseUnit: PressureUnit
        get() = pascal

    companion object {

        val pascal = PressureUnit(NothingConverter)
    }
}

typealias Pressure = Measurement<PressureUnit>