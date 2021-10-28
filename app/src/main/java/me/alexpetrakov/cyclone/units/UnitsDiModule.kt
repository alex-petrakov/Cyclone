package me.alexpetrakov.cyclone.units

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import me.alexpetrakov.cyclone.units.data.UnitsDataStore
import me.alexpetrakov.cyclone.units.domain.repositories.UnitsRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface UnitsDiModule {

    @Binds
    fun bindUnitsRepository(unitsDataStore: UnitsDataStore): UnitsRepository

    companion object {

        @Provides
        @Singleton
        @UnitsDataStore.UnitPrefs
        fun provideUnitPrefs(@ApplicationContext context: Context): SharedPreferences {
            return PreferenceManager.getDefaultSharedPreferences(context)
        }
    }
}