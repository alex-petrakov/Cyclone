package me.alexpetrakov.cyclone.locations.presentation.dialogs

import android.app.Dialog
import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import me.alexpetrakov.cyclone.R
import me.alexpetrakov.cyclone.common.presentation.extensions.parentViewModel
import me.alexpetrakov.cyclone.locations.presentation.LocationsViewModel

class RemovalConfirmationDialog : DialogFragment() {

    private val viewModel by parentViewModel<LocationsViewModel>()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val title = requireArguments().getStringOrThrow(ARG_TITLE)
        val locationId = requireArguments().getIntOrThrow(ARG_LOCATION_ID)
        return MaterialAlertDialogBuilder(requireContext())
            .setTitle(title)
            .setPositiveButton(R.string.app_action_remove) { _, _ ->
                viewModel.onConfirmLocationRemoval(locationId)
            }
            .setNegativeButton(R.string.app_action_cancel) { _, _ ->
                // Do nothing
            }
            .create()
    }

    private fun Bundle.getStringOrThrow(key: String): String {
        return getString(key)
            ?: throw IllegalStateException("Can't find a string with the given key: $key")
    }

    private fun Bundle.getIntOrThrow(key: String): Int {
        require(containsKey(key)) { "Can't find an argument for the specified key: $key" }
        return getInt(key)
    }

    companion object {
        const val TAG = "RemovalConfirmationDialog"
        private const val ARG_TITLE = "Title"
        private const val ARG_LOCATION_ID = "ItemId"

        fun newInstance(locationId: Int, confirmationText: String): RemovalConfirmationDialog {
            return RemovalConfirmationDialog().apply {
                arguments = bundleOf(
                    ARG_TITLE to confirmationText,
                    ARG_LOCATION_ID to locationId
                )
            }
        }
    }
}
