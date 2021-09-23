package me.alexpetrakov.cyclone.locationsearch.data.openweathermap

import retrofit2.http.GET
import retrofit2.http.Query

interface GeocodingApi {

    @GET("geo/1.0/direct")
    suspend fun searchLocationsByName(
        @Query("q") query: String,
        @Query("limit") limit: Int
    ): List<SearchResultJson>
}