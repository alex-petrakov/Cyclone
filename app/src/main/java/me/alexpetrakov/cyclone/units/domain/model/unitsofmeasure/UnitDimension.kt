package me.alexpetrakov.cyclone.units.domain.model.unitsofmeasure

import me.alexpetrakov.cyclone.units.domain.model.converters.Converter

interface UnitDimension<U : UnitDimension<U>> {

    val symbol: String

    val baseUnit: U

    val converter: Converter
}