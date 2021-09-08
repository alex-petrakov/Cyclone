package me.alexpetrakov.cyclone.locations

import me.alexpetrakov.cyclone.locations.data.LocationsDataStore
import me.alexpetrakov.cyclone.locations.domain.LocationsRepository
import me.alexpetrakov.cyclone.locations.presentation.LocationsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val locationModule = module {

    single<LocationsRepository> { LocationsDataStore() }

    viewModel { LocationsViewModel() }
}