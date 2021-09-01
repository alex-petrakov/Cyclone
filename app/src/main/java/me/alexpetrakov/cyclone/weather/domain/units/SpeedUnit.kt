package me.alexpetrakov.cyclone.weather.domain.units

import me.alexpetrakov.cyclone.weather.domain.units.base.Converter
import me.alexpetrakov.cyclone.weather.domain.units.base.Measurement
import me.alexpetrakov.cyclone.weather.domain.units.base.UnitDimension

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
