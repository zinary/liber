package com.zinary.liber.ui.search


import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.zinary.liber.api.RetrofitInstance
import com.zinary.liber.models.SearchResult

class SearchPageSource(
    private val query: String,
) : PagingSource<Int, SearchResult>() {

    override suspend fun load(
        params: LoadParams<Int>
    ): LoadResult<Int, SearchResult> {

        return try {
            // Start refresh at page 1 if undefined.
            val nextPageNumber = params.key ?: 1
            val response = RetrofitInstance.moviesAPI.searchMovies(query, nextPageNumber)
            Log.d("Called API", response.raw().toString())
            val body = response.body()
            val results = body?.results ?: arrayListOf()
            val totalPages = body?.totalPages ?: 0

            LoadResult.Page(
                data = results,
                prevKey = null, // Only paging forward.
                nextKey = if (nextPageNumber <= totalPages) nextPageNumber + 1 else null
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, SearchResult>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}