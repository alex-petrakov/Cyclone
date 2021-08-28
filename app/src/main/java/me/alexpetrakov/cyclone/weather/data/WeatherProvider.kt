package me.alexpetrakov.cyclone.weather.data

import me.alexpetrakov.cyclone.weather.domain.*
import me.alexpetrakov.cyclone.weather.domain.units.*
import me.alexpetrakov.cyclone.weather.domain.units.PressureUnit.Companion.pascal
import java.time.LocalDateTime

class WeatherProvider : WeatherRepository {

    val weather = Weather(
        currentConditions = CurrentConditions(
            Temperature(17.0, TemperatureUnit.celsius),
            Temperature(18.0, TemperatureUnit.celsius),
            listOf(OverallConditions(801, "Clouds", "few clouds", Icon.SCATTERED_CLOUDS)),
            Pressure(747.0, pascal),
            67.0,
            Temperature(14.0, TemperatureUnit.celsius),
            1,
            Distance(10000.0, LengthUnit.meter),
            Wind(
                Speed(5.0, SpeedUnit.meterPerSecond),
                Speed(8.0, SpeedUnit.meterPerSecond),
                255.0
            )
        ),
        hourlyForecast = listOf(
            HourConditions(
                LocalDateTime.now().plusHours(1),
                Temperature(17.0, TemperatureUnit.celsius),
                listOf(OverallConditions(801, "Clouds", "few clouds", Icon.SCATTERED_CLOUDS)),
                8.0
            ),
            HourConditions(
                LocalDateTime.now().plusHours(2),
                Temperature(18.0, TemperatureUnit.celsius),
                listOf(OverallConditions(801, "Clouds", "few clouds", Icon.SCATTERED_CLOUDS)),
                0.0
            ),
            HourConditions(
                LocalDateTime.now().plusHours(3),
                Temperature(19.0, TemperatureUnit.celsius),
                listOf(OverallConditions(801, "Clouds", "few clouds", Icon.SCATTERED_CLOUDS)),
                0.0
            ),
            HourConditions(
                LocalDateTime.now().plusHours(4),
                Temperature(21.0, TemperatureUnit.celsius),
                listOf(OverallConditions(801, "Clouds", "few clouds", Icon.SCATTERED_CLOUDS)),
                0.0
            ),
            HourConditions(
                LocalDateTime.now().plusHours(5),
                Temperature(18.0, TemperatureUnit.celsius),
                listOf(OverallConditions(801, "Clouds", "few clouds", Icon.SCATTERED_CLOUDS)),
                0.0
            ),
            HourConditions(
                LocalDateTime.now().plusHours(6),
                Temperature(17.0, TemperatureUnit.celsius),
                listOf(OverallConditions(801, "Clouds", "few clouds", Icon.SCATTERED_CLOUDS)),
                5.0
            ),
            HourConditions(
                LocalDateTime.now().plusHours(7),
                Temperature(17.0, TemperatureUnit.celsius),
                listOf(OverallConditions(801, "Clouds", "few clouds", Icon.SCATTERED_CLOUDS)),
                18.0
            ),
            HourConditions(
                LocalDateTime.now().plusHours(8),
                Temperature(16.0, TemperatureUnit.celsius),
                listOf(OverallConditions(801, "Clouds", "few clouds", Icon.SCATTERED_CLOUDS)),
                36.0
            ),
            HourConditions(
                LocalDateTime.now().plusHours(9),
                Temperature(15.0, TemperatureUnit.celsius),
                listOf(OverallConditions(801, "Clouds", "few clouds", Icon.SCATTERED_CLOUDS)),
                40.0
            ),
        ),
        dailyForecast = listOf(
            DayConditions(
                LocalDateTime.now().plusDays(1),
                Temperature(23.0, TemperatureUnit.celsius),
                Temperature(17.0, TemperatureUnit.celsius),
                listOf(OverallConditions(801, "Clouds", "few clouds", Icon.SCATTERED_CLOUDS)),
                25.0
            ),
            DayConditions(
                LocalDateTime.now().plusDays(2),
                Temperature(20.0, TemperatureUnit.celsius),
                Temperature(16.0, TemperatureUnit.celsius),
                listOf(OverallConditions(801, "Clouds", "few clouds", Icon.SCATTERED_CLOUDS)),
                34.0
            ),
            DayConditions(
                LocalDateTime.now().plusDays(3),
                Temperature(21.0, TemperatureUnit.celsius),
                Temperature(15.0, TemperatureUnit.celsius),
                listOf(OverallConditions(801, "Clouds", "few clouds", Icon.SCATTERED_CLOUDS)),
                56.0
            ),
            DayConditions(
                LocalDateTime.now().plusDays(4),
                Temperature(22.0, TemperatureUnit.celsius),
                Temperature(18.0, TemperatureUnit.celsius),
                listOf(OverallConditions(801, "Clouds", "few clouds", Icon.SCATTERED_CLOUDS)),
                10.0
            ),
            DayConditions(
                LocalDateTime.now().plusDays(5),
                Temperature(24.0, TemperatureUnit.celsius),
                Temperature(19.0, TemperatureUnit.celsius),
                listOf(OverallConditions(801, "Clouds", "few clouds", Icon.SCATTERED_CLOUDS)),
                0.0
            ),
            DayConditions(
                LocalDateTime.now().plusDays(6),
                Temperature(26.0, TemperatureUnit.celsius),
                Temperature(20.0, TemperatureUnit.celsius),
                listOf(OverallConditions(801, "Clouds", "few clouds", Icon.SCATTERED_CLOUDS)),
                0.0
            ),
            DayConditions(
                LocalDateTime.now().plusDays(7),
                Temperature(21.0, TemperatureUnit.celsius),
                Temperature(17.0, TemperatureUnit.celsius),
                listOf(OverallConditions(801, "Clouds", "few clouds", Icon.SCATTERED_CLOUDS)),
                0.0
            ),
        )
    )

    override suspend fun getWeather(): Result<Weather> {
        return Result.success(weather)
    }
}