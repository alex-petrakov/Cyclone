package me.alexpetrakov.cyclone.locationsearch.domain

import com.github.kittinunf.result.Result

class LocationSearchInteractor(private val locationSearchRepository: LocationSearchRepository) {

    suspend fun searchLocations(query: String): Result<List<SearchResult>, Fail> {
        return if (query.isBlank()) {
            Result.success(emptyList())
        } else {
            locationSearchRepository.searchLocations(query)
        }
    }
}