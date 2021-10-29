package me.alexpetrakov.cyclone.app.data.network.adapters

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import me.alexpetrakov.cyclone.units.domain.model.unitsofmeasure.Pressure
import me.alexpetrakov.cyclone.units.domain.model.unitsofmeasure.PressureUnit.Hectopascals

class PressureAdapter {

    @FromJson
    fun fromJson(value: Double): Pressure = Pressure(value, Hectopascals)

    @ToJson
    fun toJson(pressure: Pressure): Double = pressure.convertTo(Hectopascals).value
}