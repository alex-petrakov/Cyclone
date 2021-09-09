package me.alexpetrakov.cyclone.locations

import androidx.room.Room
import me.alexpetrakov.cyclone.locations.data.LocationsDataStore
import me.alexpetrakov.cyclone.locations.data.db.AppDatabase
import me.alexpetrakov.cyclone.locations.domain.LocationsRepository
import me.alexpetrakov.cyclone.locations.presentation.LocationsViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val locationModule = module {

    single {
        Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java,
            "cyclone"
        ).build()
    }

    single<LocationsRepository> {
        LocationsDataStore(
            database = get()
        )
    }

    viewModel {
        LocationsViewModel(
            router = get(),
            locationsRepository = get()
        )
    }
}