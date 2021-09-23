package me.alexpetrakov.cyclone.locationsearch.data

import com.github.kittinunf.result.Result
import com.github.kittinunf.result.map
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import me.alexpetrakov.cyclone.locationsearch.data.openweathermap.GeocodingApi
import me.alexpetrakov.cyclone.locationsearch.data.openweathermap.SearchResultJson
import me.alexpetrakov.cyclone.locationsearch.data.openweathermap.toDomain
import me.alexpetrakov.cyclone.locationsearch.domain.Fail
import me.alexpetrakov.cyclone.locationsearch.domain.LocationSearchRepository
import me.alexpetrakov.cyclone.locationsearch.domain.SearchResult
import retrofit2.HttpException

class LocationSearchProvider(private val geocodingApi: GeocodingApi) : LocationSearchRepository {

    override suspend fun searchLocations(query: String): Result<List<SearchResult>, Fail> {
        return Result.fromNetworkRequest { geocodingApi.searchLocationsByName(query, 25) }
            .map { searchResultsToDomainModel(it) }
    }

    private suspend fun searchResultsToDomainModel(
        searchResults: List<SearchResultJson>
    ): List<SearchResult> = withContext(Dispatchers.Default) {
        searchResults.map { it.toDomain() }
    }
}

suspend fun <V> Result.Companion.fromNetworkRequest(request: suspend () -> V): Result<V, Fail> {
    return try {
        success(request())
    } catch (e: java.io.IOException) {
        failure(Fail.NoConnection(e))
    } catch (e: HttpException) {
        failure(Fail.NoConnection(e))
    }
}