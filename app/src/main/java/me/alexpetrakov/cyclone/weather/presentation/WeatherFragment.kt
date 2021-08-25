package me.alexpetrakov.cyclone.weather.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import me.alexpetrakov.cyclone.databinding.FragmentWeatherBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class WeatherFragment : Fragment() {

    private val viewModel by viewModel<WeatherViewModel>()

    private var _binding: FragmentWeatherBinding? = null

    private val binding get() = _binding!!

    private val weatherAdapter by lazy { WeatherAdapter(resources) }

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
        recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = weatherAdapter
        }
        swipeToRefreshLayout.apply {
            setProgressViewEndTarget(true, progressViewEndOffset)
        }
    }

    private fun observeViewModel(): Unit = with(viewModel) {
        viewState.observe(viewLifecycleOwner) { viewState -> render(viewState) }
    }

    private fun render(viewState: ViewState): Unit = with(binding) {
        weatherAdapter.submitList(viewState.items)
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    companion object {
        fun newInstance() = WeatherFragment()
    }
}