package me.alexpetrakov.cyclone.common.data.adapters

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import me.alexpetrakov.cyclone.units.domain.unitsofmeasure.Speed
import me.alexpetrakov.cyclone.units.domain.unitsofmeasure.SpeedUnit

class SpeedAdapter {

    @FromJson
    fun fromJson(value: Double): Speed = Speed(value, SpeedUnit.MetersPerSecond)

    @ToJson
    fun toJson(speed: Speed): Double = speed.convertTo(SpeedUnit.MetersPerSecond).value
}