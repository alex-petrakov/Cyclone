package me.alexpetrakov.cyclone.locations

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import me.alexpetrakov.cyclone.BuildConfig
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
            database = get(),
            prefs = getLocationPrefs(androidContext())
        )
    }

    viewModel {
        LocationsViewModel(
            router = get(),
            locationsRepository = get()
        )
    }
}

private fun getLocationPrefs(context: Context): SharedPreferences {
    return context.getSharedPreferences(
        "${BuildConfig.APPLICATION_ID}.LOCATION_PREFS",
        Context.MODE_PRIVATE
    )
}