package me.alexpetrakov.cyclone.weather.domain.units

import me.alexpetrakov.cyclone.weather.domain.units.base.Converter
import me.alexpetrakov.cyclone.weather.domain.units.base.Measurement
import me.alexpetrakov.cyclone.weather.domain.units.base.UnitDimension

class SpeedUnit(override val converter: Converter) : UnitDimension<SpeedUnit> {

    override val baseUnit: SpeedUnit
        get() = meterPerSecond

    companion object {

        val meterPerSecond = SpeedUnit(NothingConverter)
    }
}

typealias Speed = Measurement<SpeedUnit>
