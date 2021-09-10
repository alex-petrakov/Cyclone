package me.alexpetrakov.cyclone.locations.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import me.alexpetrakov.cyclone.R
import me.alexpetrakov.cyclone.common.presentation.TextResource
import me.alexpetrakov.cyclone.common.presentation.asString
import me.alexpetrakov.cyclone.databinding.FragmentLocationsBinding
import me.alexpetrakov.cyclone.locations.presentation.list.LocationsAdapter
import me.alexpetrakov.cyclone.locations.presentation.list.OnMoveItemCallback
import org.koin.androidx.viewmodel.ext.android.viewModel

class LocationsFragment : Fragment() {

    private val viewModel by viewModel<LocationsViewModel>()

    private var _binding: FragmentLocationsBinding? = null

    private val binding get() = _binding!!

    private val itemTouchHelper: ItemTouchHelper = ItemTouchHelper(
        OnMoveItemCallback().apply {
            onMoveItem = { from, to ->
                locationsAdapter.onMoveItem(from, to)
                true
            }
            onDropItem = { viewModel.onUpdateLocationsOrder(locationsAdapter.currentList) }
        }
    )

    private val locationsAdapter = LocationsAdapter(itemTouchHelper).apply {
        onRemoveItem = { item -> viewModel.onTryToRemoveItem(item) }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLocationsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prepareView()
        observeViewModel()
    }

    private fun prepareView(): Unit = with(binding) {
        toolbar.setOnClickListener { viewModel.onNavigateBack() }
        addFloatingButton.setOnClickListener { viewModel.onAddLocation() }
        recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = locationsAdapter
        }
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    private fun observeViewModel(): Unit = with(viewModel) {
        viewState.observe(viewLifecycleOwner) { render(it) }
        viewEffect.observe(viewLifecycleOwner) { handle(it) }
    }

    private fun render(viewState: ViewState) {
        locationsAdapter.submitList(viewState.locations)
    }

    private fun handle(viewEffect: ViewEffect) {
        when (viewEffect) {
            is ViewEffect.DisplayRemovalConfirmation -> {
                showRemovalConfirmation(viewEffect.confirmationText, viewEffect.idOfItemToBeRemoved)
            }
        }
    }

    private fun showRemovalConfirmation(confirmationText: TextResource, idOfItemToRemove: Int) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(confirmationText.asString(resources))
            .setPositiveButton(R.string.app_action_remove) { _, _ ->
                viewModel.onConfirmItemRemoval(idOfItemToRemove)
            }
            .setNegativeButton(R.string.app_action_cancel) { dialog, _ ->
                dialog.dismiss()
            }.show()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    companion object {
        fun newInstance() = LocationsFragment()
    }
}