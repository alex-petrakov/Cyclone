package me.alexpetrakov.cyclone.locations.presentation

import androidx.annotation.DrawableRes
import androidx.recyclerview.widget.DiffUtil
import me.alexpetrakov.cyclone.R
import me.alexpetrakov.cyclone.common.presentation.TextResource

data class ViewState(
    val locations: List<LocationUiItem>
)

data class LocationUiItem(
    val id: Int,
    val name: TextResource,
    val removeActionIsVisible: Boolean,
    val isDraggable: Boolean,
    @DrawableRes val iconResId: Int
) {

    companion object {
        fun fromCurrentLocation(id: Int) = LocationUiItem(
            id,
            TextResource.from(R.string.app_current_location),
            removeActionIsVisible = false,
            isDraggable = false,
            iconResId = R.drawable.ic_current_location
        )

        fun fromSavedLocation(
            id: Int,
            name: TextResource,
            removeActionIsVisible: Boolean
        ): LocationUiItem {
            return LocationUiItem(
                id,
                name,
                removeActionIsVisible,
                isDraggable = true,
                R.drawable.ic_drag_handle
            )
        }
    }

    object ItemCallback : DiffUtil.ItemCallback<LocationUiItem>() {
        override fun areItemsTheSame(oldItem: LocationUiItem, newItem: LocationUiItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: LocationUiItem, newItem: LocationUiItem): Boolean {
            return oldItem == newItem
        }
    }
}