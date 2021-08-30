package me.alexpetrakov.cyclone.weather.data

import kotlinx.coroutines.delay
import me.alexpetrakov.cyclone.weather.domain.*
import me.alexpetrakov.cyclone.weather.domain.units.*
import me.alexpetrakov.cyclone.weather.domain.units.PressureUnit.Companion.pascal
import java.time.ZonedDateTime
import kotlin.random.Random

class WeatherProvider : WeatherRepository {

    val weather = Weather(
        currentConditions = CurrentConditions(
            Temperature(17.0, TemperatureUnit.celsius),
            Temperature(18.0, TemperatureUnit.celsius),
            listOf(OverallConditions(801, "Clouds", "few clouds", Icon.SCATTERED_CLOUDS)),
            Pressure(747.0, pascal),
            0.67,
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
                ZonedDateTime.now().plusHours(1).toInstant(),
                Temperature(17.0, TemperatureUnit.celsius),
                listOf(OverallConditions(801, "Clouds", "few clouds", Icon.FEW_CLOUDS)),
                0.08
            ),
            HourConditions(
                ZonedDateTime.now().plusHours(2).toInstant(),
                Temperature(18.0, TemperatureUnit.celsius),
                listOf(OverallConditions(801, "Clouds", "few clouds", Icon.CLEAR)),
                0.0
            ),
            HourConditions(
                ZonedDateTime.now().plusHours(3).toInstant(),
                Temperature(19.0, TemperatureUnit.celsius),
                listOf(OverallConditions(801, "Clouds", "few clouds", Icon.SCATTERED_CLOUDS)),
                0.0
            ),
            HourConditions(
                ZonedDateTime.now().plusHours(4).toInstant(),
                Temperature(21.0, TemperatureUnit.celsius),
                listOf(OverallConditions(801, "Clouds", "few clouds", Icon.SCATTERED_CLOUDS)),
                0.0
            ),
            HourConditions(
                ZonedDateTime.now().plusHours(5).toInstant(),
                Temperature(18.0, TemperatureUnit.celsius),
                listOf(OverallConditions(801, "Clouds", "few clouds", Icon.SCATTERED_CLOUDS)),
                0.0
            ),
            HourConditions(
                ZonedDateTime.now().plusHours(6).toInstant(),
                Temperature(17.0, TemperatureUnit.celsius),
                listOf(OverallConditions(801, "Clouds", "few clouds", Icon.RAIN_SHOWER)),
                0.05
            ),
            HourConditions(
                ZonedDateTime.now().plusHours(7).toInstant(),
                Temperature(17.0, TemperatureUnit.celsius),
                listOf(OverallConditions(801, "Clouds", "few clouds", Icon.RAIN)),
                0.18
            ),
            HourConditions(
                ZonedDateTime.now().plusHours(8).toInstant(),
                Temperature(16.0, TemperatureUnit.celsius),
                listOf(OverallConditions(801, "Clouds", "few clouds", Icon.RAIN)),
                0.36
            ),
            HourConditions(
                ZonedDateTime.now().plusHours(9).toInstant(),
                Temperature(15.0, TemperatureUnit.celsius),
                listOf(OverallConditions(801, "Clouds", "few clouds", Icon.RAIN)),
                0.4
            ),
        ),
        dailyForecast = listOf(
            DayConditions(
                ZonedDateTime.now().plusDays(1).toInstant(),
                Temperature(23.0, TemperatureUnit.celsius),
                Temperature(17.0, TemperatureUnit.celsius),
                listOf(OverallConditions(801, "Clouds", "few clouds", Icon.RAIN)),
                0.25
            ),
            DayConditions(
                ZonedDateTime.now().plusDays(2).toInstant(),
                Temperature(20.0, TemperatureUnit.celsius),
                Temperature(16.0, TemperatureUnit.celsius),
                listOf(OverallConditions(801, "Clouds", "few clouds", Icon.RAIN)),
                0.34
            ),
            DayConditions(
                ZonedDateTime.now().plusDays(3).toInstant(),
                Temperature(21.0, TemperatureUnit.celsius),
                Temperature(15.0, TemperatureUnit.celsius),
                listOf(OverallConditions(801, "Clouds", "few clouds", Icon.THUNDERSTORM)),
                0.56
            ),
            DayConditions(
                ZonedDateTime.now().plusDays(4).toInstant(),
                Temperature(22.0, TemperatureUnit.celsius),
                Temperature(18.0, TemperatureUnit.celsius),
                listOf(OverallConditions(801, "Clouds", "few clouds", Icon.RAIN_SHOWER)),
                0.1
            ),
            DayConditions(
                ZonedDateTime.now().plusDays(5).toInstant(),
                Temperature(24.0, TemperatureUnit.celsius),
                Temperature(19.0, TemperatureUnit.celsius),
                listOf(OverallConditions(801, "Clouds", "few clouds", Icon.SCATTERED_CLOUDS)),
                0.0
            ),
            DayConditions(
                ZonedDateTime.now().plusDays(6).toInstant(),
                Temperature(26.0, TemperatureUnit.celsius),
                Temperature(20.0, TemperatureUnit.celsius),
                listOf(OverallConditions(801, "Clouds", "few clouds", Icon.SCATTERED_CLOUDS)),
                0.0
            ),
            DayConditions(
                ZonedDateTime.now().plusDays(7).toInstant(),
                Temperature(21.0, TemperatureUnit.celsius),
                Temperature(17.0, TemperatureUnit.celsius),
                listOf(OverallConditions(801, "Clouds", "few clouds", Icon.SCATTERED_CLOUDS)),
                0.0
            ),
        )
    )

    private val random = Random(System.currentTimeMillis())

    override suspend fun getWeather(): Result<Weather> {
        delay(3000L)
        return if (random.nextBoolean()) {
            Result.success(weather)
        } else {
            Result.failure(RuntimeException())
        }
    }
}