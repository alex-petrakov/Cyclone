package me.alexpetrakov.cyclone.settings.presentation

import android.content.Context
import android.os.Bundle
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat

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

    private fun buildSpeedUnitsPreference(context: Context): Preference {
        return ListPreference(context).apply {
            key = "speed"
            title = "Speed units"
            dialogTitle = "Speed units"
            entries = arrayOf(
                "Kilometers per hour (km/h)",
                "Meters per second (m/s)",
                "Miles per hour (mph)"
            )
            entryValues = arrayOf("kilometers_per_hour", "meters_per_second", "miles_per_hour")
            value = "meters_per_second"
            summaryProvider = BasicListPreferenceSummaryProvider()
        }
    }

    private fun buildTemperatureUnitsPreference(context: Context): Preference {
        return ListPreference(context).apply {
            key = "temperature"
            title = "Temperature units"
            dialogTitle = "Temperature units"
            entries = arrayOf("Celsius (°C)", "Fahrenheit (°F)", "Kelvin")
            entryValues = arrayOf("celsius", "fahrenheit", "kelvin")
            value = "celsius"
            summaryProvider = BasicListPreferenceSummaryProvider()
        }
    }

    private fun buildDistanceUnitsPreference(context: Context): Preference {
        return ListPreference(context).apply {
            key = "distance"
            title = "Distance units"
            dialogTitle = "Distance units"
            entries = arrayOf("Kilometers (km)", "Miles (mi)")
            entryValues = arrayOf("kilometers", "miles")
            value = "kilometers"
            summaryProvider = BasicListPreferenceSummaryProvider()
        }
    }

    private fun buildPressureUnitsPreference(context: Context): Preference {
        return ListPreference(context).apply {
            key = "pressure"
            title = "Pressure units"
            dialogTitle = "Pressure units"
            entries = arrayOf(
                "Millibars (mb)",
                "Millimeters of mercury (mmHg)",
                "Inches of mercury (inHg)",
                "Hectopascals (hPa)",
                "Kilopascals (kPa)"
            )
            entryValues = arrayOf(
                "millibars",
                "millimeters_of_mercury",
                "inches_of_mercury",
                "hectopascals",
                "kilopascals"
            )
            value = "millimeters_of_mercury"
            summaryProvider = BasicListPreferenceSummaryProvider()
        }
    }

    class BasicListPreferenceSummaryProvider : Preference.SummaryProvider<ListPreference> {
        override fun provideSummary(preference: ListPreference): CharSequence {
            return preference.entry
        }
    }
}