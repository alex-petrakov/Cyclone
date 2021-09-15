package me.alexpetrakov.cyclone.weather.domain

import com.github.kittinunf.result.Result
import me.alexpetrakov.cyclone.locations.domain.Coordinates

interface WeatherRepository {

    suspend fun getWeather(coordinates: Coordinates): Result<Weather, Fail>
}