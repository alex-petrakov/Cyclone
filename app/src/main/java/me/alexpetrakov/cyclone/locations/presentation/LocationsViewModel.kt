package me.alexpetrakov.cyclone.locations.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import me.alexpetrakov.cyclone.common.presentation.asTextResource

class LocationsViewModel : ViewModel() {

    val viewState = liveData {
        emit(
            ViewState(
                listOf(
                    LocationUiItem.fromCurrentLocation(0),
                    LocationUiItem.fromSavedLocation(1, "Moscow".asTextResource(), true),
                    LocationUiItem.fromSavedLocation(2, "Berlin".asTextResource(), true),
                    LocationUiItem.fromSavedLocation(3, "Chicago".asTextResource(), true)
                )
            )
        )
    }
}