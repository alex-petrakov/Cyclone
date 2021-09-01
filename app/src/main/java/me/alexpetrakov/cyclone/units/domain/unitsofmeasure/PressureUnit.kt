package me.alexpetrakov.cyclone.units.domain.unitsofmeasure

import me.alexpetrakov.cyclone.units.domain.converters.Converter
import me.alexpetrakov.cyclone.units.domain.converters.LinearConverter
import me.alexpetrakov.cyclone.units.domain.converters.NothingConverter
import me.alexpetrakov.cyclone.units.domain.measurements.Measurement

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