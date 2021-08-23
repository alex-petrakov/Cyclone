package me.alexpetrakov.cyclone

import android.app.Application
import me.alexpetrakov.cyclone.common.commonModule
import me.alexpetrakov.cyclone.weather.weatherModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import timber.log.Timber

@Suppress("unused") // Used in AndroidManifest.xml
class CycloneApp : Application() {

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        startKoin {
            if (BuildConfig.DEBUG) {
                androidLogger(Level.DEBUG)
            }
            androidContext(this@CycloneApp)
            modules(commonModule, weatherModule)
        }
    }
}