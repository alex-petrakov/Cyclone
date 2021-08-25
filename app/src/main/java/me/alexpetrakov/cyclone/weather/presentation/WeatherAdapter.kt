package me.alexpetrakov.cyclone.weather.presentation

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import me.alexpetrakov.cyclone.R
import me.alexpetrakov.cyclone.common.asString
import me.alexpetrakov.cyclone.databinding.ItemCurrentConditionsBinding
import me.alexpetrakov.cyclone.databinding.ItemHeaderBinding

class WeatherAdapter(
    private val resources: Resources
) : ListAdapter<DisplayableItem, RecyclerView.ViewHolder>(DisplayableItem.DiffCallback) {

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is DisplayableItem.Header -> R.layout.item_header
            is DisplayableItem.CurrentConditions -> R.layout.item_current_conditions
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            R.layout.item_header -> HeaderViewHolder(
                ItemHeaderBinding.inflate(layoutInflater, parent, false),
                resources
            )
            R.layout.item_current_conditions -> CurrentConditionsViewHolder(
                ItemCurrentConditionsBinding.inflate(layoutInflater, parent, false),
                resources
            )
            else -> throw IllegalStateException("Unknown view type: $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is HeaderViewHolder -> holder.bind(getItem(position) as DisplayableItem.Header)
            is CurrentConditionsViewHolder -> holder.bind(getItem(position) as DisplayableItem.CurrentConditions)
            else -> throw IllegalStateException("Unexpected view holder type: ${holder::class.java}")
        }
    }

    class HeaderViewHolder(
        private val binding: ItemHeaderBinding,
        private val resources: Resources
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: DisplayableItem.Header): Unit = with(binding) {
            titleTextView.text = item.text.asString(resources)
        }
    }

    class CurrentConditionsViewHolder(
        private val binding: ItemCurrentConditionsBinding,
        private val resources: Resources
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: DisplayableItem.CurrentConditions): Unit = with(binding) {
            temperatureTextView.text = item.temperature.asString(resources)
            feelsLikeTextView.text = item.feelsLikeTemperature.asString(resources)
            conditionsTextView.text = item.conditions.asString(resources)
            conditionsImageView.setImageResource(item.conditionsIconRes)
            windValueTextView.text = item.wind.asString(resources)
            pressureValueTextView.text = item.pressure.asString(resources)
            humidityValueTextView.text = item.humidity.asString(resources)
            dewPointValueTextView.text = item.humidity.asString(resources)
            visibilityValueTextView.text = item.visibility.asString(resources)
            uvIndexTextView.text = item.uvIndex.asString(resources)
        }
    }
}