package com.zinary.liber.ui.search

import android.app.Activity
import android.graphics.Color
import android.os.Bundle
import android.text.TextPaint
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
import com.magicgoop.tagsphere.OnTagTapListener
import com.magicgoop.tagsphere.item.TagItem
import com.magicgoop.tagsphere.item.TextTagItem
import com.zinary.liber.MainViewModel
import com.zinary.liber.databinding.FragmentSearchBinding
import com.zinary.liber.ui.home.VerticalMovieListAdapter
import kotlinx.coroutines.launch


class SearchFragment : Fragment(), OnTagTapListener {

    private lateinit var moviesAdapter: VerticalMovieListAdapter

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private val viewModel by activityViewModels<MainViewModel>()

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

        moviesAdapter = VerticalMovieListAdapter()
        binding.moviesRecyclerView.adapter = moviesAdapter

        moviesAdapter.addLoadStateListener { loadState ->
            if (loadState.refresh is LoadState.Loading) {
                binding.tagView.isVisible = false
                binding.refreshLayout.isRefreshing = false
            }

            if (loadState.refresh is LoadState.NotLoading && moviesAdapter.itemCount == 0) {
                if (binding.searchView.query.isNotBlank()) {
                    binding.emptyState.isVisible = true
                    binding.tagView.isVisible = false
                } else {
                    binding.emptyState.isVisible = false
                    binding.tagView.isVisible = true
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

        viewModel.genreList.observe(viewLifecycleOwner) { genres ->
            binding.tagView.setOnTagTapListener(this)
            binding.tagView.startAutoRotation()
            binding.tagView.setTextPaint(
                TextPaint().apply {
                    isAntiAlias = true
                    textSize = 50f
                    color = Color.WHITE
                }
            )
            genres.map {
                TextTagItem(text = it.name)
            }.toList().let {
                binding.tagView.addTagList(it)
            }
        }
    }

    override fun onTap(tagItem: TagItem) {
        binding.tagView.stopAutoRotation()
        binding.searchView.setQuery((tagItem as TextTagItem).text, true)
    }

    private fun performSearch(query: String) {
        binding.moviesRecyclerView.scrollToPosition(0)
        viewModel.searchMovies(query).observe(viewLifecycleOwner) { pagingData ->
            lifecycleScope.launch {
                moviesAdapter.submitData(pagingData)
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