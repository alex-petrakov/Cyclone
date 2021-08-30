package me.alexpetrakov.cyclone.weather.presentation

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import me.alexpetrakov.cyclone.R
import me.alexpetrakov.cyclone.common.asString
import me.alexpetrakov.cyclone.databinding.ItemCurrentConditionsBinding
import me.alexpetrakov.cyclone.databinding.ItemDayConditionsBinding
import me.alexpetrakov.cyclone.databinding.ItemHeaderBinding
import me.alexpetrakov.cyclone.databinding.ItemHourlyForecastBinding

class WeatherAdapter :
    ListAdapter<DisplayableItem, RecyclerView.ViewHolder>(DisplayableItem.DiffCallback) {

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is DisplayableItem.HeaderUi -> R.layout.item_header
            is DisplayableItem.CurrentConditionsUi -> R.layout.item_current_conditions
            is DisplayableItem.HourlyForecastUi -> R.layout.item_hourly_forecast
            is DisplayableItem.DayConditionsUi -> R.layout.item_day_conditions
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            R.layout.item_header -> HeaderViewHolder.from(parent)
            R.layout.item_current_conditions -> CurrentConditionsViewHolder.from(parent)
            R.layout.item_hourly_forecast -> HourlyForecastViewHolder.from(parent)
            R.layout.item_day_conditions -> DayConditionsViewHolder.from(parent)
            else -> throw IllegalStateException("Unknown view type: $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        when (holder) {
            is HeaderViewHolder -> holder.bind(item as DisplayableItem.HeaderUi)
            is CurrentConditionsViewHolder -> holder.bind(item as DisplayableItem.CurrentConditionsUi)
            is HourlyForecastViewHolder -> holder.bind(item as DisplayableItem.HourlyForecastUi)
            is DayConditionsViewHolder -> holder.bind(item as DisplayableItem.DayConditionsUi)
            else -> throw IllegalStateException("Unexpected view holder type: ${holder::class.java}")
        }
    }

    class HeaderViewHolder(
        private val binding: ItemHeaderBinding,
        private val resources: Resources
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: DisplayableItem.HeaderUi): Unit = with(binding) {
            titleTextView.text = item.text.asString(resources)
        }

        companion object {
            fun from(parent: ViewGroup): HeaderViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                return HeaderViewHolder(
                    ItemHeaderBinding.inflate(layoutInflater, parent, false),
                    parent.context.resources
                )
            }
        }
    }

    class CurrentConditionsViewHolder(
        private val binding: ItemCurrentConditionsBinding,
        private val resources: Resources
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: DisplayableItem.CurrentConditionsUi): Unit = with(binding) {
            temperatureTextView.text = item.temperature.asString(resources)
            feelsLikeTextView.text = item.feelsLikeTemperature.asString(resources)
            conditionsTextView.text = item.conditions.asString(resources)
            conditionsImageView.setImageResource(item.icon.resId)
            windValueTextView.text = item.wind.asString(resources)
            pressureValueTextView.text = item.pressure.asString(resources)
            humidityValueTextView.text = item.humidity.asString(resources)
            dewPointValueTextView.text = item.dewPoint.asString(resources)
            visibilityValueTextView.text = item.visibility.asString(resources)
            uvIndexTextView.text = item.uvIndex.asString(resources)
        }

        companion object {
            fun from(parent: ViewGroup): CurrentConditionsViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                return CurrentConditionsViewHolder(
                    ItemCurrentConditionsBinding.inflate(layoutInflater, parent, false),
                    parent.context.resources
                )
            }
        }
    }

    class HourlyForecastViewHolder(
        private val binding: ItemHourlyForecastBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: DisplayableItem.HourlyForecastUi): Unit = with(binding) {
            hourlyForecastRecyclerView.adapter = HourlyForecastAdapter().apply {
                submitList(item.hourConditions)
            }
        }

        companion object {
            fun from(parent: ViewGroup): HourlyForecastViewHolder {
                val context = parent.context
                val binding = ItemHourlyForecastBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                ).apply {
                    hourlyForecastRecyclerView.layoutManager = LinearLayoutManager(
                        context,
                        LinearLayoutManager.HORIZONTAL,
                        false
                    )
                }
                return HourlyForecastViewHolder(
                    binding
                )
            }
        }
    }

    class DayConditionsViewHolder(
        private val binding: ItemDayConditionsBinding,
        private val resources: Resources
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: DisplayableItem.DayConditionsUi): Unit = with(binding) {
            dateTextView.text = item.date.asString(resources)
            tempLowTextView.text = item.temperatureLow.asString(resources)
            tempHighTextView.text = item.temperatureHigh.asString(resources)
            precipitationChanceTextView.apply {
                isVisible = item.precipitationChanceIsVisible
                text = item.precipitationChance.asString(resources)
            }
            conditionsImageView.apply {
                setImageResource(item.icon.resId)
                contentDescription = item.conditions.asString(resources)
            }
        }

        companion object {
            fun from(parent: ViewGroup): DayConditionsViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                return DayConditionsViewHolder(
                    ItemDayConditionsBinding.inflate(layoutInflater, parent, false),
                    parent.context.resources
                )
            }
        }
    }
}