package me.alexpetrakov.cyclone.locations

import me.alexpetrakov.cyclone.locations.data.LocationsDataStore
import me.alexpetrakov.cyclone.locations.domain.LocationsRepository
import org.koin.dsl.module

val locationModule = module {

    single<LocationsRepository> { LocationsDataStore() }
}