package com.zinary.liber.ui.home

import android.app.ActivityOptions
import android.content.Intent
import android.graphics.PorterDuff
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.google.android.material.chip.Chip
import com.google.android.material.tabs.TabLayout
import com.zinary.liber.R
import com.zinary.liber.constants.Constants
import com.zinary.liber.constants.Constants.IMAGE_INDEX
import com.zinary.liber.constants.Constants.IMAGE_URLS
import com.zinary.liber.databinding.ActivityMovieDetailBinding
import com.zinary.liber.models.Movie
import com.zinary.liber.models.Resource
import com.zinary.liber.utils.loadFromUrl
import kotlinx.coroutines.launch
import org.json.JSONArray
import java.util.*


class MovieDetailActivity : AppCompatActivity(), ImageAdapter.OnImageClickListener {

    private lateinit var binding: ActivityMovieDetailBinding
    private lateinit var castAdapter: CastAdapter
    private lateinit var videoAdapter: VideoAdapter
    private lateinit var posterAdapter: ImageAdapter
    private lateinit var backDropAdapter: ImageAdapter
    private lateinit var reviewsAdapter: ReviewsAdapter
    private lateinit var recommendedMoviesAdapter: MovieListAdapter

    private val movieDetailViewModel by viewModels<MovieDetailViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMovieDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true);

        getMovieDetails(intent)

        castAdapter = CastAdapter(this, arrayListOf())
        videoAdapter = VideoAdapter()
        posterAdapter = ImageAdapter(this)
        backDropAdapter = ImageAdapter(this)
        recommendedMoviesAdapter = MovieListAdapter(this)
        reviewsAdapter = ReviewsAdapter()

        binding.castRecyclerView.adapter = castAdapter
        binding.castRecyclerView.setHasFixedSize(true)
        binding.postersRecyclerView.adapter = posterAdapter
        binding.postersRecyclerView.setHasFixedSize(true)
        binding.backdropRecyclerView.adapter = backDropAdapter
        binding.backdropRecyclerView.setHasFixedSize(true)
        binding.recommendedMoviesRecyclerView.adapter = recommendedMoviesAdapter
        binding.recommendedMoviesRecyclerView.setHasFixedSize(true)
        binding.reviewRecyclerview.adapter = reviewsAdapter
        binding.videoPager.adapter = videoAdapter

        movieDetailViewModel.apiError.observe(this) { message ->
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }

        movieDetailViewModel.movie.observe(this) { movieResponse ->
            when (movieResponse) {
                is Resource.Success -> {
                    movieResponse.data?.let { movie -> setupUi(movie) }
                    hideProgressBar()
                    binding.appBarLayout.isVisible = true
                    binding.movieDetailsScrollView.isVisible = true
                }
                is Resource.Error -> {
                    hideProgressBar()
                    movieResponse.message?.let { message ->
                        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                        Log.e(this.javaClass.name, "An error occurred: $message")
                    }
                }
                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        }
    }

    private fun getMovieDetails(intent:Intent) {
        val movieId = intent.extras?.get("movieId") as? Int
        if (movieId != null) {
            movieDetailViewModel.getMovieDetails(movieId)
        } else {
            Toast.makeText(this, "Invalid Movie ID", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun handleWatchProviders(providers: Map<String, Any>) {
        if (providers.isNotEmpty()) {
            val locale = Locale.getDefault().country
            val results = providers["results"] as? Map<*, *>
            val countryData = results?.get(locale) as? Map<*, *>
            if (countryData != null) {
                val link = countryData["link"] as? String
                val buy = countryData["buy"] as? JSONArray
                val flatRate = countryData["flatrate"] as? JSONArray
                val rent = countryData["rent"] as? JSONArray
                if (flatRate != null) {
                    Toast.makeText(this, flatRate.toString(), Toast.LENGTH_SHORT).show()
                }
                if (buy != null) {
                    println(buy.toString())
                    Toast.makeText(this, buy.toString(), Toast.LENGTH_SHORT).show()

                }
                if (rent != null) {
                    println(rent.toString())
                    Toast.makeText(this, rent.toString(), Toast.LENGTH_SHORT).show()
                }
            } else {
                val countryData = results?.get("GB") as? Map<*, *>
                println(countryData.toString())
                Toast.makeText(this, providers.toString(), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showProgressBar() {
        binding.progressCircular.isVisible = true
    }

    private fun hideProgressBar() {
        binding.progressCircular.isVisible = false
    }

    private fun setupUi(movie: Movie) {
        val imageUrl = Constants.BASE_IMAGE_URL + movie.posterPath
        binding.movieBanner.apply {
            loadFromUrl(imageUrl, this@MovieDetailActivity, blur = true)
            setColorFilter(getColor(R.color.black), PorterDuff.Mode.SCREEN)
        }

        setToolBarTitle(movie.title)

        binding.moviePoster.loadFromUrl(imageUrl, this)
        binding.title.text = movie.title
        binding.status.text = movie.status
        binding.originalTitle.text = movie.originalTitle
        binding.yearOfRelease.text = movie.releaseDate.split("-").first()
        binding.originalLanguage.text = Locale(movie.originalLanguage).displayLanguage
        binding.runTime.text = getString(R.string.runtime_mins).format(movie.runtime)
        binding.ratingBar.rating = movie.voteAverage.toFloat() / 2

        var overViewClicked = false
        binding.overview.apply {
            text = movie.overview.ifBlank { "Unavailable" }
            setOnClickListener {
                overViewClicked = !overViewClicked
                maxLines = if (overViewClicked) Int.MAX_VALUE else 5
            }
        }

        binding.genreChipGroup.removeAllViews()

        movie.genres.forEach { genre ->
            val chip = Chip(this).apply {
                text = genre.name
                chipBackgroundColor = getColorStateList(R.color.black)
                elevation = 20f
                setTextColor(getColor(R.color.white))
            }

            binding.genreChipGroup.addView(chip)
        }

        castAdapter.setCastList(movie.credits.cast)
        binding.castLayout.isVisible = movie.credits.cast.isNotEmpty()

        val recommendedMovies = movie.recommendations.results
        recommendedMoviesAdapter.setMovieList(recommendedMovies)
        binding.recommendedSection.isVisible = recommendedMovies.isNotEmpty()

        val directors = movie.credits.crew.filter { it.job == "Director" }

        if (directors.size == 1) {
            binding.creators.text = getString(R.string.director)
            binding.creatorsDetails.text = directors.first().name
        } else {
            binding.creators.text = getString(R.string.directors)
            binding.creatorsDetails.text = directors.joinToString { it.name }
        }

        val ytVideoList =
            movie.videos.results.filter { it.site == "YouTube" && it.type == "Trailer" }
        videoAdapter.setVideoList(ytVideoList)
        binding.videoLayout.isVisible = ytVideoList.isNotEmpty()


        val posters = (movie.images.posters)
        val backDrops = (movie.images.backdrops)
        posterAdapter.setMovieImageList(posters)
        backDropAdapter.setMovieImageList(backDrops)
        binding.postersLayout.isVisible = posters.isNotEmpty()
        binding.backDropLayout.isVisible = backDrops.isNotEmpty()

        val chips = arrayListOf<Chip>()

        if (movie.externalIDs.facebookId != null) {
            chips.add(
                createExternalIdChip(
                    "Facebook",
                    "https://www.facebook.com/${movie.externalIDs.facebookId}",
                    R.drawable.ic_facebook
                )
            )
        }

        if (movie.externalIDs.instagramId != null) {
            chips.add(
                createExternalIdChip(
                    "Instagram",
                    "https://www.instagram.com/${movie.externalIDs.instagramId}",
                    R.drawable.ic_instagram
                )
            )
        }
        if (movie.externalIDs.twitterId != null) {
            chips.add(
                createExternalIdChip(
                    "Twitter",
                    "https://www.twitter.com/${movie.externalIDs.twitterId}",
                    R.drawable.ic_twitter
                )
            )
        }

        if (movie.externalIDs.imdbId != null) {
            chips.add(
                createExternalIdChip(
                    "IMDB",
                    "https://www.imdb.com/title/${movie.externalIDs.imdbId}",
                    R.drawable.ic_imdb
                )
            )
        }

        binding.externalLinksChipGroup.removeAllViews()

        chips.forEach { chip ->
            binding.externalLinksChipGroup.addView(chip)
        }

        binding.externalIdSection.isVisible = chips.isNotEmpty()
//        handleWatchProviders(movie.watchProviders)

        movieDetailViewModel.getReviews(movie.id).observe(this) { review ->
            lifecycleScope.launch {
                reviewsAdapter.submitData(review)
            }
        }

        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                binding.movieDetailsScrollView.isVisible = tab?.position == 0
                binding.reviewRecyclerview.isVisible = tab?.position == 1
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) = Unit
            override fun onTabReselected(tab: TabLayout.Tab?) = Unit
        })
    }

    private fun setToolBarTitle(title: String) {
        var isShow = true
        var scrollRange = -1
        binding.appBarLayout.addOnOffsetChangedListener { barLayout, verticalOffset ->
            if (scrollRange == -1) {
                scrollRange = barLayout?.totalScrollRange!!
            }
            if (scrollRange + verticalOffset == 0) {
                binding.collapsingToolBar.title = title
                isShow = true
            } else if (isShow) {
                binding.collapsingToolBar.title = " "
                isShow = false
            }
        }
    }

    private fun createExternalIdChip(name: String, url: String, iconDrawableId: Int? = null): Chip {
        val chip = Chip(this).apply {
            text = name
            chipBackgroundColor = getColorStateList(R.color.black)
            elevation = 20f
            chipIconSize = 50f
            iconStartPadding = 20f
            chipIcon = getDrawable(iconDrawableId ?: R.drawable.ic_baseline_settings_24)
            setTextColor(getColor(R.color.white))
            setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                startActivity(intent)
            }
        }

        return chip
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.recommendedMoviesRecyclerView.adapter = null
        binding.videoPager.adapter = null
        binding.postersRecyclerView.adapter = null
        binding.backdropRecyclerView.adapter = null
        binding.castRecyclerView.adapter = null
    }

    override fun onImageClicked(position: Int, urls: ArrayList<String>) {
        val intent = Intent(this, ImagePreviewActivity::class.java)

        intent.putExtra(IMAGE_INDEX, position)
        intent.putStringArrayListExtra(IMAGE_URLS, urls)
        startActivity(
            intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle()
        )
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        if (intent != null) {
            getMovieDetails(intent)
        }
    }
}