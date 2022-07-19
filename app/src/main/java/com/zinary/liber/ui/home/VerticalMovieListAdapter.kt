package com.zinary.liber.ui.home

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.zinary.liber.constants.Constants
import com.zinary.liber.databinding.MovieListItemBinding
import com.zinary.liber.models.Movie
import com.zinary.liber.utils.loadFromUrl

class VerticalMovieListAdapter(
    diffCallback: DiffUtil.ItemCallback<Movie> = object : DiffUtil.ItemCallback<Movie>() {
        override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem == newItem
        }
    }
) : PagingDataAdapter<Movie, VerticalMovieListAdapter.MovieViewHolder>(diffCallback) {

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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val moviePosterTileBinding =
            MovieListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieViewHolder(moviePosterTileBinding)
    }

    override fun onBindViewHolder(holder: VerticalMovieListAdapter.MovieViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }
}