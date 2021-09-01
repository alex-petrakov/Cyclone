package me.alexpetrakov.cyclone.weather.domain.units

import me.alexpetrakov.cyclone.weather.domain.units.base.Converter
import me.alexpetrakov.cyclone.weather.domain.units.base.Measurement
import me.alexpetrakov.cyclone.weather.domain.units.base.UnitDimension

class PressureUnit(override val converter: Converter) : UnitDimension<PressureUnit> {
    override val baseUnit: PressureUnit
        get() = pascal

    companion object {

        val pascal = PressureUnit(NothingConverter)

        val hectopascal = PressureUnit(LinearConverter(100.0))

        val kilopascal = PressureUnit(LinearConverter(1000.0))

        val millibar = PressureUnit(LinearConverter(100.0))

        val millimeterOfMercury = PressureUnit(LinearConverter(133.322368))

        val inchOfMercury = PressureUnit(LinearConverter(3386.389))
    }
}

typealias Pressure = Measurement<PressureUnit>