package me.alexpetrakov.cyclone

import com.github.terrakok.cicerone.androidx.FragmentScreen
import me.alexpetrakov.cyclone.weather.presentation.WeatherFragment

object AppScreens {

    fun weather() = FragmentScreen { WeatherFragment.newInstance() }
}