package me.alexpetrakov.cyclone.weather.domain

import com.github.kittinunf.result.Result
import me.alexpetrakov.cyclone.locations.domain.Location

interface WeatherRepository {

    suspend fun getWeather(location: Location): Result<Weather, Throwable>
}