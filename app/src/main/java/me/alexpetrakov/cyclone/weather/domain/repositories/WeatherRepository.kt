package me.alexpetrakov.cyclone.weather.domain.repositories

import com.github.kittinunf.result.Result
import me.alexpetrakov.cyclone.locations.domain.model.Coordinates
import me.alexpetrakov.cyclone.weather.domain.model.Fail
import me.alexpetrakov.cyclone.weather.domain.model.Weather

interface WeatherRepository {

    suspend fun getWeather(coordinates: Coordinates): Result<Weather, Fail>
}