package me.alexpetrakov.cyclone

import android.app.Application
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
import timber.log.Timber
import javax.inject.Singleton

@Suppress("unused") // Used in AndroidManifest.xml
class CycloneApp : Application() {

    private var _appComponent: AppComponent? = null

    val appComponent get() = _appComponent!!

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        _appComponent = DaggerAppComponent.builder()
            .appContext(this)
            .build()
    }
}

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