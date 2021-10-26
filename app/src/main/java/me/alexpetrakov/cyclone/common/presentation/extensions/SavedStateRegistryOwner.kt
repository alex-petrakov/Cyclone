package me.alexpetrakov.cyclone.common.presentation.extensions

import android.os.Bundle
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.savedstate.SavedStateRegistryOwner

inline fun <reified VM : ViewModel> SavedStateRegistryOwner.newViewModelFactory(
    noinline vmCreator: (SavedStateHandle) -> VM
): ViewModelProvider.Factory {
    return SavedStateViewModelFactory(
        this,
        null,
        vmCreator
    )
}

class SavedStateViewModelFactory<VM : ViewModel>(
    savedStateRegistryOwner: SavedStateRegistryOwner,
    defaultArgs: Bundle?,
    private val vmCreator: (SavedStateHandle) -> VM
) : AbstractSavedStateViewModelFactory(savedStateRegistryOwner, defaultArgs) {

    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(
        key: String,
        modelClass: Class<T>,
        handle: SavedStateHandle
    ): T {
        return vmCreator(handle) as T
    }
}