package me.alexpetrakov.cyclone.weather.presentation

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import me.alexpetrakov.cyclone.common.asString
import me.alexpetrakov.cyclone.databinding.ItemHourConditionsBinding

class HourlyForecastAdapter :
    ListAdapter<HourConditions, HourlyForecastAdapter.ViewHolder>(HourConditions.DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemHourConditionsBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            parent.context.resources
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder(
        private val binding: ItemHourConditionsBinding,
        private val resources: Resources
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: HourConditions): Unit = with(binding) {
            temperatureTextView.text = item.temperature.asString(resources)
            precipitationChanceTextView.apply {
                text = item.precipitationChance.asString(resources)
                isVisible = item.precipitationChanceIsVisible
            }
            conditionsImageView.apply {
                setImageResource(item.conditionsIconRes)
                contentDescription = item.conditions.asString(resources)
            }
            timeTextView.text = item.time.asString(resources)
        }
    }
}