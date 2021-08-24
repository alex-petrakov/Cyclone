package me.alexpetrakov.cyclone.weather.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import me.alexpetrakov.cyclone.databinding.ItemTitleBinding

class WeatherAdapter : ListAdapter<String, WeatherAdapter.ViewHolder>(stringDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemTitleBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder(
        private val binding: ItemTitleBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: String): Unit = with(binding) {
            titleTextView.text = item
        }
    }
}