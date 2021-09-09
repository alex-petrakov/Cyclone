package me.alexpetrakov.cyclone.locations.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.github.terrakok.cicerone.Router
import me.alexpetrakov.cyclone.common.presentation.asTextResource

class LocationsViewModel(private val router: Router) : ViewModel() {

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

    fun onNavigateBack() {
        router.exit()
    }
}