package me.alexpetrakov.cyclone.weather.domain

import com.github.kittinunf.result.Result
import com.github.kittinunf.result.flatMap
import me.alexpetrakov.cyclone.locations.domain.Coordinates
import me.alexpetrakov.cyclone.locations.domain.Location

class GetWeather(
    private val weatherRepo: WeatherRepository,
    private val locationRepo: DeviceLocator
) {

    suspend operator fun invoke(location: Location): Result<Weather, Throwable> {
        return location.loadCoordinates().flatMap { coordinates ->
            weatherRepo.getWeather(coordinates)
        }
    }

    private suspend fun Location.loadCoordinates(): Result<Coordinates, Throwable> {
        return when (this) {
            Location.CurrentLocation -> locationRepo.getDeviceLocation()
            is Location.StoredLocation -> Result.success(this.coordinates)
        }
    }
}