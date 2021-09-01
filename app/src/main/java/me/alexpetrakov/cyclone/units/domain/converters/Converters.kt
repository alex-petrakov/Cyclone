package me.alexpetrakov.cyclone.units.domain.converters

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