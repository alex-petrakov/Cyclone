package me.alexpetrakov.cyclone.locations.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import me.alexpetrakov.cyclone.locations.data.db.AppDatabase
import me.alexpetrakov.cyclone.locations.data.db.LocationEntity
import me.alexpetrakov.cyclone.locations.domain.Coordinates
import me.alexpetrakov.cyclone.locations.domain.Location
import me.alexpetrakov.cyclone.locations.domain.LocationsRepository

class LocationsDataStore(private val database: AppDatabase) : LocationsRepository {

    private val locationDao get() = database.locationDao()

    private val moscow = Location.StoredLocation(
        1,
        "Moscow",
        Coordinates(55.751244, 37.618423)
    )

    override fun getLocationsStream(): Flow<List<Location>> {
        return locationDao.getAllAsStream()
            .map { list ->
                val storedLocations = list.sortedBy { it.position }.map { it.toDomainModel() }
                listOf(Location.CurrentLocation) + storedLocations
            }
    }

    override suspend fun createLocation(location: Location.StoredLocation) {
        val locationCount = locationDao.getLocationCount()
        locationDao.createLocation(location.toLocationEntity(locationCount))
    }

    override suspend fun updateLocationsOrder(ids: List<Int>) {
        locationDao.updateOrder(ids)
    }

    override suspend fun removeLocationById(id: Int) {
        locationDao.removeById(id)
    }

    override suspend fun getSelectedLocation(): Location {
        return moscow
    }

    override fun observeSelectedLocation(): Flow<Location> {
        return flowOf(moscow)
    }

    private fun LocationEntity.toDomainModel(): Location {
        return Location.StoredLocation(
            id,
            name,
            Coordinates(latitude, longitude)
        )
    }

    private fun Location.StoredLocation.toLocationEntity(position: Int): LocationEntity {
        return LocationEntity(
            0,
            name,
            coordinates.lat,
            coordinates.lon,
            position
        )
    }
}