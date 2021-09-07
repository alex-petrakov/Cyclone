package me.alexpetrakov.cyclone.weather.data.openweathermap

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import me.alexpetrakov.cyclone.units.domain.unitsofmeasure.*
import me.alexpetrakov.cyclone.weather.domain.*
import java.time.Instant
import java.time.ZoneOffset
import kotlin.math.roundToInt

@JsonClass(generateAdapter = true)
data class WeatherJson(
    @Json(name = "timezone_offset") val timeZoneOffset: Int,
    @Json(name = "current") val currentConditions: CurrentJson,
    @Json(name = "hourly") val hourlyForecast: List<HourConditionsJson>,
    @Json(name = "daily") val dailyForecast: List<DayConditionsJson>
)

@JsonClass(generateAdapter = true)
data class CurrentJson(
    @Json(name = "dt") val currentTimestamp: Instant,
    @Json(name = "sunrise") val sunriseTimestamp: Instant,
    @Json(name = "sunset") val sunsetTimestamp: Instant,
    @Json(name = "temp") val temperature: Double,
    @Json(name = "feels_like") val feelsLike: Double,
    @Json(name = "pressure") val pressure: Double,
    @Json(name = "humidity") val humidity: Double,
    @Json(name = "dew_point") val dewPoint: Double,
    @Json(name = "uvi") val uvIndex: Double,
    @Json(name = "visibility") val visibility: Double,
    @Json(name = "wind_speed") val windSpeed: Double,
    @Json(name = "wind_deg") val windDirectionInDeg: Double,
    @Json(name = "wind_gust") val windGust: Double,
    @Json(name = "weather") val overallConditions: List<OverallConditionsJson>
)

@JsonClass(generateAdapter = true)
data class OverallConditionsJson(
    @Json(name = "id") val id: Int,
    @Json(name = "main") val title: String,
    @Json(name = "description") val description: String,
    @Json(name = "icon") val icon: IconJson
)

@JsonClass(generateAdapter = false)
enum class IconJson(val label: String) {
    CLEAR("01d"),
    FEW_CLOUDS("02d"),
    SCATTERED_CLOUDS("03d"),
    BROKEN_CLOUDS("04d"),
    RAIN_SHOWER("09d"),
    RAIN("10d"),
    THUNDERSTORM("11d"),
    SNOW("13d"),
    MIST("50d"),
    NIGHT_CLEAR("01n"),
    NIGHT_FEW_CLOUDS("02n"),
    NIGHT_SCATTERED_CLOUDS("03n"),
    NIGHT_BROKEN_CLOUDS("04n"),
    NIGHT_RAIN_SHOWER("09n"),
    NIGHT_RAIN("10n"),
    NIGHT_THUNDERSTORM("11n"),
    NIGHT_SNOW("13n"),
    NIGHT_MIST("50n");

    companion object {
        fun from(label: String): IconJson {
            return values().find { it.label == label }
                ?: throw IllegalArgumentException("Unknown icon label: $label")
        }
    }
}

@JsonClass(generateAdapter = true)
data class HourConditionsJson(
    @Json(name = "dt") val timestamp: Instant,
    @Json(name = "temp") val temperature: Double,
    @Json(name = "weather") val overallConditions: List<OverallConditionsJson>,
    @Json(name = "pop") val precipitationChance: Double
)

@JsonClass(generateAdapter = true)
data class DayConditionsJson(
    @Json(name = "dt") val timestamp: Instant,
    @Json(name = "temp") val temperature: TemperatureJson,
    @Json(name = "weather") val overallConditions: List<OverallConditionsJson>,
    @Json(name = "pop") val precipitationChance: Double
)

@JsonClass(generateAdapter = true)
data class TemperatureJson(
    @Json(name = "min") val min: Double,
    @Json(name = "max") val max: Double
)

fun WeatherJson.unwrap(): Weather {
    return Weather(
        currentConditions.unwrap(),
        hourlyForecast.asSequence()
            .sortedBy { it.timestamp }
            .drop(1) // The first item is always the current hour, and we don't need it
            .take(12) // We only display 12 hour forecast
            .map { it.unwrap(ZoneOffset.ofTotalSeconds(timeZoneOffset)) }
            .toList(),
        dailyForecast.asSequence()
            .sortedBy { it.timestamp }
            .drop(1) // The first item is always the current day, and we don't need it
            .map { it.unwrap(ZoneOffset.ofTotalSeconds(timeZoneOffset)) }
            .toList()
    )
}

private fun DayConditionsJson.unwrap(zoneOffset: ZoneOffset): DayConditions {
    return DayConditions(
        timestamp.atOffset(zoneOffset),
        Temperature(temperature.max, TemperatureUnit.kelvin),
        Temperature(temperature.min, TemperatureUnit.kelvin),
        overallConditions.map { it.unwrap() },
        precipitationChance
    )
}

private fun HourConditionsJson.unwrap(zoneOffset: ZoneOffset): HourConditions {
    return HourConditions(
        timestamp.atOffset(zoneOffset),
        Temperature(temperature, TemperatureUnit.kelvin),
        overallConditions.map { it.unwrap() },
        precipitationChance
    )
}

private fun CurrentJson.unwrap(): CurrentConditions {
    return CurrentConditions(
        Temperature(temperature, TemperatureUnit.kelvin),
        Temperature(feelsLike, TemperatureUnit.kelvin),
        overallConditions.map { it.unwrap() },
        Pressure(pressure, PressureUnit.hectopascal),
        humidity / 100.0,
        Temperature(dewPoint, TemperatureUnit.kelvin),
        uvIndex.roundToInt(),
        Distance(visibility, LengthUnit.meter),
        Wind(
            Speed(windSpeed, SpeedUnit.meterPerSecond),
            Speed(windGust, SpeedUnit.meterPerSecond),
            windDirectionInDeg
        )
    )
}

private fun OverallConditionsJson.unwrap(): OverallConditions {
    return OverallConditions(id, title, description, icon.unwrap())
}

private fun IconJson.unwrap(): Icon {
    return when (this) {
        IconJson.CLEAR -> Icon.CLEAR
        IconJson.FEW_CLOUDS -> Icon.FEW_CLOUDS
        IconJson.SCATTERED_CLOUDS -> Icon.SCATTERED_CLOUDS
        IconJson.BROKEN_CLOUDS -> Icon.BROKEN_CLOUDS
        IconJson.RAIN_SHOWER -> Icon.RAIN_SHOWER
        IconJson.RAIN -> Icon.RAIN
        IconJson.THUNDERSTORM -> Icon.THUNDERSTORM
        IconJson.SNOW -> Icon.SNOW
        IconJson.MIST -> Icon.MIST
        IconJson.NIGHT_CLEAR -> Icon.CLEAR
        IconJson.NIGHT_FEW_CLOUDS -> Icon.FEW_CLOUDS
        IconJson.NIGHT_SCATTERED_CLOUDS -> Icon.SCATTERED_CLOUDS
        IconJson.NIGHT_BROKEN_CLOUDS -> Icon.BROKEN_CLOUDS
        IconJson.NIGHT_RAIN_SHOWER -> Icon.RAIN_SHOWER
        IconJson.NIGHT_RAIN -> Icon.RAIN
        IconJson.NIGHT_THUNDERSTORM -> Icon.THUNDERSTORM
        IconJson.NIGHT_SNOW -> Icon.SNOW
        IconJson.NIGHT_MIST -> Icon.MIST
    }
}


