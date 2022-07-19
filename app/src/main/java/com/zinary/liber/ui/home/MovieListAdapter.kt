package com.zinary.liber.ui.home

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.zinary.liber.constants.Constants
import com.zinary.liber.databinding.MoviePosterTileBinding
import com.zinary.liber.models.Movie
import com.zinary.liber.utils.loadFromUrl

class MovieListAdapter(private val context: Context) :
    RecyclerView.Adapter<MovieListAdapter.MovieViewHolder>() {

    private val differ = AsyncListDiffer(this, object : DiffUtil.ItemCallback<Movie>() {
        override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem == newItem
        }
    })

    fun setMovieList(movies: List<Movie>) {
        differ.submitList(movies)
    }

    inner class MovieViewHolder(private val itemBinding: MoviePosterTileBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(movie: Movie) {

            itemBinding.moviePoster.loadFromUrl(
                Constants.BASE_IMAGE_URL + movie.posterPath,
                context
            )
            itemBinding.moviePoster.setOnClickListener {
                val intent = Intent(context, MovieDetailActivity::class.java)
                intent.putExtra("movieId", movie.id)
                context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val moviePosterTileBinding =
            MoviePosterTileBinding.inflate(LayoutInflater.from(context), parent, false)
        return MovieViewHolder(moviePosterTileBinding)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }

    override fun getItemCount(): Int = differ.currentList.count()
}