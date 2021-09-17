package me.alexpetrakov.cyclone.locations.presentation

import android.app.Dialog
import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import me.alexpetrakov.cyclone.R

class RemovalConfirmationDialog : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val title = requireArguments().getStringOrThrow(ARG_TITLE)
        val locationId = requireArguments().getIntOrThrow(ARG_LOCATION_ID)
        return MaterialAlertDialogBuilder(requireContext())
            .setTitle(title)
            .setPositiveButton(R.string.app_action_remove) { _, _ ->
                confirmRemoval(locationId)
            }
            .setNegativeButton(R.string.app_action_cancel) { _, _ ->
                cancelRemoval(locationId)
            }
            .setOnCancelListener {
                cancelRemoval(locationId)
            }.create()
    }

    private fun confirmRemoval(locationId: Int) {
        setFragmentResult(
            TAG,
            bundleOf(RESULT_ACTION to ACTION_REMOVE, RESULT_LOCATION_ID to locationId)
        )
    }

    private fun cancelRemoval(locationId: Int) {
        setFragmentResult(
            TAG,
            bundleOf(RESULT_ACTION to ACTION_CANCEL, RESULT_LOCATION_ID to locationId)
        )
    }

    private fun Bundle.getStringOrThrow(key: String): String {
        return getString(key)
            ?: throw IllegalStateException("Can't find a string with the given key: $key")
    }

    private fun Bundle.getIntOrThrow(key: String): Int {
        if (containsKey(key)) {
            return getInt(key)
        } else {
            throw  IllegalStateException("Can't find an int with the given key: $key")
        }
    }

    companion object {
        const val TAG = "RemovalConfirmationDialog"
        private const val ARG_TITLE = "Title"
        private const val ARG_LOCATION_ID = "ItemId"
        const val RESULT_ACTION = "Action"
        const val RESULT_LOCATION_ID = "ItemId"
        const val ACTION_REMOVE = "Remove"
        const val ACTION_CANCEL = "Cancel"

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
