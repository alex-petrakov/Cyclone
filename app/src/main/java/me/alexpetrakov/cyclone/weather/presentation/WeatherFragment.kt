package me.alexpetrakov.cyclone.weather.presentation

import android.Manifest
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts.RequestPermission
import androidx.activity.result.contract.ActivityResultContracts.StartIntentSenderForResult
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.common.api.ResolvableApiException
import me.alexpetrakov.cyclone.BuildConfig
import me.alexpetrakov.cyclone.R
import me.alexpetrakov.cyclone.common.presentation.asString
import me.alexpetrakov.cyclone.databinding.FragmentWeatherBinding
import me.alexpetrakov.cyclone.weather.presentation.LocationRationaleDialog.Companion.ACTION_CANCEL
import me.alexpetrakov.cyclone.weather.presentation.LocationRationaleDialog.Companion.ACTION_CONTINUE
import me.alexpetrakov.cyclone.weather.presentation.LocationRationaleDialog.Companion.KEY_ACTION
import org.koin.androidx.viewmodel.ext.android.viewModel

class WeatherFragment : Fragment() {

    private val viewModel by viewModel<WeatherViewModel>()

    private var _binding: FragmentWeatherBinding? = null

    private val binding get() = _binding!!

    private val weatherAdapter = WeatherAdapter()

    private val permissionRequest =
        registerForActivityResult(RequestPermission()) { permissionIsGranted ->
            if (permissionIsGranted) {
                viewModel.onLocationAccessGranted()
            } else {
                viewModel.onLocationAccessDenied()
            }
        }

    private val failureResolutionRequest =
        registerForActivityResult(StartIntentSenderForResult()) { result ->
            when (result.resultCode) {
                Activity.RESULT_OK -> viewModel.onLocationRetrievalErrorResolved()
                else -> viewModel.onLocationRetrievalErrorNotResolved()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        childFragmentManager.setFragmentResultListener(
            LocationRationaleDialog.TAG,
            this
        ) { _, result ->
            val action = result.getString(KEY_ACTION) ?: return@setFragmentResultListener
            when (action) {
                ACTION_CONTINUE -> showSystemLocationPermissionRequest()
                ACTION_CANCEL -> viewModel.onLocationAccessDenied()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWeatherBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prepareViews()
        observeViewModel()
    }

    private fun prepareViews(): Unit = with(binding) {
        contentView.apply {
            recyclerView.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = weatherAdapter
            }
            swipeToRefreshLayout.apply {
                setProgressViewEndTarget(true, progressViewEndOffset)
                setOnRefreshListener { viewModel.onRefresh() }
            }
        }
        // TODO: Make the toolbar title clickable instead
        toolbar.setNavigationOnClickListener { viewModel.onOpenLocationPicker() }

        noConnectionView.setPrimaryAction { viewModel.onRetryAfterFailure() }

        noLocationAccessView.apply {
            setPrimaryAction { viewModel.onRetryAfterFailure() }
            setSecondaryAction { viewModel.onOpenAppSettings() }
        }

        noAvailableLocationView.apply {
            setPrimaryAction { viewModel.onRetryAfterFailure() }
            setSecondaryAction { viewModel.onOpenLocationSettings() }
        }
    }

    private fun observeViewModel(): Unit = with(viewModel) {
        toolbarViewState.observe(viewLifecycleOwner) { renderToolbar(it) }
        weatherViewState.observe(viewLifecycleOwner) { renderWeather(it) }
        viewEffect.observe(viewLifecycleOwner) { handle(it) }
    }

    private fun renderToolbar(viewState: ToolbarViewState): Unit = with(binding) {
        toolbar.title = viewState.title.asString(resources)
    }

    private fun renderWeather(viewState: WeatherViewState): Unit = with(binding) {
        when (viewState) {
            WeatherViewState.Loading -> {
                contentView.root.isVisible = false
                noConnectionView.isVisible = false
                noLocationAccessView.isVisible = false
                noAvailableLocationView.isVisible = false
                progressIndicator.show()
            }
            is WeatherViewState.Content -> {
                contentView.root.isVisible = true
                noConnectionView.isVisible = false
                noLocationAccessView.isVisible = false
                noAvailableLocationView.isVisible = false
                progressIndicator.hide()
                renderContent(viewState)
            }
            WeatherViewState.Error.NoConnection -> {
                contentView.root.isVisible = false
                noConnectionView.isVisible = true
                noLocationAccessView.isVisible = false
                noAvailableLocationView.isVisible = false
                progressIndicator.hide()
            }
            WeatherViewState.Error.NoAvailableLocation -> {
                contentView.root.isVisible = false
                noConnectionView.isVisible = false
                noLocationAccessView.isVisible = false
                noAvailableLocationView.isVisible = true
                progressIndicator.hide()
            }
            WeatherViewState.Error.NoLocationAccess -> {
                contentView.root.isVisible = false
                noConnectionView.isVisible = false
                noLocationAccessView.isVisible = true
                noAvailableLocationView.isVisible = false
                progressIndicator.hide()
            }
        }
    }

    private fun renderContent(viewState: WeatherViewState.Content) {
        with(binding.contentView) {
            if (swipeToRefreshLayout.isRefreshing != viewState.isRefreshing) {
                swipeToRefreshLayout.isRefreshing = viewState.isRefreshing
            }
            weatherAdapter.submitList(viewState.items)
        }
    }

    private fun handle(viewEffect: ViewEffect) {
        when (viewEffect) {
            ViewEffect.OpenAppSettings -> openAppSettings()
            ViewEffect.OpenLocationSettings -> openLocationSettings()
            ViewEffect.RequestLocationAccess -> requestLocationAccess()
            is ViewEffect.ResolveException -> tryToResolveException(viewEffect.throwable)
            ViewEffect.None -> {
            }
        }
    }

    private fun openLocationSettings() {
        try {
            val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(
                requireContext(),
                R.string.weather_error_activity_not_found,
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun openAppSettings() {
        try {
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                data = Uri.fromParts("package", BuildConfig.APPLICATION_ID, null)
            }
            startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(
                requireContext(),
                R.string.weather_error_activity_not_found,
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun requestLocationAccess() {
        when {
            locationPermissionIsGranted -> viewModel.onLocationAccessGranted()
            shouldShowPermissionRationale -> showLocationPermissionRationale()
            else -> showSystemLocationPermissionRequest()
        }
    }

    private fun tryToResolveException(cause: Throwable?) {
        if (cause != null && cause is ResolvableApiException) {
            failureResolutionRequest.launch(IntentSenderRequest.Builder(cause.resolution).build())
        }
    }

    private val locationPermissionIsGranted: Boolean
        get() {
            return ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        }

    private val shouldShowPermissionRationale: Boolean
        get() {
            return shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)
        }

    private fun showLocationPermissionRationale() {
        LocationRationaleDialog().show(childFragmentManager, LocationRationaleDialog.TAG)
    }

    private fun showSystemLocationPermissionRequest() {
        permissionRequest.launch(Manifest.permission.ACCESS_FINE_LOCATION)
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    companion object {
        fun newInstance() = WeatherFragment()
    }
}