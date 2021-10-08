package me.alexpetrakov.cyclone.weather.domain.interactors

import com.github.kittinunf.result.Result
import com.github.kittinunf.result.flatMap
import me.alexpetrakov.cyclone.locations.domain.model.Coordinates
import me.alexpetrakov.cyclone.locations.domain.model.Location
import me.alexpetrakov.cyclone.weather.domain.model.Fail
import me.alexpetrakov.cyclone.weather.domain.model.Weather
import me.alexpetrakov.cyclone.weather.domain.repositories.DeviceLocator
import me.alexpetrakov.cyclone.weather.domain.repositories.WeatherRepository

class WeatherInteractor(
    private val weatherRepo: WeatherRepository,
    private val locationRepo: DeviceLocator
) {

    suspend fun getWeather(location: Location): Result<Weather, Fail> {
        return location.loadCoordinates().flatMap { coordinates ->
            weatherRepo.getWeather(coordinates)
        }
    }

    private suspend fun Location.loadCoordinates(): Result<Coordinates, Fail> {
        return when (this) {
            Location.CurrentLocation -> locationRepo.getDeviceLocation()
            is Location.StoredLocation -> Result.success(this.coordinates)
        }
    }
}