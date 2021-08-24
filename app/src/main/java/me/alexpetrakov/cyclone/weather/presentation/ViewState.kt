package me.alexpetrakov.cyclone.weather.presentation

import androidx.recyclerview.widget.DiffUtil

data class ViewState(val items: List<String>)

val stringDiffCallback = object : DiffUtil.ItemCallback<String>() {
    override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
        return areItemsTheSame(oldItem, newItem)
    }
}