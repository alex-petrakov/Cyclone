package me.alexpetrakov.cyclone.units.domain.model.unitsofmeasure

import me.alexpetrakov.cyclone.units.domain.model.converters.Converter
import me.alexpetrakov.cyclone.units.domain.model.converters.LinearConverter
import me.alexpetrakov.cyclone.units.domain.model.converters.NothingConverter
import me.alexpetrakov.cyclone.units.domain.model.measurements.Measurement

enum class PressureUnit(
    override val symbol: String,
    override val converter: Converter
) : UnitDimension<PressureUnit> {

    Pascals("Pa", NothingConverter),

    Hectopascals("hPa", LinearConverter(100.0)),

    Kilopascals("kPa", LinearConverter(1000.0)),

    Millibars("mbar", LinearConverter(100.0)),

    MillimetersOfMercury("mmHg", LinearConverter(133.322368)),

    InchesOfMercury("inHg", LinearConverter(3386.389));

    override val baseUnit: PressureUnit
        get() = Pascals

    companion object {

        fun from(symbol: String): PressureUnit? {
            return values().find { it.symbol == symbol }
        }
    }
}

typealias Pressure = Measurement<PressureUnit>