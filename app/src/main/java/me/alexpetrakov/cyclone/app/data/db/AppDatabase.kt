package me.alexpetrakov.cyclone.app.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import me.alexpetrakov.cyclone.locations.data.db.LocationDao
import me.alexpetrakov.cyclone.locations.data.db.LocationEntity

@Database(entities = [LocationEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun locationDao(): LocationDao
}