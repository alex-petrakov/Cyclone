package me.alexpetrakov.cyclone.locations

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import me.alexpetrakov.cyclone.BuildConfig
import me.alexpetrakov.cyclone.locations.data.LocationsDataStore
import me.alexpetrakov.cyclone.locations.data.db.AppDatabase
import me.alexpetrakov.cyclone.locations.domain.repositories.LocationsRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface LocationsDiModule {

    @Binds
    fun bindLocationsRepository(locationsDataStore: LocationsDataStore): LocationsRepository

    companion object {

        private const val APP_DB_NAME: String = "cyclone"

        @Provides
        @Singleton
        fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
            return Room.databaseBuilder(
                context,
                AppDatabase::class.java,
                APP_DB_NAME
            ).build()
        }

        @Provides
        @Singleton
        @LocationsDataStore.LocationPrefs
        fun provideLocationPrefs(@ApplicationContext context: Context): SharedPreferences {
            return context.getSharedPreferences(
                "${BuildConfig.APPLICATION_ID}.LOCATION_PREFS",
                Context.MODE_PRIVATE
            )
        }
    }
}