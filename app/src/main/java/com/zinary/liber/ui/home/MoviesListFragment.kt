package com.zinary.liber.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.*
import com.zinary.liber.MainViewModel
import com.zinary.liber.databinding.FragmentGenreMoviesBinding
import com.zinary.liber.enums.MoviesType
import kotlinx.coroutines.launch
import java.util.*


class MoviesListFragment : Fragment() {


    private var _binding: FragmentGenreMoviesBinding? = null
    private val binding get() = _binding!!
    private val mainViewModel by activityViewModels<MainViewModel>()
    private val moviesAdapter = VerticalMovieListAdapter()
    private var movieType: MoviesType? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGenreMoviesBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let { args ->
            movieType = args.get("movieType") as MoviesType
            binding.toolBar.title = movieType?.title
            binding.toolBar.setNavigationOnClickListener {
                requireActivity().onBackPressed()
            }
        }

        binding.moviesRecyclerView.adapter = moviesAdapter

        moviesAdapter.addLoadStateListener { loadState ->
            binding.refreshLayout.isRefreshing = loadState.refresh is LoadState.Loading

            if (loadState.refresh is LoadState.Error) {
                Toast.makeText(
                    requireContext(),
                    (loadState.refresh as LoadState.Error).error.message.toString(),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        Pager(PagingConfig(pageSize = 20)) {
            MovieListPageSource(
                movieType?.key.orEmpty(),
                Locale.getDefault().country,
                true
            )
        }.liveData.cachedIn(lifecycle).observe(viewLifecycleOwner) { pagingData ->
            lifecycleScope.launch {
                moviesAdapter.submitData(pagingData)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.moviesRecyclerView.adapter = null
        _binding = null
    }
}