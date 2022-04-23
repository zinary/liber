package com.zinary.liber.ui.home

import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.SnapHelper
import com.google.android.material.chip.Chip
import com.zinary.liber.MainViewModel
import com.zinary.liber.R
import com.zinary.liber.constants.Constants
import com.zinary.liber.databinding.FragmentHomeBinding
import com.zinary.liber.enums.MoviesType
import com.zinary.liber.models.Movie
import org.imaginativeworld.whynotimagecarousel.listener.CarouselListener
import org.imaginativeworld.whynotimagecarousel.model.CarouselItem
import java.util.*

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val homeViewModel by activityViewModels<HomeViewModel>()
    private val mainViewModel by activityViewModels<MainViewModel>()
    private lateinit var adapter: MovieListAdapter
    private val binding get() = _binding!!
    private var timer = Timer()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainViewModel.getMovies(MoviesType.POPULAR, mainViewModel.movies)
        mainViewModel.getMovies(MoviesType.UPCOMING, mainViewModel.featuredMovies)
        mainViewModel.getGenres()

        adapter = MovieListAdapter(requireContext(), arrayListOf())

        binding.moviesRecyclerView.adapter = adapter

        mainViewModel.movies.observe(viewLifecycleOwner) {
            if (it != null) {
                adapter.setMovieList(it)
            }
        }

        mainViewModel.featuredMovies.observe(viewLifecycleOwner) { featuredMovies ->
            if (featuredMovies != null) {
                setupCarousel(featuredMovies)
            }
        }

        mainViewModel.genreList.observe(viewLifecycleOwner) {
            it?.forEachIndexed { index, item ->
                val chip = Chip(requireContext()).apply {
                    text = item.name
                    if (index == 0) {
                        chipBackgroundColor = ColorStateList.valueOf(context.getColor(R.color.pink))
                    }
                }
                binding.genreChipGroup.addView(chip)
            }
        }

        mainViewModel.apiError.observe(viewLifecycleOwner) {
            if (it != null) {
                binding.errorMessage.text = it
            }
        }

    }

    private fun setupCarousel(featuredMovies: List<Movie>) {
        binding.carousel.registerLifecycle(viewLifecycleOwner)
        val list = featuredMovies.map {
            CarouselItem(
                imageUrl = Constants.BASE_IMAGE_URL + it.backdropPath,
                caption = it.title,
                headers = mapOf(it.title to it.originalLanguage)
            )
        }
        // Carousel listener
        binding.carousel.carouselListener = object : CarouselListener {
            override fun onClick(position: Int, carouselItem: CarouselItem) {
                val intent = Intent(context, MovieDetailActivity::class.java)
                intent.putExtra("movieId", featuredMovies[position].id)

                startActivity(intent)
            }
        }

        binding.carousel.setData(list)
    }

    override fun onStop() {
        super.onStop()
        timer.cancel()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}