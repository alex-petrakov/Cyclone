package me.alexpetrakov.cyclone.units.data

import android.content.SharedPreferences
import com.fredporciuncula.flow.preferences.FlowSharedPreferences
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import me.alexpetrakov.cyclone.units.domain.PreferredUnits
import me.alexpetrakov.cyclone.units.domain.UnitsLocale
import me.alexpetrakov.cyclone.units.domain.UnitsRepository
import me.alexpetrakov.cyclone.units.domain.unitsofmeasure.LengthUnit
import me.alexpetrakov.cyclone.units.domain.unitsofmeasure.PressureUnit
import me.alexpetrakov.cyclone.units.domain.unitsofmeasure.SpeedUnit
import me.alexpetrakov.cyclone.units.domain.unitsofmeasure.TemperatureUnit

@OptIn(ExperimentalCoroutinesApi::class)
class UnitsDataStore(prefs: SharedPreferences) : UnitsRepository {

    private val flowPrefs = FlowSharedPreferences(prefs)

    private val unitsLocale = UnitsLocale.getDefault()

    private val temperatureUnitPreference = flowPrefs.getString(
        PREF_KEY_TEMPERATURE_UNIT,
        unitsLocale.temperatureUnit.symbol
    )

    private val distanceUnitPreference = flowPrefs.getString(
        PREF_KEY_DISTANCE_UNIT,
        unitsLocale.lengthUnit.symbol
    )

    private val speedUnitPreference = flowPrefs.getString(
        PREF_KEY_SPEED_UNIT,
        unitsLocale.speedUnit.symbol
    )

    private val pressureUnitPreference = flowPrefs.getString(
        PREF_KEY_PRESSURE_UNIT,
        unitsLocale.pressureUnit.symbol
    )

    private val temperatureUnitStream = temperatureUnitPreference.asFlow().map { symbol ->
        TemperatureUnit.from(symbol) ?: unitsLocale.temperatureUnit
    }

    private val distanceUnitStream = distanceUnitPreference.asFlow().map { symbol ->
        LengthUnit.from(symbol) ?: unitsLocale.lengthUnit
    }

    private val speedUnitStream = speedUnitPreference.asFlow().map { symbol ->
        SpeedUnit.from(symbol) ?: unitsLocale.speedUnit
    }

    private val pressureUnitStream = pressureUnitPreference.asFlow().map { symbol ->
        PressureUnit.from(symbol) ?: unitsLocale.pressureUnit
    }

    override suspend fun getPreferredUnits(): PreferredUnits {
        return PreferredUnits(
            temperatureUnitStream.first(),
            distanceUnitStream.first(),
            speedUnitStream.first(),
            pressureUnitStream.first()
        )
    }

    override fun observePreferredUnits(): Flow<PreferredUnits> {
        return combine(
            temperatureUnitStream,
            distanceUnitStream,
            speedUnitStream,
            pressureUnitStream
        ) { temperatureUnit, lengthUnit, speedUnit, pressureUnit ->
            PreferredUnits(temperatureUnit, lengthUnit, speedUnit, pressureUnit)
        }
    }

    companion object {
        const val PREF_KEY_TEMPERATURE_UNIT = "temp_unit"
        const val PREF_KEY_DISTANCE_UNIT = "dist_unit"
        const val PREF_KEY_SPEED_UNIT = "speed_unit"
        const val PREF_KEY_PRESSURE_UNIT = "pressure_unit"
    }
}