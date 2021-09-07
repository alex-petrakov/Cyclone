package me.alexpetrakov.cyclone.weather.domain

import me.alexpetrakov.cyclone.units.domain.unitsofmeasure.Distance
import me.alexpetrakov.cyclone.units.domain.unitsofmeasure.Pressure
import me.alexpetrakov.cyclone.units.domain.unitsofmeasure.Speed
import me.alexpetrakov.cyclone.units.domain.unitsofmeasure.Temperature
import java.time.LocalDate
import java.time.LocalTime
import java.time.OffsetDateTime

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
    RAIN_SHOWER(4),
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
    val timestamp: OffsetDateTime,
    val temperature: Temperature,
    val overallConditions: List<OverallConditions>,
    val precipitationChance: Double
) {
    val localTime: LocalTime
        get() = timestamp.toLocalTime()
}

data class DayConditions(
    val timestamp: OffsetDateTime,
    val tempHigh: Temperature,
    val tempLow: Temperature,
    val overallConditions: List<OverallConditions>,
    val precipitationChance: Double
) {
    val localDate: LocalDate
        get() = timestamp.toLocalDate()
}