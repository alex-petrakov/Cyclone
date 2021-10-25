package me.alexpetrakov.cyclone.units

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import dagger.Binds
import dagger.Module
import dagger.Provides
import me.alexpetrakov.cyclone.units.data.UnitsDataStore
import me.alexpetrakov.cyclone.units.domain.interactors.UnitsInteractor
import me.alexpetrakov.cyclone.units.domain.repositories.UnitsRepository
import javax.inject.Singleton

@Module
interface UnitsModule {

    @Binds
    fun bindUnitsRepository(unitsDataStore: UnitsDataStore): UnitsRepository

    companion object {
        @Provides
        @Singleton
        @UnitsDataStore.UnitPrefs
        fun provideUnitPrefs(appContext: Context): SharedPreferences {
            return PreferenceManager.getDefaultSharedPreferences(appContext)
        }

        @Provides
        fun provideUnitsInteractor(unitsRepository: UnitsRepository): UnitsInteractor {
            return UnitsInteractor(unitsRepository)
        }
    }
}