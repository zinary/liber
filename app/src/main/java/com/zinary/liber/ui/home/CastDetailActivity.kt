package com.zinary.liber.ui.home

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.google.android.material.chip.Chip
import com.zinary.liber.R
import com.zinary.liber.constants.Constants
import com.zinary.liber.databinding.ActivityCastDetailBinding
import com.zinary.liber.models.Person
import com.zinary.liber.utils.Utils
import com.zinary.liber.utils.loadFromUrl

class CastDetailActivity : AppCompatActivity() , ImageAdapter.OnImageClickListener {

    private lateinit var binding: ActivityCastDetailBinding
    private lateinit var recommendedMoviesAdapter: MovieListAdapter
    private lateinit var photosAdapter: ImageAdapter

    private val viewModel by viewModels<CastDetailViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCastDetailBinding.inflate(layoutInflater)

        setContentView(binding.root)
        intent?.extras?.let { args ->
            val castId = args.getInt(Constants.CAST_ID)
            viewModel.getPersonDetails(castId)
        }

        binding.closeButton.setOnClickListener { onBackPressed() }
        recommendedMoviesAdapter = MovieListAdapter(this)
        photosAdapter = ImageAdapter(this)

        viewModel.person.observe(this) { person ->
            setupUi(person)
        }
    }

    private fun setupUi(person: Person) {

        binding.image.loadFromUrl(Constants.BASE_IMAGE_URL + person.profilePath, this)
        binding.moviesBy.text = "Movies featuring ${person.name}"
        binding.profileName.text = person.name
        binding.biography.text = person.biography

        binding.photosRecyclerView.adapter = photosAdapter
        photosAdapter.setMovieImageList(person.images.profiles)
        binding.photosLayout.isVisible = person.images.profiles.isNotEmpty()

        binding.castMoviesRecyclerView.adapter = recommendedMoviesAdapter
        recommendedMoviesAdapter.setMovieList(person.credits.cast)
        binding.moviesSection.isVisible = person.images.profiles.isNotEmpty()

        var clicked = false
        binding.biography.setOnClickListener {
            clicked = !clicked
            binding.biography.maxLines = if (clicked) Int.MAX_VALUE else 5
            binding.biography.animate()
        }

        val chips = arrayListOf<Chip>()

        if (person.externalIDs.facebookId != null) {
            chips.add(
                Utils.createExternalIdChip(
                    this,
                    "Facebook",
                    "https://www.facebook.com/${person.externalIDs.facebookId}",
                    R.drawable.ic_facebook
                )
            )
        }

        if (person.externalIDs.instagramId != null) {
            chips.add(
                Utils.createExternalIdChip(
                    this,
                    "Instagram",
                    "https://www.instagram.com/${person.externalIDs.instagramId}",
                    R.drawable.ic_instagram
                )
            )
        }
        if (person.externalIDs.twitterId != null) {

            chips.add(
                Utils.createExternalIdChip(
                    this,
                    "Twitter",
                    "https://www.twitter.com/${person.externalIDs.twitterId}",
                    R.drawable.ic_twitter
                )
            )
        }
        if (person.externalIDs.imdbId != null) {

            chips.add(
                Utils.createExternalIdChip(
                    this,
                    "IMDB",
                    "https://www.imdb.com/title/${person.externalIDs.imdbId}",
                    R.drawable.ic_imdb
                )
            )
        }
        binding.externalLinksChipGroup.removeAllViews()
        chips.forEach { chip ->
            binding.externalLinksChipGroup.addView(chip)
        }

        binding.externalIdSection.isVisible = chips.isNotEmpty()
    }

    override fun onImageClicked(position: Int, urls: ArrayList<String>) {
        val intent = Intent(this, ImagePreviewActivity::class.java)
        intent.putExtra(Constants.IMAGE_INDEX, position)
        intent.putStringArrayListExtra(Constants.IMAGE_URLS, urls)
        startActivity(
            intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle()
        )
    }
}