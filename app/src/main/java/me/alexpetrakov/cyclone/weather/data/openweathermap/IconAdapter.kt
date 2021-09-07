package me.alexpetrakov.cyclone.weather.data.openweathermap

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson

class IconAdapter {

    @FromJson
    fun fromJson(string: String): IconJson {
        return IconJson.from(string)
    }

    @ToJson
    fun toJson(icon: IconJson): String {
        return icon.label
    }
}