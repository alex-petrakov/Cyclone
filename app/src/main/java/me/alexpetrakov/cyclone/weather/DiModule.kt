package me.alexpetrakov.cyclone.weather

import com.google.android.gms.location.LocationServices
import me.alexpetrakov.cyclone.weather.data.DeviceLocationProvider
import me.alexpetrakov.cyclone.weather.data.WeatherProvider
import me.alexpetrakov.cyclone.weather.data.openweathermap.ForecastApi
import me.alexpetrakov.cyclone.weather.domain.DeviceLocator
import me.alexpetrakov.cyclone.weather.domain.GetWeather
import me.alexpetrakov.cyclone.weather.domain.WeatherRepository
import me.alexpetrakov.cyclone.weather.presentation.WeatherViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit

val weatherModule = module {

    single<WeatherRepository> {
        val openWeatherMapService = get<Retrofit>().create(ForecastApi::class.java)
        WeatherProvider(openWeatherMapService)
    }

    single<DeviceLocator> {
        DeviceLocationProvider(
            LocationServices.getFusedLocationProviderClient(androidContext()),
            LocationServices.getSettingsClient(androidContext())
        )
    }

    factory {
        GetWeather(
            weatherRepo = get(),
            locationRepo = get()
        )
    }

    viewModel {
        WeatherViewModel(
            getWeather = get(),
            locationsRepository = get(),
            unitsRepository = get(),
            router = get()
        )
    }
}