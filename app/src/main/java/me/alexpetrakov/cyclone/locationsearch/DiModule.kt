package me.alexpetrakov.cyclone.locationsearch

import me.alexpetrakov.cyclone.locationsearch.data.LocationSearchProvider
import me.alexpetrakov.cyclone.locationsearch.data.openweathermap.GeocodingApi
import me.alexpetrakov.cyclone.locationsearch.domain.interactors.LocationSearchInteractor
import me.alexpetrakov.cyclone.locationsearch.domain.repositories.LocationSearchRepository
import me.alexpetrakov.cyclone.locationsearch.presentation.LocationSearchViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit

val locationSearchModule = module {

    single<LocationSearchRepository> {
        val geocodingApi = get<Retrofit>().create(GeocodingApi::class.java)
        LocationSearchProvider(geocodingApi)
    }

    factory { LocationSearchInteractor(locationSearchRepository = get()) }

    viewModel { params ->
        LocationSearchViewModel(
            savedStateHandle = params.get(),
            locationSearchInteractor = get(),
            locationsInteractor = get(),
            router = get()
        )
    }
}