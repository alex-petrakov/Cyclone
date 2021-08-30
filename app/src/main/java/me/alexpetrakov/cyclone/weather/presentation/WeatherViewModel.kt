package me.alexpetrakov.cyclone.weather.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import me.alexpetrakov.cyclone.R
import me.alexpetrakov.cyclone.common.TextResource
import me.alexpetrakov.cyclone.common.asTextResource
import me.alexpetrakov.cyclone.weather.domain.*
import java.text.NumberFormat
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*

class WeatherViewModel(private val weatherRepository: WeatherRepository) : ViewModel() {

    private val _viewState = liveData {
        weatherRepository.getWeather().fold(
            { weather -> emit(ViewState(weather.toUiModel())) },
            { TODO("Not implemented") }
        )
    }

    val viewState get() = _viewState

    private val dateFormatter = DateTimeFormatter.ofPattern("EEEE d")

    private val timeFormatter = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)

    private val percentFormatter = NumberFormat.getPercentInstance().apply {
        minimumFractionDigits = 0
        maximumFractionDigits = 0
    }

    private fun Weather.toUiModel(): List<DisplayableItem> {
        return listOf(
            currentConditions.toUiModel(),
            DisplayableItem.HeaderUi(TextResource.from(R.string.weather_today_title)),
            hourlyForecast.toUiModel(),
            DisplayableItem.HeaderUi(TextResource.from(R.string.weather_this_week_title)),
        ) + dailyForecast.toUiModel()
    }

    private fun CurrentConditions.toUiModel(): DisplayableItem.CurrentConditionsUi {
        return DisplayableItem.CurrentConditionsUi(
            temperature.value.toString().asTextResource(),
            overallConditions[0].title.asTextResource(),
            temperature.value.toString().asTextResource(),
            overallConditions[0].icon.toUiModel(),
            "${wind.speed.value}".asTextResource(),
            "${pressure.value}".asTextResource(),
            percentFormatter.format(humidity).asTextResource(),
            "${dewPoint.value}".asTextResource(),
            "${visibility.value}".asTextResource(),
            "$uvIndex".asTextResource()
        )
    }

    private fun List<HourConditions>.toUiModel(): DisplayableItem.HourlyForecastUi {
        return DisplayableItem.HourlyForecastUi(map { it.toUiModel() })
    }

    private fun HourConditions.toUiModel(): HourConditionsUi {
        return HourConditionsUi(
            timeFormatter.format(localTime).asTextResource(),
            temperature.value.toString().asTextResource(),
            percentFormatter.format(precipitationChance).asTextResource(),
            precipitationChance > 0,
            overallConditions[0].title.asTextResource(),
            overallConditions[0].icon.toUiModel()
        )
    }

    private fun List<DayConditions>.toUiModel(): List<DisplayableItem.DayConditionsUi> {
        return map { it.toUiModel() }
    }

    private fun DayConditions.toUiModel(): DisplayableItem.DayConditionsUi {
        return DisplayableItem.DayConditionsUi(
            dateFormatter.format(localDate).capitalizingFirstLetter().asTextResource(),
            tempLow.value.toString().asTextResource(),
            tempHigh.value.toString().asTextResource(),
            overallConditions[0].title.asTextResource(),
            overallConditions[0].icon.toUiModel(),
            percentFormatter.format(precipitationChance).asTextResource(),
            precipitationChance > 0
        )
    }

    private fun Icon.toUiModel(): IconUi {
        return when (this) {
            Icon.CLEAR -> IconUi.CLEAR
            Icon.FEW_CLOUDS -> IconUi.FEW_CLOUDS
            Icon.SCATTERED_CLOUDS -> IconUi.SCATTERED_CLOUDS
            Icon.BROKEN_CLOUDS -> IconUi.BROKEN_CLOUDS
            Icon.RAIN_SHOWER -> IconUi.RAIN_SHOWER
            Icon.RAIN -> IconUi.RAIN
            Icon.THUNDERSTORM -> IconUi.THUNDERSTORM
            Icon.SNOW -> IconUi.SNOW
            Icon.MIST -> IconUi.MIST
        }
    }
}

private fun String.capitalizingFirstLetter(): String {
    return this.replaceFirstChar { char ->
        if (char.isLowerCase()) {
            char.titlecase(Locale.getDefault())
        } else {
            char.toString()
        }
    }
}