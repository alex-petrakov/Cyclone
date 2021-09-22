package me.alexpetrakov.cyclone.locationsearch.domain

import com.github.kittinunf.result.Result

interface LocationSearchRepository {

    suspend fun searchLocations(query: String): Result<List<SearchResult>, Throwable>
}