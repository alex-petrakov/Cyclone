package me.alexpetrakov.cyclone.weather.domain.units.base

interface Converter {
    fun toBaseUnit(value: Double): Double

    fun fromBaseUnit(value: Double): Double
}