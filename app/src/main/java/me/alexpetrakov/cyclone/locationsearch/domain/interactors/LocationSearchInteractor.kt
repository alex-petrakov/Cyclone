package me.alexpetrakov.cyclone.locationsearch.domain.interactors

import com.github.kittinunf.result.Result
import me.alexpetrakov.cyclone.locationsearch.domain.model.Fail
import me.alexpetrakov.cyclone.locationsearch.domain.model.SearchResult
import me.alexpetrakov.cyclone.locationsearch.domain.repositories.LocationSearchRepository
import javax.inject.Inject

class LocationSearchInteractor @Inject constructor(
    private val locationSearchRepository: LocationSearchRepository
) {

    suspend fun searchLocations(query: String): Result<List<SearchResult>, Fail> {
        return if (query.isBlank()) {
            Result.success(emptyList())
        } else {
            locationSearchRepository.searchLocations(query)
        }
    }
}