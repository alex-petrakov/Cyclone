package me.alexpetrakov.cyclone.locations.presentation

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import me.alexpetrakov.cyclone.common.presentation.TextResource
import me.alexpetrakov.cyclone.common.presentation.asString
import me.alexpetrakov.cyclone.common.presentation.extensions.extendBottomMarginWithSystemInsets
import me.alexpetrakov.cyclone.common.presentation.extensions.extendBottomPaddingWithSystemInsets
import me.alexpetrakov.cyclone.databinding.FragmentLocationsBinding
import me.alexpetrakov.cyclone.locations.presentation.dialogs.RemovalConfirmationDialog
import me.alexpetrakov.cyclone.locations.presentation.list.LocationsAdapter
import me.alexpetrakov.cyclone.locations.presentation.list.OnMoveItemCallback

@AndroidEntryPoint
class LocationsFragment : Fragment() {

    private val viewModel by viewModels<LocationsViewModel>()

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

    private val locationsAdapter = LocationsAdapter(
        itemTouchHelper,
        onItemClick = { location -> viewModel.onSelectLocation(location) },
        onRemoveButtonClick = { location -> viewModel.onTryToRemoveLocation(location) }
    )

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
        ViewCompat.setOnApplyWindowInsetsListener(collapsingToolbarLayout, null)
        toolbar.setOnClickListener { viewModel.onNavigateBack() }
        addFloatingButton.apply {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                extendBottomMarginWithSystemInsets()
            }
            setAnimateShowBeforeLayout(true)
            setOnClickListener { viewModel.onAddLocation() }
        }
        recyclerView.apply {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                extendBottomPaddingWithSystemInsets()
            }
            clipToPadding = false
            layoutManager = LinearLayoutManager(requireContext())
            adapter = locationsAdapter
        }
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    private fun observeViewModel(): Unit = with(viewModel) {
        viewState.observe(viewLifecycleOwner) { render(it) }
        viewEffect.observe(viewLifecycleOwner) { handle(it) }
    }

    private fun render(viewState: ViewState): Unit = with(binding) {
        locationsAdapter.submitList(viewState.locations)
        if (viewState.addActionIsAvailable) {
            addFloatingButton.show()
        } else {
            addFloatingButton.hide()
        }
    }

    private fun handle(viewEffect: ViewEffect) {
        when (viewEffect) {
            is ViewEffect.DisplayRemovalConfirmation -> {
                showRemovalConfirmation(viewEffect.locationId, viewEffect.confirmationText)
            }
        }
    }

    private fun showRemovalConfirmation(locationId: Int, confirmationText: TextResource) {
        RemovalConfirmationDialog.newInstance(locationId, confirmationText.asString(resources))
            .show(childFragmentManager, RemovalConfirmationDialog.TAG)
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    companion object {
        fun newInstance() = LocationsFragment()
    }
}