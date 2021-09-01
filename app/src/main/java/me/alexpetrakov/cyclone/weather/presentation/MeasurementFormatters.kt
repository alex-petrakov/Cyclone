package me.alexpetrakov.cyclone.weather.presentation

import androidx.annotation.StringRes
import me.alexpetrakov.cyclone.R
import me.alexpetrakov.cyclone.common.TextResource
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
            TemperatureUnit.celsius -> R.string.template_temperature_degree
            TemperatureUnit.fahrenheit -> R.string.template_temperature_degree
            TemperatureUnit.kelvin -> R.string.template_temperature_kelvin
            else -> throw IllegalStateException("Unexpected unit $unit")
        }
    }
}

class DistanceFormatter(numberFormat: NumberFormat = NumberFormat.getIntegerInstance()) :
    AbstractFormatter<LengthUnit>(numberFormat) {
    override fun getTemplate(unit: LengthUnit): Int {
        return when (unit) {
            LengthUnit.meter -> R.string.template_length_meter
            LengthUnit.kilometer -> R.string.template_length_kilometer
            LengthUnit.mile -> R.string.template_length_mile
            else -> throw IllegalStateException("Unexpected unit $unit")
        }
    }
}

class SpeedFormatter(numberFormat: NumberFormat = NumberFormat.getIntegerInstance()) :
    AbstractFormatter<SpeedUnit>(numberFormat) {
    override fun getTemplate(unit: SpeedUnit): Int {
        return when (unit) {
            SpeedUnit.meterPerSecond -> R.string.template_speed_meter_per_second
            SpeedUnit.kilometerPerHour -> R.string.template_speed_kilometer_per_hour
            SpeedUnit.milePerHour -> R.string.template_speed_mile_per_hour
            else -> throw IllegalStateException("Unexpected unit $unit")
        }
    }
}

class PressureFormatter(numberFormat: NumberFormat = NumberFormat.getIntegerInstance()) :
    AbstractFormatter<PressureUnit>(numberFormat) {
    override fun getTemplate(unit: PressureUnit): Int {
        return when (unit) {
            PressureUnit.pascal -> R.string.template_pressure_pascal
            PressureUnit.hectopascal -> R.string.template_pressure_hectopascal
            PressureUnit.kilopascal -> R.string.template_pressure_kilopascal
            PressureUnit.millibar -> R.string.template_pressure_millibar
            PressureUnit.millimeterOfMercury -> R.string.template_pressure_mmhg
            PressureUnit.inchOfMercury -> R.string.template_pressure_inhg
            else -> throw IllegalStateException("Unexpected unit $unit")
        }
    }
}