package me.alexpetrakov.cyclone.units.data

import android.content.SharedPreferences
import com.fredporciuncula.flow.preferences.FlowSharedPreferences
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import me.alexpetrakov.cyclone.units.domain.model.PreferredUnits
import me.alexpetrakov.cyclone.units.domain.model.UnitsLocale
import me.alexpetrakov.cyclone.units.domain.model.unitsofmeasure.LengthUnit
import me.alexpetrakov.cyclone.units.domain.model.unitsofmeasure.PressureUnit
import me.alexpetrakov.cyclone.units.domain.model.unitsofmeasure.SpeedUnit
import me.alexpetrakov.cyclone.units.domain.model.unitsofmeasure.TemperatureUnit
import me.alexpetrakov.cyclone.units.domain.repositories.UnitsRepository
import me.alexpetrakov.cyclone.units.domain.repositories.UnitsRepository.Companion.PREF_KEY_DISTANCE_UNIT
import me.alexpetrakov.cyclone.units.domain.repositories.UnitsRepository.Companion.PREF_KEY_PRESSURE_UNIT
import me.alexpetrakov.cyclone.units.domain.repositories.UnitsRepository.Companion.PREF_KEY_SPEED_UNIT
import me.alexpetrakov.cyclone.units.domain.repositories.UnitsRepository.Companion.PREF_KEY_TEMPERATURE_UNIT

@OptIn(ExperimentalCoroutinesApi::class)
class UnitsDataStore(prefs: SharedPreferences) : UnitsRepository {

    override val temperatureUnits: List<TemperatureUnit>
        get() = TemperatureUnit.values().toList()

    override val defaultTemperatureUnit: TemperatureUnit
        get() = defaultUnitsLocale.temperatureUnit

    override val distanceUnits: List<LengthUnit>
        get() = LengthUnit.values().toList()

    override val defaultDistanceUnit: LengthUnit
        get() = defaultUnitsLocale.lengthUnit

    override val speedUnits: List<SpeedUnit>
        get() = SpeedUnit.values().toList()

    override val defaultSpeedUnit: SpeedUnit
        get() = defaultUnitsLocale.speedUnit

    override val pressureUnits: List<PressureUnit>
        get() = PressureUnit.values().toList()

    override val defaultPressureUnit: PressureUnit
        get() = defaultUnitsLocale.pressureUnit

    private val flowPrefs = FlowSharedPreferences(prefs)

    private val defaultUnitsLocale = UnitsLocale.getDefault()

    private val temperatureUnitPreference = flowPrefs.getString(
        PREF_KEY_TEMPERATURE_UNIT,
        defaultUnitsLocale.temperatureUnit.symbol
    )

    private val distanceUnitPreference = flowPrefs.getString(
        PREF_KEY_DISTANCE_UNIT,
        defaultUnitsLocale.lengthUnit.symbol
    )

    private val speedUnitPreference = flowPrefs.getString(
        PREF_KEY_SPEED_UNIT,
        defaultUnitsLocale.speedUnit.symbol
    )

    private val pressureUnitPreference = flowPrefs.getString(
        PREF_KEY_PRESSURE_UNIT,
        defaultUnitsLocale.pressureUnit.symbol
    )

    private val temperatureUnitStream = temperatureUnitPreference.asFlow().map { symbol ->
        TemperatureUnit.from(symbol) ?: defaultTemperatureUnit
    }

    private val distanceUnitStream = distanceUnitPreference.asFlow().map { symbol ->
        LengthUnit.from(symbol) ?: defaultDistanceUnit
    }

    private val speedUnitStream = speedUnitPreference.asFlow().map { symbol ->
        SpeedUnit.from(symbol) ?: defaultSpeedUnit
    }

    private val pressureUnitStream = pressureUnitPreference.asFlow().map { symbol ->
        PressureUnit.from(symbol) ?: defaultPressureUnit
    }

    override suspend fun getPreferredUnits(): PreferredUnits {
        return PreferredUnits(
            temperatureUnitStream.first(),
            distanceUnitStream.first(),
            speedUnitStream.first(),
            pressureUnitStream.first()
        )
    }

    override fun getPreferredUnitsStream(): Flow<PreferredUnits> {
        return combine(
            temperatureUnitStream,
            distanceUnitStream,
            speedUnitStream,
            pressureUnitStream
        ) { temperatureUnit, lengthUnit, speedUnit, pressureUnit ->
            PreferredUnits(temperatureUnit, lengthUnit, speedUnit, pressureUnit)
        }
    }
}