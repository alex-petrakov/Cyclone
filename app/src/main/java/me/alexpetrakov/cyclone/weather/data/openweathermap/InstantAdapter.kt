package me.alexpetrakov.cyclone.weather.data.openweathermap

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import java.time.Instant

class InstantAdapter {

    @ToJson
    fun toJson(instant: Instant): Long = instant.epochSecond

    @FromJson
    fun fromJson(long: Long): Instant = Instant.ofEpochSecond(long)
}