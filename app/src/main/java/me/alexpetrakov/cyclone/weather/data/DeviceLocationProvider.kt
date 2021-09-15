package me.alexpetrakov.cyclone.weather.data

import android.annotation.SuppressLint
import android.location.Location
import android.os.SystemClock
import com.github.kittinunf.result.Result
import com.github.kittinunf.result.flatMapError
import com.github.kittinunf.result.map
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY
import com.google.android.gms.tasks.CancellationTokenSource
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.tasks.await
import me.alexpetrakov.cyclone.locations.domain.Coordinates
import me.alexpetrakov.cyclone.weather.domain.DeviceLocator
import me.alexpetrakov.cyclone.weather.domain.Fail
import java.util.concurrent.TimeUnit.MINUTES

class DeviceLocationProvider(
    private val fusedLocationProvider: FusedLocationProviderClient
) : DeviceLocator {

    override suspend fun getDeviceLocation(): Result<Coordinates, Fail> {
        return try {
            getLastKnownLocation()
                .flatMapError { getCurrentLocation() }
                .map { Coordinates(it.latitude, it.longitude) }
        } catch (e: CancellationException) {
            throw e
        } catch (e: SecurityException) {
            Result.failure(Fail.NoLocationAccess(e))
        } catch (e: Exception) {
            Result.failure(Fail.NoAvailableLocation(e))
        }
    }

    @SuppressLint("MissingPermission")
    private suspend fun getLastKnownLocation(): Result<Location, Fail> {
        val location: Location? = fusedLocationProvider.lastLocation.await()
        return if (location != null && location.isFresh && location.isAccurate) {
            Result.success(location)
        } else {
            Result.failure(Fail.NoAvailableLocation(null))
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
            Result.failure(Fail.NoAvailableLocation())
        }
    }
}