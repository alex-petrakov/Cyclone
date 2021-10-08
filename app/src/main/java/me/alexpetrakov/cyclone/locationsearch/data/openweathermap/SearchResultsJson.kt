package me.alexpetrakov.cyclone.locationsearch.data.openweathermap

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import me.alexpetrakov.cyclone.locations.domain.model.Coordinates
import me.alexpetrakov.cyclone.locationsearch.domain.model.SearchResult
import java.util.*

@JsonClass(generateAdapter = true)
data class SearchResultJson(
    @Json(name = "name") val internationalName: String,
    @Json(name = "local_names") val languagesToLocalNames: Map<String, String>?,
    @Json(name = "lat") val lat: Double,
    @Json(name = "lon") val lon: Double,
    @Json(name = "country") val country: String,
    @Json(name = "state") val state: String?
)

fun SearchResultJson.toDomain(locale: Locale): SearchResult {
    val placeName = languagesToLocalNames?.get(locale.language) ?: internationalName
    val countryName = if (state != null) {
        "$country, $state"
    } else {
        country
    }
    return SearchResult(
        placeName,
        countryName,
        Coordinates(lat, lon)
    )
}
