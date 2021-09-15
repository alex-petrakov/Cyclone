package me.alexpetrakov.cyclone.weather.domain

import com.github.kittinunf.result.Result
import me.alexpetrakov.cyclone.locations.domain.Coordinates

interface DeviceLocator {

    suspend fun getDeviceLocation(): Result<Coordinates, Fail>
}
