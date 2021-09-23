package me.alexpetrakov.cyclone.locationsearch

import me.alexpetrakov.cyclone.locationsearch.data.LocationSearchProvider
import me.alexpetrakov.cyclone.locationsearch.domain.LocationSearchRepository
import me.alexpetrakov.cyclone.locationsearch.presentation.LocationSearchViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val locationSearchModule = module {

    single<LocationSearchRepository> { LocationSearchProvider() }

    viewModel { params ->
        LocationSearchViewModel(
            savedStateHandle = params.get(),
            locationsRepository = get(),
            locationSearchRepository = get(),
            router = get()
        )
    }
}