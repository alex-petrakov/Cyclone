package me.alexpetrakov.cyclone.units.domain.unitsofmeasure

import me.alexpetrakov.cyclone.units.domain.converters.Converter
import me.alexpetrakov.cyclone.units.domain.converters.LinearConverter
import me.alexpetrakov.cyclone.units.domain.converters.NothingConverter
import me.alexpetrakov.cyclone.units.domain.measurements.Measurement

class PressureUnit(
    override val symbol: String,
    override val converter: Converter
) : UnitDimension<PressureUnit> {

    override val baseUnit: PressureUnit
        get() = pascal

    companion object {

        val pascal = PressureUnit("Pa", NothingConverter)

        val hectopascal = PressureUnit("hPa", LinearConverter(100.0))

        val kilopascal = PressureUnit("kPa", LinearConverter(1000.0))

        val millibar = PressureUnit("mbar", LinearConverter(100.0))

        val millimeterOfMercury = PressureUnit("mmHg", LinearConverter(133.322368))

        val inchOfMercury = PressureUnit("inHg", LinearConverter(3386.389))
    }
}

typealias Pressure = Measurement<PressureUnit>