package me.alexpetrakov.cyclone.units.domain.unitsofmeasure

import me.alexpetrakov.cyclone.units.domain.converters.Converter
import me.alexpetrakov.cyclone.units.domain.converters.LinearConverter
import me.alexpetrakov.cyclone.units.domain.converters.NothingConverter
import me.alexpetrakov.cyclone.units.domain.measurements.Measurement

class SpeedUnit(override val converter: Converter) : UnitDimension<SpeedUnit> {

    override val baseUnit: SpeedUnit
        get() = meterPerSecond

    companion object {

        val meterPerSecond = SpeedUnit(NothingConverter)

        val kilometerPerHour = SpeedUnit(object : Converter {
            override fun toBaseUnit(value: Double): Double {
                return value * 1000 / 3600
            }

            override fun fromBaseUnit(value: Double): Double {
                return value * 3600 / 1000
            }
        })

        val milePerHour = SpeedUnit(LinearConverter(0.447))
    }
}

typealias Speed = Measurement<SpeedUnit>
