package me.alexpetrakov.cyclone.weather.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import me.alexpetrakov.cyclone.R
import me.alexpetrakov.cyclone.common.TextResource
import me.alexpetrakov.cyclone.common.asTextResource
import me.alexpetrakov.cyclone.locations.domain.Location
import me.alexpetrakov.cyclone.locations.domain.LocationsRepository
import me.alexpetrakov.cyclone.weather.domain.*
import java.text.NumberFormat
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*

class WeatherViewModel(
    private val weatherRepository: WeatherRepository,
    private val locationsRepository: LocationsRepository
) : ViewModel() {

    private val _weatherViewState = MutableLiveData<WeatherViewState>().apply {
        value = WeatherViewState.Loading
    }

    val weatherViewState get() = _weatherViewState

    private val _toolbarViewState = MutableLiveData<ToolbarViewState>().apply {
        value = ToolbarViewState("".asTextResource())
    }

    val toolbarViewState get() = _toolbarViewState

    private val dateFormatter = DateTimeFormatter.ofPattern("EEEE d")

    private val timeFormatter = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)

    private val percentFormatter = NumberFormat.getPercentInstance().apply {
        minimumFractionDigits = 0
        maximumFractionDigits = 0
    }

    init {
        viewModelScope.launch {
            locationsRepository.observeSelectedLocation().onEach { location ->
                _toolbarViewState.value = ToolbarViewState(location.toUiModel())
                _weatherViewState.value = WeatherViewState.Loading
                loadForecast()
            }.collect()
        }
    }

    fun onRetryAfterFailure() {
        _weatherViewState.value = WeatherViewState.Loading
        loadForecast()
    }

    fun onRefresh() {
        val currentViewState = _weatherViewState.value
        check(currentViewState is WeatherViewState.Content) {
            "Refresh can be triggered only from ViewState.Content state"
        }
        _weatherViewState.value = currentViewState.copy(isRefreshing = true)
        loadForecast()
    }

    private fun loadForecast() {
        viewModelScope.launch {
            val selectedLocation = locationsRepository.getSelectedLocation()
            _weatherViewState.value = weatherRepository.getWeather(selectedLocation)
                .fold({ mapWeatherToViewState(it) }, ::mapFailureToViewState)
        }
    }

    private suspend fun mapWeatherToViewState(weather: Weather): WeatherViewState {
        return withContext(Dispatchers.Default) {
            WeatherViewState.Content(false, weather.toUiModel())
        }
    }

    private fun mapFailureToViewState(throwable: Throwable): WeatherViewState {
        return WeatherViewState.Error
    }

    private fun Weather.toUiModel(): List<DisplayableItem> {
        return listOf(
            currentConditions.toUiModel(),
            DisplayableItem.HeaderUi(TextResource.from(R.string.weather_today_title)),
            hourlyForecast.toUiModel(),
            DisplayableItem.HeaderUi(TextResource.from(R.string.weather_this_week_title)),
        ) + dailyForecast.toUiModel()
    }

    private fun CurrentConditions.toUiModel(): DisplayableItem.CurrentConditionsUi {
        return DisplayableItem.CurrentConditionsUi(
            temperature.value.toString().asTextResource(),
            overallConditions[0].title.asTextResource(),
            temperature.value.toString().asTextResource(),
            overallConditions[0].icon.toUiModel(),
            "${wind.speed.value}".asTextResource(),
            "${pressure.value}".asTextResource(),
            percentFormatter.format(humidity).asTextResource(),
            "${dewPoint.value}".asTextResource(),
            "${visibility.value}".asTextResource(),
            "$uvIndex".asTextResource()
        )
    }

    private fun List<HourConditions>.toUiModel(): DisplayableItem.HourlyForecastUi {
        return DisplayableItem.HourlyForecastUi(map { it.toUiModel() })
    }

    private fun HourConditions.toUiModel(): HourConditionsUi {
        return HourConditionsUi(
            timeFormatter.format(localTime).asTextResource(),
            temperature.value.toString().asTextResource(),
            percentFormatter.format(precipitationChance).asTextResource(),
            precipitationChance > 0,
            overallConditions[0].title.asTextResource(),
            overallConditions[0].icon.toUiModel()
        )
    }

    private fun List<DayConditions>.toUiModel(): List<DisplayableItem.DayConditionsUi> {
        return map { it.toUiModel() }
    }

    private fun DayConditions.toUiModel(): DisplayableItem.DayConditionsUi {
        return DisplayableItem.DayConditionsUi(
            dateFormatter.format(localDate).capitalizingFirstLetter().asTextResource(),
            tempLow.value.toString().asTextResource(),
            tempHigh.value.toString().asTextResource(),
            overallConditions[0].title.asTextResource(),
            overallConditions[0].icon.toUiModel(),
            percentFormatter.format(precipitationChance).asTextResource(),
            precipitationChance > 0
        )
    }

    private fun Icon.toUiModel(): IconUi {
        return when (this) {
            Icon.CLEAR -> IconUi.CLEAR
            Icon.FEW_CLOUDS -> IconUi.FEW_CLOUDS
            Icon.SCATTERED_CLOUDS -> IconUi.SCATTERED_CLOUDS
            Icon.BROKEN_CLOUDS -> IconUi.BROKEN_CLOUDS
            Icon.RAIN_SHOWER -> IconUi.RAIN_SHOWER
            Icon.RAIN -> IconUi.RAIN
            Icon.THUNDERSTORM -> IconUi.THUNDERSTORM
            Icon.SNOW -> IconUi.SNOW
            Icon.MIST -> IconUi.MIST
        }
    }

    private fun Location.toUiModel(): TextResource {
        return when (this) {
            Location.CurrentLocation -> TextResource.from(R.string.app_current_location)
            is Location.SavedLocation -> TextResource.from(name)
        }
    }
}

private fun String.capitalizingFirstLetter(): String {
    return this.replaceFirstChar { char ->
        if (char.isLowerCase()) {
            char.titlecase(Locale.getDefault())
        } else {
            char.toString()
        }
    }
}