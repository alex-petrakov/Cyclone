package me.alexpetrakov.cyclone.weather.presentation

import androidx.annotation.DrawableRes
import androidx.recyclerview.widget.DiffUtil
import me.alexpetrakov.cyclone.R
import me.alexpetrakov.cyclone.common.presentation.TextResource

data class ToolbarViewState(val title: TextResource)

sealed class WeatherViewState {

    object Loading : WeatherViewState()

    sealed class Error : WeatherViewState() {
        object NoLocationAccess : Error()
        object NoAvailableLocation : Error()
        object NoConnection : Error()
    }

    data class Content(
        val isRefreshing: Boolean,
        val items: List<DisplayableItem>
    ) : WeatherViewState()
}

sealed class DisplayableItem {
    data class HeaderUi(val text: TextResource) : DisplayableItem()

    data class CurrentConditionsUi(
        val temperature: TextResource,
        val conditions: TextResource,
        val feelsLikeTemperature: TextResource,
        val icon: IconUi,
        val wind: TextResource,
        val pressure: TextResource,
        val humidityAndDewPoint: TextResource,
        val visibility: TextResource,
        val uvIndex: TextResource
    ) : DisplayableItem()

    data class DayConditionsUi(
        val date: TextResource,
        val temperatureLow: TextResource,
        val temperatureHigh: TextResource,
        val conditions: TextResource,
        val icon: IconUi,
        val precipitationChance: TextResource,
        val precipitationChanceIsVisible: Boolean
    ) : DisplayableItem()

    data class HourlyForecastUi(val hourConditions: List<HourConditionsUi>) : DisplayableItem()

    object DataProviderNotice : DisplayableItem()

    // TODO: Implement DiffUtil.ItemCallback
    object DiffCallback : DiffUtil.ItemCallback<DisplayableItem>() {
        override fun areItemsTheSame(oldItem: DisplayableItem, newItem: DisplayableItem): Boolean {
            return false
        }

        override fun areContentsTheSame(
            oldItem: DisplayableItem,
            newItem: DisplayableItem
        ): Boolean {
            return false
        }
    }
}

data class HourConditionsUi(
    val time: TextResource,
    val temperature: TextResource,
    val precipitationChance: TextResource,
    val precipitationChanceIsVisible: Boolean,
    val conditions: TextResource,
    val icon: IconUi
) {
    // TODO: Implement DiffUtil.ItemCallback
    object DiffCallback : DiffUtil.ItemCallback<HourConditionsUi>() {
        override fun areItemsTheSame(
            oldItem: HourConditionsUi,
            newItem: HourConditionsUi
        ): Boolean {
            return false
        }

        override fun areContentsTheSame(
            oldItem: HourConditionsUi,
            newItem: HourConditionsUi
        ): Boolean {
            return false
        }
    }
}

enum class IconUi(@DrawableRes val resId: Int) {
    CLEAR(R.drawable.ic_weather_clear),
    FEW_CLOUDS(R.drawable.ic_weather_partly_cloudy),
    SCATTERED_CLOUDS(R.drawable.ic_weather_cloudy),
    BROKEN_CLOUDS(R.drawable.ic_weather_cloudy),
    RAIN_SHOWER(R.drawable.ic_weather_rain_shower),
    RAIN(R.drawable.ic_weather_rain),
    THUNDERSTORM(R.drawable.ic_weather_thunderstorm),
    SNOW(R.drawable.ic_weather_snow),
    MIST(R.drawable.ic_weather_mist)
}