package me.alexpetrakov.cyclone.weather.presentation

import androidx.annotation.DrawableRes
import androidx.recyclerview.widget.DiffUtil
import me.alexpetrakov.cyclone.R
import me.alexpetrakov.cyclone.common.presentation.TextResource
import java.time.OffsetDateTime

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

    data class HeaderUi(val id: Int, val text: TextResource) : DisplayableItem() {
        override fun hasSameIdentityWith(other: DisplayableItem): Boolean {
            return other is HeaderUi && this.id == other.id
        }

        override fun hasSameContentWith(other: DisplayableItem) = this == other

        companion object {
            const val TODAY_ID = 0
            const val THIS_WEEK_ID = 1
        }
    }

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
    ) : DisplayableItem() {

        // We assume that we have only one such item in the list at any given time
        override fun hasSameIdentityWith(other: DisplayableItem) = other is CurrentConditionsUi

        override fun hasSameContentWith(other: DisplayableItem) = this == other
    }

    data class DayConditionsUi(
        val rawDate: OffsetDateTime,
        val date: TextResource,
        val temperatureLow: TextResource,
        val temperatureHigh: TextResource,
        val conditions: TextResource,
        val icon: IconUi,
        val precipitationChance: TextResource,
        val precipitationChanceIsVisible: Boolean
    ) : DisplayableItem() {

        override fun hasSameIdentityWith(other: DisplayableItem): Boolean {
            return other is DayConditionsUi && this.rawDate == other.rawDate
        }

        override fun hasSameContentWith(other: DisplayableItem) = this == other
    }

    data class HourlyForecastUi(val hourConditions: List<HourConditionsUi>) : DisplayableItem() {

        // We assume that we have only one such item in the list at any given time
        override fun hasSameIdentityWith(other: DisplayableItem) = other is HourlyForecastUi

        override fun hasSameContentWith(other: DisplayableItem) = this == other
    }

    object DataProviderNotice : DisplayableItem() {

        // We assume that we have only one such item in the list at any given time
        override fun hasSameIdentityWith(other: DisplayableItem) = other is DataProviderNotice

        override fun hasSameContentWith(other: DisplayableItem) = true
    }

    abstract fun hasSameIdentityWith(other: DisplayableItem): Boolean

    abstract fun hasSameContentWith(other: DisplayableItem): Boolean

    object DiffCallback : DiffUtil.ItemCallback<DisplayableItem>() {
        override fun areItemsTheSame(old: DisplayableItem, new: DisplayableItem): Boolean {
            return old.hasSameIdentityWith(new)
        }

        override fun areContentsTheSame(old: DisplayableItem, new: DisplayableItem): Boolean {
            return old.hasSameContentWith(new)
        }
    }
}

data class HourConditionsUi(
    val rawTime: OffsetDateTime,
    val time: TextResource,
    val temperature: TextResource,
    val precipitationChance: TextResource,
    val precipitationChanceIsVisible: Boolean,
    val conditions: TextResource,
    val icon: IconUi
) {
    object DiffCallback : DiffUtil.ItemCallback<HourConditionsUi>() {
        override fun areItemsTheSame(old: HourConditionsUi, new: HourConditionsUi): Boolean {
            return old.rawTime == new.rawTime
        }

        override fun areContentsTheSame(old: HourConditionsUi, new: HourConditionsUi): Boolean {
            return old == new
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