package me.alexpetrakov.cyclone.locationsearch

import dagger.Binds
import dagger.Module
import dagger.Provides
import me.alexpetrakov.cyclone.locationsearch.data.LocationSearchProvider
import me.alexpetrakov.cyclone.locationsearch.data.openweathermap.GeocodingApi
import me.alexpetrakov.cyclone.locationsearch.domain.repositories.LocationSearchRepository
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
interface LocationSearchModule {

    @Binds
    fun bindLocationSearchRepository(
        locationSearchProvider: LocationSearchProvider
    ): LocationSearchRepository

    companion object {

        @Provides
        @Singleton
        fun provideLocationSearchRepository(geocodingApi: GeocodingApi): LocationSearchProvider {
            return LocationSearchProvider(geocodingApi)
        }

        @Provides
        @Singleton
        fun provideGeocodingApi(retrofit: Retrofit): GeocodingApi {
            return retrofit.create(GeocodingApi::class.java)
        }
    }
}