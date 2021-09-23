package me.alexpetrakov.cyclone.locationsearch.data.openweathermap

import com.squareup.moshi.JsonClass
import me.alexpetrakov.cyclone.locations.domain.Coordinates
import me.alexpetrakov.cyclone.locationsearch.domain.SearchResult

@JsonClass(generateAdapter = true)
data class SearchResultJson(
    val name: String,
    val lat: Double,
    val lon: Double,
    val country: String,
    val state: String?
)

fun SearchResultJson.toDomain(): SearchResult {
    val countryName = if (state != null) {
        "$country, $state"
    } else {
        country
    }
    return SearchResult(
        name,
        countryName,
        Coordinates(lat, lon)
    )
}
