package me.alexpetrakov.cyclone.locations.data

import android.content.SharedPreferences
import com.fredporciuncula.flow.preferences.FlowSharedPreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import me.alexpetrakov.cyclone.locations.data.db.LocationDao
import me.alexpetrakov.cyclone.locations.data.db.LocationEntity
import me.alexpetrakov.cyclone.locations.domain.model.Coordinates
import me.alexpetrakov.cyclone.locations.domain.model.Location
import me.alexpetrakov.cyclone.locations.domain.repositories.LocationsRepository
import javax.inject.Inject
import javax.inject.Qualifier
import javax.inject.Singleton

@OptIn(ExperimentalCoroutinesApi::class)
@Singleton
class LocationsDataStore @Inject constructor(
    private val locationDao: LocationDao,
    @LocationPrefs prefs: SharedPreferences
) : LocationsRepository {

    @Qualifier
    annotation class LocationPrefs

    private val flowPrefs = FlowSharedPreferences(prefs)

    private val selectedLocationId = flowPrefs.getInt(
        "selectedLocation",
        defaultValue = Location.CurrentLocation.id
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

    override suspend fun selectLocation(id: Int) {
        selectedLocationId.set(id)
    }

    override suspend fun getSelectedLocation(): Location {
        val selectedId = withContext(Dispatchers.IO) { selectedLocationId.get() }
        return locationDao.getById(selectedId)?.toDomainModel() ?: Location.CurrentLocation
    }

    override fun getSelectedLocationStream(): Flow<Location> {
        return selectedLocationId.asFlow()
            .map { id -> locationDao.getById(id)?.toDomainModel() ?: Location.CurrentLocation }
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