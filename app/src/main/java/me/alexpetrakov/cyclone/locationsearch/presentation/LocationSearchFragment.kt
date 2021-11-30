package me.alexpetrakov.cyclone.locationsearch.presentation

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import me.alexpetrakov.cyclone.common.presentation.MaterialZAxisTransition
import me.alexpetrakov.cyclone.common.presentation.extensions.extendBottomPaddingWithSystemInsets
import me.alexpetrakov.cyclone.common.presentation.extensions.focusAndShowKeyboard
import me.alexpetrakov.cyclone.common.presentation.extensions.hideKeyboard
import me.alexpetrakov.cyclone.databinding.FragmentLocationSearchBinding
import me.alexpetrakov.cyclone.locationsearch.presentation.list.ResultsAdapter

@AndroidEntryPoint
class LocationSearchFragment : Fragment() {

    private val viewModel by viewModels<LocationSearchViewModel>()

    private var _binding: FragmentLocationSearchBinding? = null

    private val binding get() = _binding!!

    private val resultsAdapter = ResultsAdapter(
        onItemClick = { clickedItem -> viewModel.onAddSearchResultToSavedLocations(clickedItem) }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MaterialZAxisTransition.setupOriginAndDestination(this)
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
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                extendBottomPaddingWithSystemInsets()
            }
            clipToPadding = false
            layoutManager = LinearLayoutManager(requireContext())
            adapter = resultsAdapter
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                        viewModel.onScrollResults()
                    }
                }
            })
        }
        queryEditText.apply {
            setOnEditorActionListener { _, actionId, _ ->
                when (actionId) {
                    EditorInfo.IME_ACTION_SEARCH -> {
                        viewModel.onPerformSearch()
                        true
                    }
                    else -> false
                }
            }
            doAfterTextChanged { text -> viewModel.onQueryTextChanged(text.toString()) }
        }
        if (savedInstanceState == null) {
            queryEditText.post { queryEditText.focusAndShowKeyboard() }
        }
    }

    private fun observeViewModel(): Unit = with(viewModel) {
        searchResultsViewState.observe(viewLifecycleOwner) { render(it) }
        queryViewState.observe(viewLifecycleOwner) { renderQuery(it) }
        viewEffect.observe(viewLifecycleOwner) { handle(it) }
    }

    private fun render(viewState: SearchResultsViewState): Unit = with(binding) {
        if (viewState.isLoading) progressIndicator.show() else progressIndicator.hide()
        when (viewState) {
            is SearchResultsViewState.Empty -> {
                emptyView.isVisible = true
                errorView.isVisible = false
                recyclerView.isVisible = false
            }
            is SearchResultsViewState.Content -> {
                emptyView.isVisible = false
                errorView.isVisible = false
                recyclerView.isVisible = true
                renderContent(viewState)
            }
            is SearchResultsViewState.Error -> {
                emptyView.isVisible = false
                errorView.isVisible = true
                recyclerView.isVisible = false
            }
        }
    }

    private fun renderQuery(query: String): Unit = with(binding) {
        if (queryEditText.text.toString() == query) {
            return
        }
        queryEditText.apply {
            setText(query)
            setSelection(query.length)
        }
    }

    private fun renderContent(viewState: SearchResultsViewState.Content) {
        resultsAdapter.submitList(viewState.searchResults)
    }

    private fun handle(effect: ViewEffect) {
        when (effect) {
            ViewEffect.HIDE_KEYBOARD -> hideKeyboard()
        }
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
