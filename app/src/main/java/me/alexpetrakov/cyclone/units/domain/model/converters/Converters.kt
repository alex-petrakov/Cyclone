package me.alexpetrakov.cyclone.units.domain.model.converters

object NothingConverter : Converter {
    override fun toBaseUnit(value: Double): Double {
        return value
    }

    override fun fromBaseUnit(value: Double): Double {
        return value
    }
}

data class LinearConverter(val coefficient: Double, val constant: Double = .0) : Converter {
    override fun toBaseUnit(value: Double): Double {
        return value * coefficient + constant
    }

    override fun fromBaseUnit(value: Double): Double {
        return (value - constant) / coefficient
    }
}