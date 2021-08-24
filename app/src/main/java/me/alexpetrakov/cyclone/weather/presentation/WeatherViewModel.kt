package me.alexpetrakov.cyclone.weather.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData

class WeatherViewModel : ViewModel() {

    private val _viewState = liveData<ViewState> {
        val items = (1..50).map { it.toString() }.toList()
        emit(ViewState(items))
    }

    val viewState get() = _viewState
}