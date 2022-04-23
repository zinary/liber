package com.zinary.liber.ui.home

import android.annotation.SuppressLint
import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.zinary.liber.MainActivity
import com.zinary.liber.constants.Constants
import com.zinary.liber.databinding.MoviePosterTileBinding
import com.zinary.liber.models.Movie

class MovieListAdapter(private val context: Context, private var movieList: List<Movie>) :
    RecyclerView.Adapter<MovieListAdapter.MovieViewHolder>() {

    @SuppressLint("NotifyDataSetChanged")
    fun setMovieList(movies: List<Movie>) {
        movieList = movies
        notifyDataSetChanged()
    }

    inner class MovieViewHolder(private val itemBinding: MoviePosterTileBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(movie: Movie) {
            Glide.with(context)
                .load(Constants.BASE_IMAGE_URL + movie.posterPath)
                .into(itemBinding.moviePoster)
            itemBinding.moviePoster.setOnClickListener {
                val intent = Intent(context, MovieDetailActivity::class.java)
                intent.putExtra("movieId", movie.id)
//                val options = ActivityOptions
//                    .makeSceneTransitionAnimation((context as MainActivity), itemBinding.posterContainer, "image")
                // start the new activity
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
        holder.bind(movieList[position])
    }

    override fun getItemCount(): Int = movieList.size
}