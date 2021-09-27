package me.alexpetrakov.cyclone.weather.presentation

import androidx.annotation.StringRes
import me.alexpetrakov.cyclone.R
import me.alexpetrakov.cyclone.common.presentation.TextResource
import me.alexpetrakov.cyclone.units.domain.measurements.Measurement
import me.alexpetrakov.cyclone.units.domain.unitsofmeasure.*
import java.text.NumberFormat

interface MeasurementFormatter<U : UnitDimension<U>> {

    fun format(measurement: Measurement<U>, targetUnit: U): TextResource

    fun format(measurement: Measurement<U>): TextResource
}

abstract class AbstractFormatter<U : UnitDimension<U>>(protected val numberFormat: NumberFormat) :
    MeasurementFormatter<U> {

    override fun format(measurement: Measurement<U>, targetUnit: U): TextResource {
        val convertedMeasurement = measurement.convertTo(targetUnit)
        val templateResId = getTemplate(convertedMeasurement.unit)
        val formattedValue = numberFormat.format(convertedMeasurement.value)
        return TextResource.from(templateResId, formattedValue)
    }

    override fun format(measurement: Measurement<U>): TextResource {
        return format(measurement, measurement.unit)
    }

    open fun formatValue(value: Double): String {
        return numberFormat.format(value)
    }

    @StringRes
    abstract fun getTemplate(unit: U): Int
}

class TemperatureFormatter(numberFormat: NumberFormat = NumberFormat.getIntegerInstance()) :
    AbstractFormatter<TemperatureUnit>(numberFormat) {
    override fun getTemplate(unit: TemperatureUnit): Int {
        return when (unit) {
            TemperatureUnit.Celsius -> R.string.template_temperature_degree
            TemperatureUnit.Fahrenheit -> R.string.template_temperature_degree
            TemperatureUnit.Kelvin -> R.string.template_temperature_kelvin
        }
    }
}

class DistanceFormatter(numberFormat: NumberFormat = NumberFormat.getIntegerInstance()) :
    AbstractFormatter<LengthUnit>(numberFormat) {
    override fun getTemplate(unit: LengthUnit): Int {
        return when (unit) {
            LengthUnit.Meters -> R.string.template_length_meter
            LengthUnit.Kilometers -> R.string.template_length_kilometer
            LengthUnit.Miles -> R.string.template_length_mile
        }
    }
}

class SpeedFormatter(numberFormat: NumberFormat = NumberFormat.getIntegerInstance()) :
    AbstractFormatter<SpeedUnit>(numberFormat) {
    override fun getTemplate(unit: SpeedUnit): Int {
        return when (unit) {
            SpeedUnit.MetersPerSecond -> R.string.template_speed_meter_per_second
            SpeedUnit.KilometersPerHour -> R.string.template_speed_kilometer_per_hour
            SpeedUnit.MilesPerHour -> R.string.template_speed_mile_per_hour
        }
    }
}

class PressureFormatter(numberFormat: NumberFormat = NumberFormat.getIntegerInstance()) :
    AbstractFormatter<PressureUnit>(numberFormat) {
    override fun getTemplate(unit: PressureUnit): Int {
        return when (unit) {
            PressureUnit.Pascals -> R.string.template_pressure_pascal
            PressureUnit.Hectopascals -> R.string.template_pressure_hectopascal
            PressureUnit.Kilopascals -> R.string.template_pressure_kilopascal
            PressureUnit.Millibars -> R.string.template_pressure_millibar
            PressureUnit.MillimetersOfMercury -> R.string.template_pressure_mmhg
            PressureUnit.InchesOfMercury -> R.string.template_pressure_inhg
        }
    }
}