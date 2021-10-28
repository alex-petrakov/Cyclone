package me.alexpetrakov.cyclone.weather

import android.content.Context
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.SettingsClient
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import me.alexpetrakov.cyclone.weather.data.DeviceLocationProvider
import me.alexpetrakov.cyclone.weather.data.WeatherProvider
import me.alexpetrakov.cyclone.weather.data.openweathermap.ForecastApi
import me.alexpetrakov.cyclone.weather.domain.repositories.DeviceLocator
import me.alexpetrakov.cyclone.weather.domain.repositories.WeatherRepository
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface WeatherDiModule {

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
        fun provideFusedLocationProviderClient(@ApplicationContext context: Context): FusedLocationProviderClient {
            return LocationServices.getFusedLocationProviderClient(context)
        }

        @Provides
        @Singleton
        fun provideSettingsClient(@ApplicationContext context: Context): SettingsClient {
            return LocationServices.getSettingsClient(context)
        }
    }
}