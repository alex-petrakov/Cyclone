package me.alexpetrakov.cyclone

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import me.alexpetrakov.cyclone.common.CommonModule
import me.alexpetrakov.cyclone.locations.LocationsModule
import me.alexpetrakov.cyclone.locations.presentation.LocationsFragment
import me.alexpetrakov.cyclone.locationsearch.LocationSearchModule
import me.alexpetrakov.cyclone.locationsearch.presentation.LocationSearchFragment
import me.alexpetrakov.cyclone.settings.presentation.InternalSettingsFragment
import me.alexpetrakov.cyclone.settings.presentation.SettingsFragment
import me.alexpetrakov.cyclone.units.UnitsModule
import me.alexpetrakov.cyclone.weather.WeatherModule
import me.alexpetrakov.cyclone.weather.presentation.WeatherFragment
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        CommonModule::class,
        WeatherModule::class,
        LocationsModule::class,
        UnitsModule::class,
        LocationSearchModule::class
    ]
)
interface AppComponent {

    fun inject(hostActivity: HostActivity)

    fun inject(weatherFragment: WeatherFragment)

    fun inject(locationsFragment: LocationsFragment)

    fun inject(locationSearchFragment: LocationSearchFragment)

    fun inject(settingsFragment: SettingsFragment)

    fun inject(internalSettingsFragment: InternalSettingsFragment)

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun appContext(context: Context): Builder

        fun build(): AppComponent
    }
}