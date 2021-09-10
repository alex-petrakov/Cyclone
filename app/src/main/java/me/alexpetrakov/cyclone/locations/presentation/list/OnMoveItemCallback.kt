package me.alexpetrakov.cyclone.locations.presentation.list

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.ACTION_STATE_IDLE
import androidx.recyclerview.widget.RecyclerView

class OnMoveItemCallback : ItemTouchHelper.Callback() {

    var onMoveItem: ((Int, Int) -> Boolean)? = null

    var onDropItem: (() -> Unit)? = null

    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        return makeMovementFlags(ItemTouchHelper.UP or ItemTouchHelper.DOWN, 0)
    }

    override fun onMove(
        recyclerView: RecyclerView,
        selected: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        val fromPosition = selected.adapterPosition
        val toPosition = target.adapterPosition
        if (fromPosition == 0 || toPosition == 0) {
            return false
        }
        return onMoveItem?.invoke(selected.adapterPosition, toPosition) ?: true
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        // Do nothing
    }

    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        super.onSelectedChanged(viewHolder, actionState)
        if (actionState == ACTION_STATE_IDLE) {
            onDropItem?.invoke()
        }
    }
}