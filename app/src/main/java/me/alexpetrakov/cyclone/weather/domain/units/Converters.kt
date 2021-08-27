package me.alexpetrakov.cyclone.weather.domain.units

import me.alexpetrakov.cyclone.weather.domain.units.base.Converter

object NothingConverter : Converter {
    override fun toBaseUnit(value: Double): Double {
        return value
    }

    override fun fromBaseUnit(value: Double): Double {
        return value
    }
}

data class LinearConverter(val coefficient: Double) : Converter {
    override fun toBaseUnit(value: Double): Double {
        return value * coefficient
    }

    override fun fromBaseUnit(value: Double): Double {
        return value / coefficient
    }
}