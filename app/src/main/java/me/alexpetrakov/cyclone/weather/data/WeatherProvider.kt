package me.alexpetrakov.cyclone.weather.data

import com.github.kittinunf.result.Result
import com.github.kittinunf.result.map
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import me.alexpetrakov.cyclone.locations.domain.Location
import me.alexpetrakov.cyclone.weather.data.openweathermap.ForecastApi
import me.alexpetrakov.cyclone.weather.data.openweathermap.WeatherJson
import me.alexpetrakov.cyclone.weather.data.openweathermap.toDomain
import me.alexpetrakov.cyclone.weather.domain.Weather
import me.alexpetrakov.cyclone.weather.domain.WeatherRepository
import retrofit2.HttpException

class WeatherProvider(
    private val forecastApi: ForecastApi
) : WeatherRepository {

    override suspend fun getWeather(location: Location): Result<Weather, Throwable> {
        val (lat, lon) = when (location) {
            Location.CurrentLocation -> TODO("Not implemented")
            is Location.StoredLocation -> location.coordinates
        }
        return Result.fromNetworkRequest { forecastApi.getWeather(lat, lon) }
            .map { toDomainModel(it) }
    }

    private suspend fun toDomainModel(weatherJson: WeatherJson): Weather {
        return withContext(Dispatchers.Default) {
            weatherJson.toDomain()
        }
    }
}

private suspend fun <V> Result.Companion.fromNetworkRequest(request: suspend () -> V): Result<V, Throwable> {
    return try {
        success(request())
    } catch (e: java.io.IOException) {
        failure(e)
    } catch (e: HttpException) {
        failure(e)
    }
}