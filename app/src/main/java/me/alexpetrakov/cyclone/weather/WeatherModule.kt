package me.alexpetrakov.cyclone.weather

import android.content.Context
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.SettingsClient
import dagger.Binds
import dagger.Module
import dagger.Provides
import me.alexpetrakov.cyclone.weather.data.DeviceLocationProvider
import me.alexpetrakov.cyclone.weather.data.WeatherProvider
import me.alexpetrakov.cyclone.weather.data.openweathermap.ForecastApi
import me.alexpetrakov.cyclone.weather.domain.interactors.WeatherInteractor
import me.alexpetrakov.cyclone.weather.domain.repositories.DeviceLocator
import me.alexpetrakov.cyclone.weather.domain.repositories.WeatherRepository
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
interface WeatherModule {

    @Binds
    fun bindWeatherRepository(weatherProvider: WeatherProvider): WeatherRepository

    @Binds
    fun bindDeviceLocator(deviceLocationProvider: DeviceLocationProvider): DeviceLocator

    companion object {

        @Provides
        @Singleton
        fun provideForecastApi(retrofit: Retrofit): ForecastApi {
            return retrofit.create(ForecastApi::class.java)
        }

        @Provides
        @Singleton
        fun provideFusedLocationProviderClient(appContext: Context): FusedLocationProviderClient {
            return LocationServices.getFusedLocationProviderClient(appContext)
        }

        @Provides
        @Singleton
        fun provideSettingsClient(appContext: Context): SettingsClient {
            return LocationServices.getSettingsClient(appContext)
        }

        @Provides
        fun provideWeatherInteractor(
            weatherRepository: WeatherRepository,
            deviceLocator: DeviceLocator
        ): WeatherInteractor {
            return WeatherInteractor(weatherRepository, deviceLocator)
        }
    }
}