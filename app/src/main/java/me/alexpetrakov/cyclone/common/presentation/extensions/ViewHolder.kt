package me.alexpetrakov.cyclone.common.presentation.extensions

import androidx.recyclerview.widget.RecyclerView

inline fun RecyclerView.ViewHolder.withAdapterPosition(action: (Int) -> Unit) {
    val position = adapterPosition
    if (position != RecyclerView.NO_POSITION) {
        action(position)
    }
}