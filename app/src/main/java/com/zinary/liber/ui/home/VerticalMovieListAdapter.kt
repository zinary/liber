package com.zinary.liber.ui.home

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.zinary.liber.constants.Constants
import com.zinary.liber.databinding.ItemSearchResultGridBinding
import com.zinary.liber.databinding.MovieListItemBinding
import com.zinary.liber.models.Movie
import com.zinary.liber.models.SearchResult
import com.zinary.liber.utils.loadFromUrl

class VerticalMovieListAdapter(val viewType: ViewType) :
    PagingDataAdapter<Movie, RecyclerView.ViewHolder>(DiffCallBack) {

    enum class ViewType {
        GRID, LIST
    }

    override fun getItemViewType(position: Int): Int {
        return viewType.ordinal
    }

    inner class MovieViewHolder(private val itemBinding: MovieListItemBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(movie: Movie) {
            itemBinding.movieName.text = movie.title
            itemBinding.releaseDate.text = movie.releaseDate
            itemBinding.rating.text = movie.voteAverage.toString()
            itemBinding.overview.text = movie.overview
            itemBinding.moviePoster.loadFromUrl(
                Constants.BASE_IMAGE_URL + movie.posterPath,
                itemBinding.moviePoster.context
            )

            itemBinding.cardContainer.setOnClickListener {
                val intent = Intent(it.context, MovieDetailActivity::class.java)
                intent.putExtra("movieId", movie.id)
                itemBinding.cardContainer.context.startActivity(intent)
            }
        }
    }

    inner class SimpleViewHolder(private val itemBinding: ItemSearchResultGridBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(movie: Movie) {
            itemBinding.moviePoster.loadFromUrl(
                Constants.BASE_IMAGE_URL + (movie.posterPath),
                itemBinding.moviePoster.context
            )
            itemBinding.cardContainer.setOnClickListener {
                val intent = Intent(it.context, MovieDetailActivity::class.java)
                intent.putExtra("movieId", movie.id)
                itemBinding.cardContainer.context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == ViewType.LIST.ordinal) {
            val moviePosterTileBinding =
                MovieListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            MovieViewHolder(moviePosterTileBinding)
        } else {
            val moviePosterTileBinding =
                ItemSearchResultGridBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            SimpleViewHolder(moviePosterTileBinding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        getItem(position)?.let {
            when (holder) {
                is MovieViewHolder -> {
                    holder.bind(it)
                }
                is SimpleViewHolder -> {
                    holder.bind(it)
                }
            }
        }
    }

    object DiffCallBack : DiffUtil.ItemCallback<Movie>() {
        override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem == newItem
        }
    }
}