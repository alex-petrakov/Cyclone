package me.alexpetrakov.cyclone.common.data.adapters

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import me.alexpetrakov.cyclone.units.domain.unitsofmeasure.Distance
import me.alexpetrakov.cyclone.units.domain.unitsofmeasure.LengthUnit

class DistanceAdapter {

    @FromJson
    fun fromJson(value: Double): Distance = Distance(value, LengthUnit.Meters)

    @ToJson
    fun toJson(distance: Distance): Double = distance.convertTo(LengthUnit.Meters).value
}