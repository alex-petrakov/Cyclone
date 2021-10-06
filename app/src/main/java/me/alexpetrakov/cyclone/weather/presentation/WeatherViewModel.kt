package me.alexpetrakov.cyclone.weather.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.terrakok.cicerone.Router
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import me.alexpetrakov.cyclone.AppScreens
import me.alexpetrakov.cyclone.R
import me.alexpetrakov.cyclone.common.presentation.SingleLiveEvent
import me.alexpetrakov.cyclone.common.presentation.TextResource
import me.alexpetrakov.cyclone.common.presentation.asTextResource
import me.alexpetrakov.cyclone.common.presentation.extensions.withCapitalizedFirstChar
import me.alexpetrakov.cyclone.locations.domain.Location
import me.alexpetrakov.cyclone.locations.domain.LocationsRepository
import me.alexpetrakov.cyclone.units.domain.PreferredUnits
import me.alexpetrakov.cyclone.units.domain.UnitsRepository
import me.alexpetrakov.cyclone.weather.domain.*
import java.text.NumberFormat
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

class WeatherViewModel(
    private val getWeather: GetWeather,
    private val locationsRepository: LocationsRepository,
    private val unitsRepository: UnitsRepository,
    private val router: Router
) : ViewModel() {

    private val _weatherViewState = MutableLiveData<WeatherViewState>().apply {
        value = WeatherViewState.Loading
    }

    val weatherViewState get() = _weatherViewState

    private val _toolbarViewState = MutableLiveData<ToolbarViewState>().apply {
        value = ToolbarViewState("".asTextResource())
    }

    val toolbarViewState get() = _toolbarViewState

    private val _viewEffect = SingleLiveEvent<ViewEffect>()

    val viewEffect: LiveData<ViewEffect> get() = _viewEffect

    private val dateFormatter = DateTimeFormatter.ofPattern("EEEE d")

    private val timeFormatter = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)

    private val percentFormatter = NumberFormat.getPercentInstance().apply {
        minimumFractionDigits = 0
        maximumFractionDigits = 0
    }

    private val temperatureFormatter = TemperatureFormatter()

    private val distanceFormatter = DistanceFormatter()

    private val speedFormatter = SpeedFormatter()

    private val pressureFormatter = PressureFormatter()

    init {
        val selectedLocation = locationsRepository.getSelectedLocationStream()
        val preferredUnits = unitsRepository.getPreferredUnitsStream()
        combine(selectedLocation, preferredUnits) { location, units -> location to units }
            .onEach { (location, _) ->
                _toolbarViewState.value = ToolbarViewState(location.toUiModel())
                showLoadingAndLoadForecast()
            }
            .launchIn(viewModelScope)
    }

    fun onRetryAfterFailure() {
        showLoadingAndLoadForecast()
    }

    fun onRefresh() {
        val currentViewState = _weatherViewState.value
        check(currentViewState is WeatherViewState.Content) {
            "Refresh can be triggered only from ViewState.Content state"
        }
        showRefreshingAndLoadForecast(currentViewState)
    }

    fun onOpenLocationPicker() {
        router.navigateTo(AppScreens.locations())
    }

    fun onOpenSettings() {
        router.navigateTo(AppScreens.settings())
    }

    fun onOpenAppSettings() {
        _viewEffect.value = ViewEffect.OpenAppSettings
    }

    fun onOpenLocationSettings() {
        _viewEffect.value = ViewEffect.OpenLocationSettings
    }

    fun onLocationPermissionCheckResult(result: PermissionCheckResult) {
        when (result) {
            PermissionCheckResult.PERMISSION_IS_GRANTED -> showLoadingAndLoadForecast()
            PermissionCheckResult.PERMISSION_IS_NOT_GRANTED -> {
                _viewEffect.value = ViewEffect.ShowLocationPermissionRequest
            }
            PermissionCheckResult.RATIONALE_REQUIRED -> {
                _viewEffect.value = ViewEffect.ShowLocationPermissionRationale
            }
        }
    }

    fun onLocationPermissionRationaleOutcome(rationaleOutcome: RationaleOutcome) {
        when (rationaleOutcome) {
            RationaleOutcome.ACCEPTED -> {
                _viewEffect.value = ViewEffect.ShowLocationPermissionRequest
            }
            RationaleOutcome.DENIED -> {
                _weatherViewState.value = WeatherViewState.Error.NoLocationAccess
            }
        }
    }

    fun onLocationAccessGranted() {
        showLoadingAndLoadForecast()
    }

    fun onLocationAccessDenied() {
        _weatherViewState.value = WeatherViewState.Error.NoLocationAccess
    }

    fun onLocationRetrievalErrorResolved() {
        showLoadingAndLoadForecast()
    }

    fun onLocationRetrievalErrorNotResolved() {
        _weatherViewState.value = WeatherViewState.Error.NoAvailableLocation
    }

    private fun showLoadingAndLoadForecast() {
        _weatherViewState.value = WeatherViewState.Loading
        loadForecast()
    }

    private fun showRefreshingAndLoadForecast(viewState: WeatherViewState.Content) {
        _weatherViewState.value = viewState.copy(isRefreshing = true)
        loadForecast()
    }

    private fun loadForecast() {
        viewModelScope.launch {
            val weather = getWeather(locationsRepository.getSelectedLocation())
            _weatherViewState.value = weather.fold(
                { mapWeatherToViewState(it) },
                ::mapFailureToViewState
            )
            _viewEffect.value = weather.fold(::mapWeatherToViewEffect, ::mapFailureToViewEffect)
        }
    }

    private suspend fun mapWeatherToViewState(weather: Weather): WeatherViewState {
        val preferredUnits = unitsRepository.getPreferredUnits()
        return withContext(Dispatchers.Default) {
            WeatherViewState.Content(false, weather.toUiModel(preferredUnits))
        }
    }

    private fun mapFailureToViewState(fail: Fail): WeatherViewState {
        return when (fail) {
            is Fail.LocationAccessDenied -> WeatherViewState.Loading
            is Fail.LocationIsDisabled -> WeatherViewState.Loading
            is Fail.LocationIsNotAvailable -> WeatherViewState.Error.NoAvailableLocation
            is Fail.NoConnection -> WeatherViewState.Error.NoConnection
        }
    }

    private fun mapWeatherToViewEffect(weather: Weather): ViewEffect {
        return ViewEffect.None
    }

    private fun mapFailureToViewEffect(fail: Fail): ViewEffect {
        return when (fail) {
            is Fail.LocationAccessDenied -> ViewEffect.CheckLocationAccess
            is Fail.LocationIsDisabled -> ViewEffect.ResolveException(fail.cause)
            else -> ViewEffect.None
        }
    }

    private fun Weather.toUiModel(preferredUnits: PreferredUnits): List<DisplayableItem> {
        return listOf(
            currentConditions.toUiModel(preferredUnits),
            DisplayableItem.HeaderUi(TextResource.from(R.string.weather_today_title)),
            hourlyForecast.toUiModel(preferredUnits),
            DisplayableItem.HeaderUi(TextResource.from(R.string.weather_this_week_title)),
        ) + dailyForecast.toUiModel(preferredUnits)
    }

    private fun CurrentConditions.toUiModel(preferredUnits: PreferredUnits): DisplayableItem.CurrentConditionsUi {
        val feelsLikeTemperature =
            temperatureFormatter.format(feelsLike, preferredUnits.temperatureUnit)
        val windValue = speedFormatter.format(wind.speed, preferredUnits.speedUnit)
        val pressureValue = pressureFormatter.format(pressure, preferredUnits.pressureUnit)
        val humidityValue = percentFormatter.format(humidity)
        val dewPointValue = temperatureFormatter.format(dewPoint, preferredUnits.temperatureUnit)
        val visibilityValue = distanceFormatter.format(visibility, preferredUnits.lengthUnit)
        val uvIndexValue = uvIndex.toString()
        return DisplayableItem.CurrentConditionsUi(
            temperatureFormatter.format(temperature, preferredUnits.temperatureUnit),
            overallConditions.name.asTextResource(),
            TextResource.from(R.string.weather_template_feels_like, feelsLikeTemperature),
            overallConditions.icon.toUiModel(),
            TextResource.from(R.string.weather_template_wind, windValue),
            TextResource.from(R.string.weather_template_pressure, pressureValue),
            TextResource.from(R.string.weather_template_humidity, humidityValue, dewPointValue),
            TextResource.from(R.string.weather_template_visibility, visibilityValue),
            TextResource.from(R.string.weather_template_uv_index, uvIndexValue)
        )
    }

    private fun List<HourConditions>.toUiModel(preferredUnits: PreferredUnits): DisplayableItem.HourlyForecastUi {
        val currentHourConditions = take(1).map { currentHourConditions ->
            currentHourConditions.toUiModel(preferredUnits)
                .copy(time = TextResource.from(R.string.weather_now))
        }
        val otherHoursConditions = drop(1).map { it.toUiModel(preferredUnits) }
        return DisplayableItem.HourlyForecastUi(currentHourConditions + otherHoursConditions)
    }

    private fun HourConditions.toUiModel(preferredUnits: PreferredUnits): HourConditionsUi {
        return HourConditionsUi(
            timeFormatter.format(localTime).asTextResource(),
            temperatureFormatter.format(temperature, preferredUnits.temperatureUnit),
            percentFormatter.format(precipitationChance).asTextResource(),
            precipitationChance > 0,
            overallConditions.name.asTextResource(),
            overallConditions.icon.toUiModel()
        )
    }

    private fun List<DayConditions>.toUiModel(preferredUnits: PreferredUnits): List<DisplayableItem.DayConditionsUi> {
        val todayConditions = take(1).map { todayConditions ->
            todayConditions.toUiModel(preferredUnits)
                .copy(date = TextResource.from(R.string.weather_today))
        }
        val otherDaysConditions = drop(1).map { dayConditions ->
            dayConditions.toUiModel(preferredUnits)
        }
        return todayConditions + otherDaysConditions
    }

    private fun DayConditions.toUiModel(preferredUnits: PreferredUnits): DisplayableItem.DayConditionsUi {
        return DisplayableItem.DayConditionsUi(
            dateFormatter.format(localDate).withCapitalizedFirstChar().asTextResource(),
            temperatureFormatter.format(tempLow, preferredUnits.temperatureUnit),
            temperatureFormatter.format(tempHigh, preferredUnits.temperatureUnit),
            overallConditions.name.asTextResource(),
            overallConditions.icon.toUiModel(),
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
            is Location.StoredLocation -> TextResource.from(name)
        }
    }
}

enum class RationaleOutcome {
    ACCEPTED,
    DENIED
}

enum class PermissionCheckResult {
    PERMISSION_IS_GRANTED,
    PERMISSION_IS_NOT_GRANTED,
    RATIONALE_REQUIRED
}