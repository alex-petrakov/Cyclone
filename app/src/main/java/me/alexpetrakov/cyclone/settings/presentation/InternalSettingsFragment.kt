package me.alexpetrakov.cyclone.settings.presentation

import android.content.Context
import android.os.Bundle
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import me.alexpetrakov.cyclone.R
import me.alexpetrakov.cyclone.units.data.UnitsDataStore
import me.alexpetrakov.cyclone.units.domain.UnitsLocale
import me.alexpetrakov.cyclone.units.domain.unitsofmeasure.LengthUnit
import me.alexpetrakov.cyclone.units.domain.unitsofmeasure.PressureUnit
import me.alexpetrakov.cyclone.units.domain.unitsofmeasure.SpeedUnit
import me.alexpetrakov.cyclone.units.domain.unitsofmeasure.TemperatureUnit

class InternalSettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        val context = preferenceManager.context
        val screen = preferenceManager.createPreferenceScreen(context)

        val preferences = listOf(
            buildTemperatureUnitsPreference(context),
            buildDistanceUnitsPreference(context),
            buildSpeedUnitsPreference(context),
            buildPressureUnitsPreference(context)
        )
        for (preference in preferences) {
            screen.addPreference(preference)
        }

        preferenceScreen = screen
    }

    private fun buildTemperatureUnitsPreference(context: Context): Preference {
        return ListPreference(context).apply {
            key = UnitsDataStore.PREF_KEY_TEMPERATURE_UNIT
            title = getString(R.string.settings_temperature_units)
            dialogTitle = title
            entries = TemperatureUnit.values().map { it.getLocalizedName(context) }.toTypedArray()
            entryValues = TemperatureUnit.values().map { it.symbol }.toTypedArray()
            value = UnitsLocale.getDefault().temperatureUnit.symbol
            summaryProvider = BasicListPreferenceSummaryProvider()
        }
    }

    private fun buildDistanceUnitsPreference(context: Context): Preference {
        return ListPreference(context).apply {
            key = UnitsDataStore.PREF_KEY_DISTANCE_UNIT
            title = getString(R.string.settings_distance_units)
            dialogTitle = title
            entries = LengthUnit.values().map { it.getLocalizedName(context) }.toTypedArray()
            entryValues = LengthUnit.values().map { it.symbol }.toTypedArray()
            value = UnitsLocale.getDefault().lengthUnit.symbol
            summaryProvider = BasicListPreferenceSummaryProvider()
        }
    }

    private fun buildSpeedUnitsPreference(context: Context): Preference {
        return ListPreference(context).apply {
            key = UnitsDataStore.PREF_KEY_SPEED_UNIT
            title = getString(R.string.settings_speed_units)
            dialogTitle = title
            entries = SpeedUnit.values().map { it.getLocalizedName(context) }.toTypedArray()
            entryValues = SpeedUnit.values().map { it.symbol }.toTypedArray()
            value = UnitsLocale.getDefault().speedUnit.symbol
            summaryProvider = BasicListPreferenceSummaryProvider()
        }
    }

    private fun buildPressureUnitsPreference(context: Context): Preference {
        return ListPreference(context).apply {
            key = UnitsDataStore.PREF_KEY_PRESSURE_UNIT
            title = getString(R.string.settings_pressure_units)
            dialogTitle = title
            entries = PressureUnit.values().map { it.getLocalizedName(context) }.toTypedArray()
            entryValues = PressureUnit.values().map { it.symbol }.toTypedArray()
            value = UnitsLocale.getDefault().pressureUnit.symbol
            summaryProvider = BasicListPreferenceSummaryProvider()
        }
    }

    class BasicListPreferenceSummaryProvider : Preference.SummaryProvider<ListPreference> {
        override fun provideSummary(preference: ListPreference): CharSequence {
            return preference.entry ?: ""
        }
    }
}

private fun TemperatureUnit.getLocalizedName(context: Context): String {
    val resId = when (this) {
        TemperatureUnit.Celsius -> R.string.settings_celsius
        TemperatureUnit.Fahrenheit -> R.string.settings_fahrenheit
        TemperatureUnit.Kelvin -> R.string.settings_kelvin
    }
    return context.getString(resId)
}

private fun LengthUnit.getLocalizedName(context: Context): String {
    val resId = when (this) {
        LengthUnit.Meters -> R.string.settings_meters
        LengthUnit.Kilometers -> R.string.settings_kilometers
        LengthUnit.Miles -> R.string.settings_miles
    }
    return context.getString(resId)
}

private fun SpeedUnit.getLocalizedName(context: Context): String {
    val resId = when (this) {
        SpeedUnit.MetersPerSecond -> R.string.settings_meters_per_second
        SpeedUnit.KilometersPerHour -> R.string.settings_kilometers_per_hour
        SpeedUnit.MilesPerHour -> R.string.settings_miles_per_hour
    }
    return context.getString(resId)
}

private fun PressureUnit.getLocalizedName(context: Context): String {
    val resId = when (this) {
        PressureUnit.Pascals -> R.string.settings_pascals
        PressureUnit.Hectopascals -> R.string.settings_hectopascals
        PressureUnit.Kilopascals -> R.string.settings_kilopascals
        PressureUnit.Millibars -> R.string.settings_millibars
        PressureUnit.MillimetersOfMercury -> R.string.settings_millimeters_of_mercur
        PressureUnit.InchesOfMercury -> R.string.settings_inches_of_mercury
    }
    return context.getString(resId)
}