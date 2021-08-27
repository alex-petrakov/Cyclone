package me.alexpetrakov.cyclone.weather.domain

interface WeatherRepository {

    suspend fun getWeather(): Result<Weather>
}