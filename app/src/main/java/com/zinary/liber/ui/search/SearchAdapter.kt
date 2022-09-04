package com.zinary.liber.ui.search


import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.zinary.liber.constants.Constants
import com.zinary.liber.databinding.ItemSearchResultGridBinding
import com.zinary.liber.models.SearchResult
import com.zinary.liber.ui.home.CastDetailActivity
import com.zinary.liber.ui.home.MovieDetailActivity
import com.zinary.liber.utils.loadFromUrl

class SearchAdapter(
    diffCallback: DiffUtil.ItemCallback<SearchResult> = object :
        DiffUtil.ItemCallback<SearchResult>() {
        override fun areItemsTheSame(oldItem: SearchResult, newItem: SearchResult): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: SearchResult, newItem: SearchResult): Boolean {
            return oldItem == newItem
        }
    }
) : PagingDataAdapter<SearchResult, SearchAdapter.SearchResultViewHolder>(diffCallback) {

    inner class SearchResultViewHolder(private val itemBinding: ItemSearchResultGridBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(searchResult: SearchResult) {
            itemBinding.moviePoster.loadFromUrl(
                Constants.BASE_IMAGE_URL + (searchResult.posterPath ?: searchResult.profilePath),
                itemBinding.moviePoster.context
            )

            itemBinding.cardContainer.setOnClickListener {
                val intent: Intent
                when (searchResult.mediaType) {
                    "movie" -> {
                        intent = Intent(it.context, MovieDetailActivity::class.java)
                        intent.putExtra("movieId", searchResult.id)
                        itemBinding.cardContainer.context.startActivity(intent)

                    }
                    "person" -> {
                        intent = Intent(it.context, CastDetailActivity::class.java)
                        intent.putExtra(Constants.CAST_ID, searchResult.id)
                        itemBinding.cardContainer.context.startActivity(intent)
                    } 
                    else -> {
                        Toast.makeText(itemBinding.root.context, "TV Show support coming soon", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchResultViewHolder {
        val moviePosterTileBinding =
            ItemSearchResultGridBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SearchResultViewHolder(moviePosterTileBinding)
    }

    override fun onBindViewHolder(holder: SearchAdapter.SearchResultViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }
}