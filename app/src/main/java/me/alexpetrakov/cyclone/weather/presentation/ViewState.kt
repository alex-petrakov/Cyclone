package me.alexpetrakov.cyclone.weather.presentation

import androidx.annotation.DrawableRes
import androidx.recyclerview.widget.DiffUtil
import me.alexpetrakov.cyclone.common.TextResource

data class ViewState(val items: List<DisplayableItem>)

sealed class DisplayableItem {
    data class Header(val text: TextResource) : DisplayableItem()
    data class CurrentConditions(
        val temperature: TextResource,
        val conditions: TextResource,
        val feelsLikeTemperature: TextResource,
        @DrawableRes val conditionsIconRes: Int,
        val wind: TextResource,
        val pressure: TextResource,
        val humidity: TextResource,
        val dewPoint: TextResource,
        val visibility: TextResource,
        val uvIndex: TextResource
    ) : DisplayableItem()

    data class DayConditions(
        val date: TextResource,
        val temperatureLow: TextResource,
        val temperatureHigh: TextResource,
        val conditions: TextResource,
        @DrawableRes val conditionsIconRes: Int,
        val precipitationChance: TextResource,
        val precipitationChanceIsVisible: Boolean
    ) : DisplayableItem()

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