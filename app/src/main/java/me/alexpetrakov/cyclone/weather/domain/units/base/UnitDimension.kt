package me.alexpetrakov.cyclone.weather.domain.units.base

interface UnitDimension<U : UnitDimension<U>> {

    val baseUnit: U

    val converter: Converter
}