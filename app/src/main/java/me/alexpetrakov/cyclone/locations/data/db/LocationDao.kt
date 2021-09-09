package me.alexpetrakov.cyclone.locations.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface LocationDao {

    @Query("SELECT * FROM location ORDER BY position")
    fun getAllAsStream(): Flow<List<LocationEntity>>

    @Query("SELECT COUNT(id) FROM location")
    suspend fun getLocationCount(): Int

    @Insert
    suspend fun createLocation(locationEntity: LocationEntity)
}