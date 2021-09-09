package me.alexpetrakov.cyclone.locations.data.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "location")
data class LocationEntity(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "lat") val latitude: Double,
    @ColumnInfo(name = "lon") val longitude: Double,
    @ColumnInfo(name = "position") val position: Int
)