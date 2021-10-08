package me.alexpetrakov.cyclone.weather.domain

import com.github.kittinunf.result.Result
import com.github.kittinunf.result.flatMap
import me.alexpetrakov.cyclone.locations.domain.Coordinates
import me.alexpetrakov.cyclone.locations.domain.Location

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