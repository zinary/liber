package com.zinary.liber.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.zinary.liber.MainViewModel
import com.zinary.liber.R
import com.zinary.liber.databinding.FragmentHomeBinding
import com.zinary.liber.enums.MoviesType

class HomeFragment : Fragment() {

    private lateinit var popularMoviesAdapter: MovieListAdapter
    private lateinit var topRatedMoviesAdapter: MovieListAdapter
    private lateinit var nowPlayingMoviesAdapter: MovieListAdapter
    private lateinit var upComingMoviesAdapter: MovieListAdapter

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val mainViewModel by activityViewModels<MainViewModel>()

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

        popularMoviesAdapter = MovieListAdapter(requireContext())
        topRatedMoviesAdapter = MovieListAdapter(requireContext())
        nowPlayingMoviesAdapter = MovieListAdapter(requireContext())
        upComingMoviesAdapter = MovieListAdapter(requireContext())

        binding.popularMoviesRecyclerView.adapter = popularMoviesAdapter
        binding.topRatedMoviesRecyclerView.adapter = topRatedMoviesAdapter
        binding.nowPayingMoviesRecyclerView.adapter = nowPlayingMoviesAdapter
        binding.upcomingMoviesRecyclerView.adapter = upComingMoviesAdapter

        mainViewModel.upcomingMovies.observe(viewLifecycleOwner) { featuredMovies ->
            if (featuredMovies != null) {
                upComingMoviesAdapter.setMovieList(featuredMovies)
            }
        }

        mainViewModel.topRatedMovies.observe(viewLifecycleOwner) { featuredMovies ->
            if (featuredMovies != null) {
                topRatedMoviesAdapter.setMovieList(featuredMovies)
            }
        }

        mainViewModel.popularMovies.observe(viewLifecycleOwner) { featuredMovies ->
            if (featuredMovies != null) {
                popularMoviesAdapter.setMovieList(featuredMovies)
            }
        }

        mainViewModel.nowPlayingMovies.observe(viewLifecycleOwner) { featuredMovies ->
            if (featuredMovies != null) {
                nowPlayingMoviesAdapter.setMovieList(featuredMovies)
                binding.homeLayout.isVisible = true
                hideProgressBar()
            }
        }

        mainViewModel.apiError.observe(viewLifecycleOwner) { errorMessage ->
            hideProgressBar()
            Snackbar.make(requireContext(), binding.root, errorMessage, Snackbar.LENGTH_SHORT)
                .show()
        }

        binding.seeMoreNowPaying.setOnClickListener {
            openMovieListScreen(MoviesType.NOW_PLAYING)
        }

        binding.seeMoreUpcoming.setOnClickListener {
            openMovieListScreen(MoviesType.UPCOMING)
        }

        binding.seeMorePopular.setOnClickListener {
            openMovieListScreen(MoviesType.POPULAR)
        }

        binding.seeMoreTopRated.setOnClickListener {
            openMovieListScreen(MoviesType.TOP_RATED)
        }


    }

    private fun openMovieListScreen(moviesType: MoviesType) {
        val arg = bundleOf("movieType" to moviesType)
        findNavController().navigate(R.id.genreMoviesFragment, arg)
    }

    private fun hideProgressBar() {
        binding.progressCircular.isVisible = false
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.popularMoviesRecyclerView.adapter = null
        binding.topRatedMoviesRecyclerView.adapter = null
        binding.nowPayingMoviesRecyclerView.adapter = null
        binding.upcomingMoviesRecyclerView.adapter = null
        _binding = null
    }
}