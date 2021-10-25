package me.alexpetrakov.cyclone.locations

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import dagger.Binds
import dagger.Module
import dagger.Provides
import me.alexpetrakov.cyclone.BuildConfig
import me.alexpetrakov.cyclone.locations.data.LocationsDataStore
import me.alexpetrakov.cyclone.locations.data.db.AppDatabase
import me.alexpetrakov.cyclone.locations.domain.repositories.LocationsRepository
import javax.inject.Singleton

@Module
interface LocationsModule {

    @Binds
    fun bindLocationsRepository(locationsDataStore: LocationsDataStore): LocationsRepository

    companion object {

        private const val APP_DB_FILE_NAME = "cyclone"

        @Provides
        @Singleton
        fun provideAppDatabase(appContext: Context): AppDatabase {
            return Room.databaseBuilder(
                appContext,
                AppDatabase::class.java,
                APP_DB_FILE_NAME
            ).build()
        }

        @Provides
        @Singleton
        @LocationsDataStore.LocationPrefs
        fun provideLocationPrefs(appContext: Context): SharedPreferences {
            return appContext.getSharedPreferences(
                "${BuildConfig.APPLICATION_ID}.LOCATION_PREFS",
                Context.MODE_PRIVATE
            )
        }
    }
}
