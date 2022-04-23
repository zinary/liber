package com.zinary.liber.ui.home

import android.graphics.PorterDuff
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.zinary.liber.R
import com.zinary.liber.constants.Constants
import com.zinary.liber.databinding.ActivityMovieDetailBinding
import com.zinary.liber.models.Movie
import com.zinary.liber.utils.loadFromUrl


class MovieDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMovieDetailBinding
    private lateinit var castAdapter: CastAdapter
    private lateinit var videoAdapter: VideoAdapter
    private val movieDetailViewModel by viewModels<MovieDetailViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMovieDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val movieId = intent.extras?.get("movieId") as? Int
        if (movieId != null) {
            movieDetailViewModel.getMovieDetails(movieId)
            movieDetailViewModel.getVideos(movieId)
            movieDetailViewModel.getCredits(movieId)
        } else {
            Toast.makeText(this, "Invalid Movie ID", Toast.LENGTH_SHORT).show()
            finish()
        }

        binding.closeButton.setOnClickListener { onBackPressed() }
        binding.likeButton.setOnClickListener {   }

        castAdapter = CastAdapter(this, arrayListOf())
        videoAdapter = VideoAdapter(this, arrayListOf())

        binding.castRecyclerView.adapter = castAdapter
        binding.videoRecyclerView.adapter = videoAdapter

        movieDetailViewModel.movie.observe(this) { movie ->
            setupUi(movie)
            binding.progressCircular.isVisible = false
            binding.movieDetailsScrollView.isVisible = true
        }

        movieDetailViewModel.castList.observe(this) { cast ->
            binding.castLayout.isVisible = cast.isNotEmpty()
            castAdapter.setCastList(cast)
        }

        movieDetailViewModel.crewList.observe(this) { cast ->
            val directors = cast.filter { it.job == "Director" }

            if (directors.size == 1) {
                binding.creators.text = "Director"
                binding.creatorsDetails.text = directors.first().name
            } else {
                binding.creators.text = "Directors"
                binding.creatorsDetails.text = directors.joinToString { it.name }
            }
        }


        movieDetailViewModel.videoList.observe(this) { videoList ->
            val ytVideoList = videoList.filter { it.site == "YouTube" }
            binding.videoLayout.isVisible = ytVideoList.isNotEmpty()
            videoAdapter.setVideoList(ytVideoList)
        }
    }

    private fun setupUi(movie: Movie) {

        val imageUrl = Constants.BASE_IMAGE_URL + movie.posterPath
        binding.movieBanner.apply {
            loadFromUrl(imageUrl, this@MovieDetailActivity, true)
            setColorFilter(getColor(R.color.black), PorterDuff.Mode.SCREEN)
        }

        binding.moviePoster.loadFromUrl(imageUrl, this)
        binding.title.text = movie.title
        binding.overview.text = movie.overview
        binding.yearOfRelease.text = movie.releaseDate.split("-").first()
        binding.genre.text = movie.genres.subList(0, 1).joinToString { it.name }
        binding.runTime.text = getString(R.string.runtime_mins).format(movie.runtime)
        binding.rating.text =
            if (movie.voteAverage > 0.0) movie.voteAverage.toString() else "N/A"
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> onBackPressed()
        }
        return super.onOptionsItemSelected(item)

    }

}