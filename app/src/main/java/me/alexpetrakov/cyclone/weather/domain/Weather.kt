package me.alexpetrakov.cyclone.weather.domain

import me.alexpetrakov.cyclone.weather.domain.units.Distance
import me.alexpetrakov.cyclone.weather.domain.units.Pressure
import me.alexpetrakov.cyclone.weather.domain.units.Speed
import me.alexpetrakov.cyclone.weather.domain.units.Temperature
import java.time.LocalDateTime

data class Weather(
    val currentConditions: CurrentConditions,
    val hourlyForecast: List<HourConditions>,
    val dailyForecast: List<DayConditions>
)

data class CurrentConditions(
    val temperature: Temperature,
    val feelsLike: Temperature,
    val overallConditions: List<OverallConditions>,
    val pressure: Pressure,
    val humidity: Double,
    val dewPoint: Temperature,
    val uvIndex: Int,
    val visibility: Distance,
    val wind: Wind
)

data class OverallConditions(
    val id: Int,
    val title: String,
    val description: String,
    val icon: Icon
)

enum class Icon(val id: Int) {
    CLEAR(0),
    FEW_CLOUDS(1),
    SCATTERED_CLOUDS(2),
    BROKEN_CLOUDS(3),
    SHOWER_RAIN(4),
    RAIN(5),
    THUNDERSTORM(6),
    SNOW(7),
    MIST(8)
}

data class Wind(
    val speed: Speed,
    val gustsSpeed: Speed,
    val direction: Double
)

data class HourConditions(
    val dateTime: LocalDateTime,
    val temperature: Temperature,
    val overallConditions: List<OverallConditions>,
    val precipitationChance: Double
)

data class DayConditions(
    val dateTime: LocalDateTime,
    val tempHigh: Temperature,
    val tempLow: Temperature,
    val overallConditions: List<OverallConditions>,
    val precipitationChance: Double
)