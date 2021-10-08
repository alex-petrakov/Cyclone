package me.alexpetrakov.cyclone.units.domain.model.converters

interface Converter {
    fun toBaseUnit(value: Double): Double

    fun fromBaseUnit(value: Double): Double
}