package com.zinary.liber.ui.home

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.*
import androidx.recyclerview.widget.GridLayoutManager
import com.zinary.liber.MainViewModel
import com.zinary.liber.databinding.FragmentGenreMoviesBinding
import com.zinary.liber.enums.MoviesType
import kotlinx.coroutines.launch
import java.util.*

class MoviesListFragment : Fragment() {

    private var _binding: FragmentGenreMoviesBinding? = null
    private val binding get() = _binding!!
    private val moviesAdapter = VerticalMovieListAdapter(VerticalMovieListAdapter.ViewType.GRID)
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

        binding.moviesRecyclerView.setHasFixedSize(true)
        binding.moviesRecyclerView.layoutManager  = GridLayoutManager(requireContext(), 3)
        binding.moviesRecyclerView.adapter = moviesAdapter.withLoadStateFooter(
            footer = LoadStateAdapter { moviesAdapter.retry() }
        )

        moviesAdapter.addLoadStateListener { loadState ->

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
                moviesAdapter.submitData(pagingData.filter { it.posterPath != null })
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        binding.moviesRecyclerView.adapter = null
        _binding = null
    }
}