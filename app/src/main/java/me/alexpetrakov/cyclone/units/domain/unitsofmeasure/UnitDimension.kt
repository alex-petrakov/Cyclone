package me.alexpetrakov.cyclone.units.domain.unitsofmeasure

import me.alexpetrakov.cyclone.units.domain.converters.Converter

interface UnitDimension<U : UnitDimension<U>> {

    val symbol: String

    val baseUnit: U

    val converter: Converter
}