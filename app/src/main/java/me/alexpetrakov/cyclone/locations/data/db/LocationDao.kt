package me.alexpetrakov.cyclone.locations.data.db

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface LocationDao {

    @Query("SELECT * FROM location ORDER BY position")
    fun getAllAsStream(): Flow<List<LocationEntity>>

    @Query("SELECT * FROM location WHERE id = :id")
    suspend fun getById(id: Int): LocationEntity?

    @Query("SELECT COUNT(id) FROM location")
    suspend fun getLocationCount(): Int

    @Insert
    suspend fun createLocation(location: LocationEntity)

    @Update
    suspend fun updateAll(locations: List<LocationEntity>)

    @Transaction
    suspend fun updateOrder(ids: List<Int>) {
        for ((index, value) in ids.withIndex()) {
            updateOrder(id = value, position = index)
        }
    }

    @Query("UPDATE location SET position = :position WHERE id = :id")
    suspend fun updateOrder(id: Int, position: Int)

    @Query("DELETE FROM location WHERE id = :id")
    suspend fun removeById(id: Int)
}