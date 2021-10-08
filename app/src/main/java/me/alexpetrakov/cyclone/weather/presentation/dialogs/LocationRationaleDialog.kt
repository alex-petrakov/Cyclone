package me.alexpetrakov.cyclone.weather.presentation.dialogs

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import me.alexpetrakov.cyclone.R
import me.alexpetrakov.cyclone.common.presentation.extensions.parentViewModel
import me.alexpetrakov.cyclone.weather.presentation.RationaleOutcome
import me.alexpetrakov.cyclone.weather.presentation.WeatherViewModel

class LocationRationaleDialog : DialogFragment() {

    private val viewModel by parentViewModel<WeatherViewModel>()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.weather_allow_access_to_your_location)
            .setMessage(R.string.weather_location_permission_rationale_message)
            .setPositiveButton(R.string.app_action_continue) { _, _ ->
                viewModel.onLocationPermissionRationaleOutcome(RationaleOutcome.ACCEPTED)
            }
            .setNegativeButton(R.string.app_action_cancel) { _, _ ->
                viewModel.onLocationPermissionRationaleOutcome(RationaleOutcome.DENIED)
            }
            .create()
    }

    override fun onDismiss(dialog: DialogInterface) {
        viewModel.onLocationPermissionRationaleOutcome(RationaleOutcome.DENIED)
        super.onDismiss(dialog)
    }

    companion object {
        const val TAG = "LocationPermissionRationaleDialog"
    }
}