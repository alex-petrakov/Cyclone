package me.alexpetrakov.cyclone.locationsearch.domain.repositories

import com.github.kittinunf.result.Result
import me.alexpetrakov.cyclone.locationsearch.domain.model.Fail
import me.alexpetrakov.cyclone.locationsearch.domain.model.SearchResult

interface LocationSearchRepository {

    suspend fun searchLocations(query: String): Result<List<SearchResult>, Fail>
}