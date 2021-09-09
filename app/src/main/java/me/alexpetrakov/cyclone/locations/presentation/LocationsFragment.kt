package me.alexpetrakov.cyclone.locations.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import me.alexpetrakov.cyclone.databinding.FragmentLocationsBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class LocationsFragment : Fragment() {

    private val viewModel by viewModel<LocationsViewModel>()

    private var _binding: FragmentLocationsBinding? = null

    private val binding get() = _binding!!

    private val locationsAdapter = LocationsAdapter()

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
    }

    private fun observeViewModel(): Unit = with(viewModel) {
        viewState.observe(viewLifecycleOwner) { render(it) }
    }

    private fun render(viewState: ViewState): Unit = with(binding) {
        locationsAdapter.submitList(viewState.locations)
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    companion object {
        fun newInstance() = LocationsFragment()
    }
}