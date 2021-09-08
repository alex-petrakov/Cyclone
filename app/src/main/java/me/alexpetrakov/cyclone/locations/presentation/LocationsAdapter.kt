package me.alexpetrakov.cyclone.locations.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import me.alexpetrakov.cyclone.common.presentation.asString
import me.alexpetrakov.cyclone.databinding.ItemLocationBinding

class LocationsAdapter :
    ListAdapter<LocationUiItem, LocationsAdapter.ViewHolder>(LocationUiItem.ItemCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemLocationBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder(private val binding: ItemLocationBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private val resources get() = binding.root.context.resources

        fun bind(item: LocationUiItem): Unit = with(binding) {
            dragHandle.setImageResource(item.iconResId)
            nameTextView.text = item.name.asString(resources)
            removeButton.isVisible = item.removeActionIsVisible
        }
    }
}
