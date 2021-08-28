package me.alexpetrakov.cyclone.weather.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import me.alexpetrakov.cyclone.R
import me.alexpetrakov.cyclone.common.TextResource
import me.alexpetrakov.cyclone.common.asTextResource
import me.alexpetrakov.cyclone.weather.domain.CurrentConditions
import me.alexpetrakov.cyclone.weather.domain.Weather
import me.alexpetrakov.cyclone.weather.domain.WeatherRepository

class WeatherViewModel(private val weatherRepository: WeatherRepository) : ViewModel() {

    private val _viewState = liveData {
        weatherRepository.getWeather().fold(
            { weather -> emit(ViewState(weather.toUiModel())) },
            { TODO("Not implemented") }
        )
    }

    val viewState get() = _viewState

    private fun Weather.toUiModel(): List<DisplayableItem> {
        return listOf(
            currentConditions.toUiModel(),
            DisplayableItem.Header(TextResource.from("Today")),
            DisplayableItem.Header(TextResource.from("This week"))
        )
    }

    private fun CurrentConditions.toUiModel(): DisplayableItem.CurrentConditions {
        return DisplayableItem.CurrentConditions(
            temperature.value.toString().asTextResource(),
            overallConditions[0].title.asTextResource(),
            temperature.value.toString().asTextResource(),
            R.drawable.ic_weather_cloudy,
            "${wind.speed.value}".asTextResource(),
            "${pressure.value}".asTextResource(),
            "$humidity".asTextResource(),
            "${dewPoint.value}".asTextResource(),
            "${visibility.value}".asTextResource(),
            "$uvIndex".asTextResource()
        )
    }
}