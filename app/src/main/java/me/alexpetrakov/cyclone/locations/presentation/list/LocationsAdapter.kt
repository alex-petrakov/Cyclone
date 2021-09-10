package me.alexpetrakov.cyclone.locations.presentation.list

import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import me.alexpetrakov.cyclone.common.presentation.asString
import me.alexpetrakov.cyclone.databinding.ItemLocationBinding
import me.alexpetrakov.cyclone.locations.presentation.LocationUiItem

class LocationsAdapter(private val itemTouchHelper: ItemTouchHelper) :
    ListAdapter<LocationUiItem, LocationsAdapter.ViewHolder>(LocationUiItem.ItemCallback) {

    private val onStartDragListener = { viewHolder: ViewHolder ->
        itemTouchHelper.startDrag(viewHolder)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemLocationBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding, onStartDragListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    fun onMoveItem(from: Int, to: Int) {
        val updatedList = currentList.toMutableList().apply {
            add(to, removeAt(from))
        }.toList()
        submitList(updatedList)
    }

    class ViewHolder(
        private val binding: ItemLocationBinding,
        private val onStartDrag: (ViewHolder) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        private val resources get() = binding.root.context.resources

        fun bind(item: LocationUiItem): Unit = with(binding) {
            dragHandle.setImageResource(item.iconResId)
            setUpDragHandle(item, dragHandle)
            nameTextView.text = item.name.asString(resources)
            removeButton.isVisible = item.removeActionIsVisible
        }

        private fun setUpDragHandle(item: LocationUiItem, handle: View) {
            if (item.isDraggable) {
                handle.setOnTouchListener { _, event ->
                    if (event.action == MotionEvent.ACTION_DOWN) {
                        onStartDrag(this)
                        true
                    } else {
                        false
                    }
                }
            } else {
                handle.setOnTouchListener(null)
            }
        }
    }
}
