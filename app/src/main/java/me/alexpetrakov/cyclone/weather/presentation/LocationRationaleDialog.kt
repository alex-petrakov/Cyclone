package me.alexpetrakov.cyclone.weather.presentation

import android.app.Dialog
import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import me.alexpetrakov.cyclone.R

class LocationRationaleDialog : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.weather_allow_access_to_your_location)
            .setMessage(R.string.weather_location_permission_rationale_message)
            .setPositiveButton(R.string.app_action_continue) { _, _ ->
                setFragmentResult(TAG, bundleOf(KEY_ACTION to ACTION_CONTINUE))
            }
            .setNegativeButton(R.string.app_action_cancel) { _, _ ->
                setFragmentResult(TAG, bundleOf(KEY_ACTION to ACTION_CANCEL))
            }
            .setOnCancelListener {
                setFragmentResult(TAG, bundleOf(KEY_ACTION to ACTION_CANCEL))
            }.create()
    }

    companion object {
        const val TAG = "LocationPermissionRationaleDialog"
        const val KEY_ACTION = "Action"
        const val ACTION_CONTINUE = "Continue"
        const val ACTION_CANCEL = "Cancel"
    }
}