package me.alexpetrakov.cyclone.weather.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import me.alexpetrakov.cyclone.common.presentation.asString
import me.alexpetrakov.cyclone.databinding.FragmentWeatherBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class WeatherFragment : Fragment() {

    private val viewModel by viewModel<WeatherViewModel>()

    private var _binding: FragmentWeatherBinding? = null

    private val binding get() = _binding!!

    private val weatherAdapter = WeatherAdapter()

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

        errorView.retryButton.setOnClickListener { viewModel.onRetryAfterFailure() }
    }

    private fun observeViewModel(): Unit = with(viewModel) {
        toolbarViewState.observe(viewLifecycleOwner) { renderToolbar(it) }
        weatherViewState.observe(viewLifecycleOwner) { renderWeather(it) }
    }

    private fun renderToolbar(viewState: ToolbarViewState): Unit = with(binding) {
        toolbar.title = viewState.title.asString(resources)
    }

    private fun renderWeather(viewState: WeatherViewState): Unit = with(binding) {
        when (viewState) {
            WeatherViewState.Loading -> {
                contentView.root.isVisible = false
                errorView.root.isVisible = false
                progressIndicator.show()
            }
            is WeatherViewState.Content -> {
                contentView.root.isVisible = true
                errorView.root.isVisible = false
                progressIndicator.hide()
                renderContent(viewState)
            }
            is WeatherViewState.Error -> {
                contentView.root.isVisible = false
                errorView.root.isVisible = true
                progressIndicator.hide()
            }

        }
    }

    private fun renderContent(viewState: WeatherViewState.Content) = with(binding.contentView) {
        if (swipeToRefreshLayout.isRefreshing != viewState.isRefreshing) {
            swipeToRefreshLayout.isRefreshing = viewState.isRefreshing
        }
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