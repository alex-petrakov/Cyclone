package me.alexpetrakov.cyclone.app

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import me.alexpetrakov.cyclone.app.data.db.AppDatabase
import me.alexpetrakov.cyclone.locations.data.db.LocationDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppDiModule {

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
    fun provideLocationDao(appDatabase: AppDatabase): LocationDao {
        return appDatabase.locationDao()
    }
}