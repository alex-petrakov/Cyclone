package me.alexpetrakov.cyclone.weather.data

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.SystemClock
import androidx.core.content.ContextCompat
import com.github.kittinunf.result.Result
import com.github.kittinunf.result.flatMap
import com.github.kittinunf.result.flatMapError
import com.github.kittinunf.result.map
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY
import com.google.android.gms.tasks.CancellationTokenSource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.tasks.await
import me.alexpetrakov.cyclone.locations.domain.model.Coordinates
import me.alexpetrakov.cyclone.weather.domain.model.Fail
import me.alexpetrakov.cyclone.weather.domain.repositories.DeviceLocator
import java.util.concurrent.TimeUnit.MINUTES
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DeviceLocationProvider @Inject constructor(
    private val context: Context,
    private val fusedLocationProvider: FusedLocationProviderClient,
    private val settingsClient: SettingsClient
) : DeviceLocator {

    override suspend fun getDeviceLocation(): Result<Coordinates, Fail> {
        return checkLocationPermission()
            .flatMap { checkLocationSettings() }
            .flatMap { getAnyFreshLocation() }
    }

    private fun checkLocationPermission(): Result<Unit, Fail> {
        val permissionIsGranted = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        return if (permissionIsGranted) {
            Result.success(Unit)
        } else {
            Result.failure(Fail.LocationAccessDenied())
        }
    }

    private suspend fun checkLocationSettings(): Result<LocationSettingsResponse, Fail> {
        val locationRequest = LocationRequest.create().apply {
            priority = PRIORITY_HIGH_ACCURACY
        }
        val settingsRequest = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)
            .build()
        return try {
            val response = settingsClient.checkLocationSettings(settingsRequest).await()
            Result.success(response)
        } catch (e: ResolvableApiException) {
            Result.failure(Fail.LocationIsDisabled(e))
        }
    }

    private suspend fun getAnyFreshLocation(): Result<Coordinates, Fail> {
        return getLastKnownLocation()
            .flatMapError { getCurrentLocation() }
            .map { Coordinates(it.latitude, it.longitude) }
    }

    private suspend fun getLastKnownLocation(): Result<Location, Fail> {
        return try {
            getLastKnownLocationOrThrow()
        } catch (e: SecurityException) {
            Result.failure(Fail.LocationAccessDenied(e))
        }
    }

    @SuppressLint("MissingPermission")
    private suspend fun getLastKnownLocationOrThrow(): Result<Location, Fail.LocationIsNotAvailable> {

        fun Location.isFresh(): Boolean {
            return (SystemClock.elapsedRealtimeNanos() - elapsedRealtimeNanos) <= LOCATION_SHELF_LIFE_IN_NANOS
        }

        fun Location.isAccurate(): Boolean {
            return hasAccuracy() && accuracy <= 1000f
        }

        val location: Location? = fusedLocationProvider.lastLocation.await()
        return if (location != null && location.isFresh() && location.isAccurate()) {
            Result.success(location)
        } else {
            Result.failure(Fail.LocationIsNotAvailable(null))
        }
    }

    private suspend fun getCurrentLocation(): Result<Location, Fail> {
        return try {
            getCurrentLocationOrThrow()
        } catch (e: SecurityException) {
            Result.failure(Fail.LocationAccessDenied(e))
        }
    }

    @SuppressLint("MissingPermission")
    @OptIn(ExperimentalCoroutinesApi::class)
    private suspend fun getCurrentLocationOrThrow(): Result<Location, Fail.LocationIsNotAvailable> {
        val cancellationTokenSource = CancellationTokenSource()
        val location: Location? = fusedLocationProvider.getCurrentLocation(
            PRIORITY_HIGH_ACCURACY,
            cancellationTokenSource.token
        ).await(cancellationTokenSource)
        return if (location != null) {
            Result.success(location)
        } else {
            Result.failure(Fail.LocationIsNotAvailable())
        }
    }

    companion object {
        private val LOCATION_SHELF_LIFE_IN_NANOS = MINUTES.toNanos(5L)
    }
}