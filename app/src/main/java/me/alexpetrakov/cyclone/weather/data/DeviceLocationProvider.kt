package me.alexpetrakov.cyclone.weather.data

import android.annotation.SuppressLint
import android.location.Location
import android.os.SystemClock
import com.github.kittinunf.result.Result
import com.github.kittinunf.result.flatMapError
import com.github.kittinunf.result.map
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.SettingsClient
import com.google.android.gms.tasks.CancellationTokenSource
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.tasks.await
import me.alexpetrakov.cyclone.locations.domain.model.Coordinates
import me.alexpetrakov.cyclone.weather.domain.model.Fail
import me.alexpetrakov.cyclone.weather.domain.repositories.DeviceLocator
import java.util.concurrent.TimeUnit.MINUTES

class DeviceLocationProvider(
    private val fusedLocationProvider: FusedLocationProviderClient,
    private val settingsClient: SettingsClient
) : DeviceLocator {

    override suspend fun getDeviceLocation(): Result<Coordinates, Fail> {
        return try {
            requireLocationSettings()
            getLastKnownLocation()
                .flatMapError { getCurrentLocation() }
                .map { Coordinates(it.latitude, it.longitude) }
        } catch (e: CancellationException) {
            throw e
        } catch (e: SecurityException) {
            Result.failure(Fail.LocationAccessDenied(e))
        } catch (e: ResolvableApiException) {
            Result.failure(Fail.LocationIsDisabled(e))
        } catch (e: Exception) {
            Result.failure(Fail.LocationIsNotAvailable(e))
        }
    }

    private suspend fun requireLocationSettings() {
        val locationRequest = LocationRequest.create().apply {
            priority = PRIORITY_HIGH_ACCURACY
        }
        val settingsRequest = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)
            .build()
        settingsClient.checkLocationSettings(settingsRequest).await()
    }

    @SuppressLint("MissingPermission")
    private suspend fun getLastKnownLocation(): Result<Location, Fail> {
        val location: Location? = fusedLocationProvider.lastLocation.await()
        return if (location != null && location.isFresh && location.isAccurate) {
            Result.success(location)
        } else {
            Result.failure(Fail.LocationIsNotAvailable(null))
        }
    }

    private val Location.isFresh: Boolean
        get() {
            return SystemClock.elapsedRealtimeNanos() - elapsedRealtimeNanos <= MINUTES.toNanos(5)
        }

    private val Location.isAccurate: Boolean
        get() {
            return hasAccuracy() && accuracy <= 1000f
        }

    @SuppressLint("MissingPermission")
    @OptIn(ExperimentalCoroutinesApi::class)
    private suspend fun getCurrentLocation(): Result<Location, Fail> {
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
}