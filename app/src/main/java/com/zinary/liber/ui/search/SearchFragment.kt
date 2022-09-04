package com.zinary.liber.ui.search

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.paging.filter
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.tabs.TabLayout
import com.zinary.liber.MainViewModel
import com.zinary.liber.databinding.FragmentSearchBinding
import kotlinx.coroutines.launch

class SearchFragment : Fragment() {

    private lateinit var moviesAdapter: SearchAdapter

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private val viewModel by activityViewModels<MainViewModel>()
    private var selectedMediaType = "movie"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.searchView.isFocusableInTouchMode = true
        binding.searchView.isFocusable = true
        binding.searchView.requestFocus()
        val inputMethodManager =
            context?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.showSoftInput(binding.searchView, InputMethodManager.SHOW_IMPLICIT)
        binding.searchView.setOnQueryTextListener(object : OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query ?: return false
                if (query.isNotBlank()) {
                    performSearch(query)
                }
                return true
            }

            override fun onQueryTextChange(query: String?): Boolean {
                query ?: return false
                if (query.length >= 2) {
                    performSearch(query)
                }

                if (query.isEmpty()) {
                    performSearch(query)

                    binding.emptyState.isVisible = false
                }
                return true
            }
        })

        moviesAdapter = SearchAdapter()
        binding.moviesRecyclerView.layoutManager =
            GridLayoutManager(requireContext(), 3) // todo figure out size of the layout
        binding.moviesRecyclerView.adapter = moviesAdapter

        moviesAdapter.addLoadStateListener { loadState ->
            if (loadState.refresh is LoadState.Loading) {
                binding.generesChipGroup.isVisible = false
                binding.emptyState.isVisible = false
            }

            if (loadState.refresh is LoadState.NotLoading) {
                binding.resultsLayout.isVisible =
                    binding.searchView.query.isNotBlank() && moviesAdapter.itemCount > 0
                if (loadState.refresh.endOfPaginationReached) {
                    binding.emptyState.isVisible = binding.searchView.query.isNotBlank() && moviesAdapter.itemCount == 0
                }
            }

            if (loadState.refresh is LoadState.Error) {
                Toast.makeText(
                    requireContext(),
                    (loadState.refresh as LoadState.Error).error.message.toString(),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                selectedMediaType = tab?.text?.toString()?.lowercase() ?: "movie"
                moviesAdapter.refresh()
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) = Unit

            override fun onTabReselected(tab: TabLayout.Tab?) {
                binding.moviesRecyclerView.smoothScrollToPosition(0)
            }
        })

        viewModel.genreList.observe(viewLifecycleOwner) { genres ->
            // todo
        }
    }

    private fun performSearch(query: String) {
        viewModel.searchMovies(query).observe(viewLifecycleOwner) { pagingData ->
            lifecycleScope.launch {
                moviesAdapter.submitData(pagingData.filter { it.mediaType == selectedMediaType })
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.searchQuery = ""
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.moviesRecyclerView.adapter = null
        _binding = null
    }
}