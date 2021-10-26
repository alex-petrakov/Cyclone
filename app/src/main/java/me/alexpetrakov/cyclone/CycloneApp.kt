package me.alexpetrakov.cyclone

import android.app.Application
import timber.log.Timber

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