package me.alexpetrakov.cyclone.weather.data.openweathermap

import retrofit2.http.GET
import retrofit2.http.Query

interface ForecastApi {

    @GET("data/2.5/onecall?exclude=minutely,alerts")
    suspend fun getWeather(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double
    ): WeatherJson
}