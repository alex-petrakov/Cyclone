package me.alexpetrakov.cyclone.weather.data

import com.github.kittinunf.result.Result
import com.github.kittinunf.result.map
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import me.alexpetrakov.cyclone.locations.domain.model.Coordinates
import me.alexpetrakov.cyclone.weather.data.openweathermap.ForecastApi
import me.alexpetrakov.cyclone.weather.data.openweathermap.WeatherJson
import me.alexpetrakov.cyclone.weather.data.openweathermap.toDomain
import me.alexpetrakov.cyclone.weather.domain.model.Fail
import me.alexpetrakov.cyclone.weather.domain.model.Weather
import me.alexpetrakov.cyclone.weather.domain.repositories.WeatherRepository
import retrofit2.HttpException
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WeatherProvider @Inject constructor(
    private val forecastApi: ForecastApi
) : WeatherRepository {

    override suspend fun getWeather(coordinates: Coordinates): Result<Weather, Fail> {
        return Result.fromNetworkRequest {
            forecastApi.getWeather(
                coordinates.lat,
                coordinates.lon,
                getOpenWeatherCompatibleLanguageCode()
            )
        }.map { toDomainModel(it) }
    }

    private fun getOpenWeatherCompatibleLanguageCode(): String {
        val languageCode = when (val locale = Locale.getDefault()) {
            Locale.TRADITIONAL_CHINESE,
            Locale.SIMPLIFIED_CHINESE,
            Locale("pt", "BR") -> locale.toString()
            else -> locale.language
        }
        return if (languageCode.isNotEmpty()) languageCode else Locale.ENGLISH.language
    }

    private suspend fun toDomainModel(weatherJson: WeatherJson): Weather {
        return withContext(Dispatchers.Default) {
            weatherJson.toDomain()
        }
    }
}

private suspend fun <V> Result.Companion.fromNetworkRequest(request: suspend () -> V): Result<V, Fail> {
    return try {
        success(request())
    } catch (e: java.io.IOException) {
        failure(Fail.NoConnection(e))
    } catch (e: HttpException) {
        failure(Fail.NoConnection(e))
    }
}