package me.alexpetrakov.cyclone.weather

import me.alexpetrakov.cyclone.weather.data.WeatherProvider
import me.alexpetrakov.cyclone.weather.data.openweathermap.ForecastApi
import me.alexpetrakov.cyclone.weather.domain.WeatherRepository
import me.alexpetrakov.cyclone.weather.presentation.WeatherViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit

val weatherModule = module {

    single<WeatherRepository> {
        val openWeatherMapService = get<Retrofit>().create(ForecastApi::class.java)
        WeatherProvider(openWeatherMapService)
    }

    viewModel {
        WeatherViewModel(
            weatherRepository = get(),
            locationsRepository = get(),
            unitsRepository = get(),
            router = get()
        )
    }
}