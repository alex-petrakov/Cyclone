package me.alexpetrakov.cyclone.common.data.adapters

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import me.alexpetrakov.cyclone.units.domain.unitsofmeasure.Temperature
import me.alexpetrakov.cyclone.units.domain.unitsofmeasure.TemperatureUnit.Kelvin

class TemperatureAdapter {

    @FromJson
    fun fromJson(value: Double): Temperature = Temperature(value, Kelvin)

    @ToJson
    fun toJson(temperature: Temperature): Double = temperature.convertTo(Kelvin).value
}