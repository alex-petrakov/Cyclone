package me.alexpetrakov.cyclone

import com.github.terrakok.cicerone.androidx.FragmentScreen
import me.alexpetrakov.cyclone.locations.presentation.LocationsFragment
import me.alexpetrakov.cyclone.locationsearch.presentation.LocationSearchFragment
import me.alexpetrakov.cyclone.weather.presentation.WeatherFragment

object AppScreens {

    fun weather() = FragmentScreen { WeatherFragment.newInstance() }

    fun locations() = FragmentScreen { LocationsFragment.newInstance() }

    fun locationSearch() = FragmentScreen { LocationSearchFragment.newInstance() }
}