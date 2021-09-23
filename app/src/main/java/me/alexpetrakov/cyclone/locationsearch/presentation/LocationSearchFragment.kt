package me.alexpetrakov.cyclone.locationsearch.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import me.alexpetrakov.cyclone.common.presentation.extensions.focusAndShowKeyboard
import me.alexpetrakov.cyclone.common.presentation.extensions.hideKeyboard
import me.alexpetrakov.cyclone.databinding.FragmentLocationSearchBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class LocationSearchFragment : Fragment() {

    private val viewModel by viewModel<LocationSearchViewModel>()

    private var _binding: FragmentLocationSearchBinding? = null

    private val binding get() = _binding!!

    private val resultsAdapter = ResultsAdapter().apply {
        onClickItem = { clickedItem -> viewModel.onAddSearchResultToSavedLocations(clickedItem) }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLocationSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prepareView(savedInstanceState)
        observeViewModel()
    }

    private fun prepareView(savedInstanceState: Bundle?): Unit = with(binding) {
        toolbar.setNavigationOnClickListener { viewModel.onNavigateBack() }
        progressIndicator.setVisibilityAfterHide(View.GONE)
        recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = resultsAdapter
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                        hideKeyboard()
                    }
                }
            })
        }
        queryEditText.setOnEditorActionListener { _, actionId, _ ->
            when (actionId) {
                EditorInfo.IME_ACTION_SEARCH -> {
                    hideKeyboard()
                    viewModel.onQueryChanged(queryEditText.text.toString())
                    true
                }
                else -> false
            }
        }
        if (savedInstanceState == null) {
            queryEditText.post { queryEditText.focusAndShowKeyboard() }
        }
    }

    private fun observeViewModel(): Unit = with(viewModel) {
        viewState.observe(viewLifecycleOwner) { render(it) }
    }

    private fun render(viewState: ViewState): Unit = with(binding) {
        if (viewState.isLoading) progressIndicator.show() else progressIndicator.hide()
        when (viewState) {
            is ViewState.Empty -> {
                emptyView.isVisible = true
                errorView.isVisible = false
                recyclerView.isVisible = false
            }
            is ViewState.Content -> {
                emptyView.isVisible = false
                errorView.isVisible = false
                recyclerView.isVisible = true
                renderContent(viewState)
            }
            is ViewState.Error -> {
                emptyView.isVisible = false
                errorView.isVisible = true
                recyclerView.isVisible = false
            }
        }
    }

    private fun renderContent(viewState: ViewState.Content) {
        resultsAdapter.submitList(viewState.searchResults)
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    companion object {
        fun newInstance(): LocationSearchFragment {
            return LocationSearchFragment()
        }
    }
}
