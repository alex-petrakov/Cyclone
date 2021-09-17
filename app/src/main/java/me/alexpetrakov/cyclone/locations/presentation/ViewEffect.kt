package me.alexpetrakov.cyclone.locations.presentation

import me.alexpetrakov.cyclone.common.presentation.TextResource

sealed class ViewEffect {
    data class DisplayRemovalConfirmation(
        val locationId: Int,
        val confirmationText: TextResource
    ) : ViewEffect()
}