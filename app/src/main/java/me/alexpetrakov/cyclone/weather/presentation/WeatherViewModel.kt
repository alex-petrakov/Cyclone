package me.alexpetrakov.cyclone.weather.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import me.alexpetrakov.cyclone.R
import me.alexpetrakov.cyclone.common.asTextResource

class WeatherViewModel : ViewModel() {

    private val _viewState = liveData {
        val items = listOf(
            DisplayableItem.CurrentConditions(
                "17°".asTextResource(),
                "Cloudy".asTextResource(),
                "Feels like 18°".asTextResource(),
                R.drawable.ic_weather_cloudy,
                "E 1.0 m/s".asTextResource(),
                "747 mmHg".asTextResource(),
                "67%".asTextResource(),
                "14°".asTextResource(),
                "10 km".asTextResource(),
                "0".asTextResource()
            ),
            DisplayableItem.Header("Today".asTextResource()),
            DisplayableItem.HourlyForecast(
                (12..19).map { hour ->
                    HourConditions(
                        "$hour pm".asTextResource(),
                        "19°".asTextResource(),
                        "0%".asTextResource(),
                        hour % 2 == 0,
                        "Cloudy".asTextResource(),
                        R.drawable.ic_weather_cloudy
                    )
                }
            ),
            DisplayableItem.Header("This week".asTextResource()),
            DisplayableItem.DayConditions(
                "Aug 26".asTextResource(),
                "17°".asTextResource(),
                "25°".asTextResource(),
                "Cloudy".asTextResource(),
                R.drawable.ic_weather_cloudy,
                "38%".asTextResource(),
                true
            )
        )
        emit(ViewState(items))
    }

    val viewState get() = _viewState
}