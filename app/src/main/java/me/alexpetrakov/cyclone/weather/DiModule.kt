package me.alexpetrakov.cyclone.weather

import me.alexpetrakov.cyclone.weather.data.WeatherProvider
import me.alexpetrakov.cyclone.weather.domain.WeatherRepository
import me.alexpetrakov.cyclone.weather.presentation.WeatherViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val weatherModule = module {

    single<WeatherRepository> { WeatherProvider() }

    viewModel {
        WeatherViewModel(
            weatherRepository = get(),
            locationsRepository = get(),
            unitsRepository = get()
        )
    }
}