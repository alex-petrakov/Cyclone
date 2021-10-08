package me.alexpetrakov.cyclone.units

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import me.alexpetrakov.cyclone.units.data.UnitsDataStore
import me.alexpetrakov.cyclone.units.domain.interactors.UnitsInteractor
import me.alexpetrakov.cyclone.units.domain.repositories.UnitsRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val unitsModule = module {
    single<UnitsRepository> {
        UnitsDataStore(
            prefs = getUnitPrefs(androidContext())
        )
    }

    factory { UnitsInteractor(unitsRepository = get()) }
}

private fun getUnitPrefs(context: Context): SharedPreferences {
    return PreferenceManager.getDefaultSharedPreferences(context)
}