package me.alexpetrakov.cyclone.locations.presentation.list

import android.annotation.SuppressLint
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

    var onClickItem: ((LocationUiItem) -> Unit)? = null

    var onRemoveItem: ((LocationUiItem) -> Unit)? = null

    private val onStartDragListener = { viewHolder: ViewHolder ->
        itemTouchHelper.startDrag(viewHolder)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemLocationBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(
            binding,
            { adapterPosition -> onClickItem?.invoke(getItem(adapterPosition)) },
            { adapterPosition -> onRemoveItem?.invoke(getItem(adapterPosition)) },
            onStartDragListener
        )
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
        private val onClick: (Int) -> Unit,
        private val onClickRemove: (Int) -> Unit,
        private val onStartDrag: (ViewHolder) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        private val resources get() = binding.root.context.resources

        init {
            binding.apply {
                removeButton.setOnClickListener { onClickRemove(adapterPosition) }
                root.setOnClickListener { onClick(adapterPosition) }
            }
        }

        fun bind(item: LocationUiItem): Unit = with(binding) {
            dragHandle.setImageResource(item.iconResId)
            setUpDragHandle(item, dragHandle)
            nameTextView.text = item.name.asString(resources)
            removeButton.isVisible = item.removeActionIsVisible
        }

        @SuppressLint("ClickableViewAccessibility")
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
