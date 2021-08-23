package me.alexpetrakov.cyclone.weather

import me.alexpetrakov.cyclone.weather.presentation.WeatherViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val weatherModule = module {
    viewModel { WeatherViewModel() }
}