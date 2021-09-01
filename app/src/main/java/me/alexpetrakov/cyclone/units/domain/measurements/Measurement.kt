package me.alexpetrakov.cyclone.units.domain.measurements

import me.alexpetrakov.cyclone.units.domain.unitsofmeasure.UnitDimension

data class Measurement<U : UnitDimension<U>>(val value: Double, val unit: U) {
    fun convertTo(otherUnit: U): Measurement<U> {
        if (unit == otherUnit) {
            return this.copy()
        }
        val valueInBaseUnits = unit.converter.toBaseUnit(value)
        val valueInTargetUnits = otherUnit.converter.fromBaseUnit(valueInBaseUnits)
        return Measurement(valueInTargetUnits, otherUnit)
    }
}