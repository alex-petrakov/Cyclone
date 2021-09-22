package me.alexpetrakov.cyclone.locationsearch.data

import com.github.kittinunf.result.Result
import kotlinx.coroutines.delay
import me.alexpetrakov.cyclone.locations.domain.Coordinates
import me.alexpetrakov.cyclone.locationsearch.domain.Fail
import me.alexpetrakov.cyclone.locationsearch.domain.LocationSearchRepository
import me.alexpetrakov.cyclone.locationsearch.domain.SearchResult
import kotlin.random.Random

class LocationSearchProvider : LocationSearchRepository {

    private val random = Random(System.currentTimeMillis())

    override suspend fun searchLocations(query: String): Result<List<SearchResult>, Fail> {
        // TODO: Connect to OpenWeather
        delay(750L)
        return if (random.nextBoolean()) {
            Result.success(
                listOf(
                    SearchResult("Moscow", "RU", Coordinates(55.751244, 37.618423)),
                    SearchResult("Bordeaux", "FR", Coordinates(44.836151, -0.580816)),
                    SearchResult("Chicago", "US, IL", Coordinates(41.881832, -87.623177)),
                    SearchResult("Moscow", "RU", Coordinates(55.751244, 37.618423)),
                    SearchResult("Bordeaux", "FR", Coordinates(44.836151, -0.580816)),
                    SearchResult("Chicago", "US, IL", Coordinates(41.881832, -87.623177)),
                    SearchResult("Moscow", "RU", Coordinates(55.751244, 37.618423)),
                    SearchResult("Bordeaux", "FR", Coordinates(44.836151, -0.580816)),
                    SearchResult("Chicago", "US, IL", Coordinates(41.881832, -87.623177))
                )
            )
        } else {
            Result.failure(Fail.NoConnection())
        }
    }
}