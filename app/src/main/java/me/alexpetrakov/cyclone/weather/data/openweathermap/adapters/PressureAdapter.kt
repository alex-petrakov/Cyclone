package me.alexpetrakov.cyclone.weather.data.openweathermap.adapters

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import me.alexpetrakov.cyclone.units.domain.unitsofmeasure.Pressure
import me.alexpetrakov.cyclone.units.domain.unitsofmeasure.PressureUnit.Companion.hectopascal

class PressureAdapter {

    @FromJson
    fun fromJson(value: Double): Pressure = Pressure(value, hectopascal)

    @ToJson
    fun toJson(pressure: Pressure): Double = pressure.convertTo(hectopascal).value
}