package me.alexpetrakov.cyclone.locations.presentation

import me.alexpetrakov.cyclone.common.presentation.TextResource

sealed class ViewEffect {
    data class DisplayRemovalConfirmation(
        val confirmationText: TextResource,
        val idOfItemToBeRemoved: Int
    ) : ViewEffect()
}