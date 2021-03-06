package me.alexpetrakov.cyclone.app.data.network.adapters

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import me.alexpetrakov.cyclone.units.domain.model.unitsofmeasure.Temperature
import me.alexpetrakov.cyclone.units.domain.model.unitsofmeasure.TemperatureUnit.Kelvin

class TemperatureAdapter {

    @FromJson
    fun fromJson(value: Double): Temperature = Temperature(value, Kelvin)

    @ToJson
    fun toJson(temperature: Temperature): Double = temperature.convertTo(Kelvin).value
}