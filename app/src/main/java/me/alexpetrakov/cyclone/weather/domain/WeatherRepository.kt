package me.alexpetrakov.cyclone.weather.domain

import com.github.kittinunf.result.Result

interface WeatherRepository {

    suspend fun getWeather(): Result<Weather, Throwable>
}