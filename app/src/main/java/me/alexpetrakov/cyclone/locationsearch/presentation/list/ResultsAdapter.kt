package me.alexpetrakov.cyclone.locationsearch.presentation.list

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import me.alexpetrakov.cyclone.common.presentation.extensions.withAdapterPosition
import me.alexpetrakov.cyclone.databinding.ItemSearchResultBinding
import me.alexpetrakov.cyclone.locationsearch.presentation.SearchResultUiItem

class ResultsAdapter(
    private val onItemClick: (SearchResultUiItem) -> Unit
) : RecyclerView.Adapter<ResultsAdapter.ViewHolder>() {

    private var items: List<SearchResultUiItem> = emptyList()

    override fun getItemCount() = items.size

    @SuppressLint("NotifyDataSetChanged") // TODO: Fix once search results have stable IDs
    fun submitList(searchResults: List<SearchResultUiItem>) {
        items = searchResults
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemSearchResultBinding.inflate(inflater, parent, false)
        return ViewHolder(binding) { position ->
            onItemClick.invoke(items[position])
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    class ViewHolder(
        private val binding: ItemSearchResultBinding,
        private val onClick: (Int) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                withAdapterPosition { position -> onClick(position) }
            }
        }

        fun bind(searchResult: SearchResultUiItem): Unit = with(binding) {
            titleTextView.text = searchResult.placeName
            subtitleTextView.text = searchResult.countryName
        }
    }

}
