package com.zinary.liber.ui.home

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.zinary.liber.api.RetrofitInstance
import com.zinary.liber.models.Movie

class MovieListPageSource(
    private val type: String,
    private val region: String,
    private val adult: Boolean
) : PagingSource<Int, Movie>() {

    override suspend fun load(
        params: LoadParams<Int>
    ): LoadResult<Int, Movie> {

        return try {
            // Start refresh at page 1 if undefined.
            val nextPageNumber = params.key ?: 1
            val response = RetrofitInstance.moviesAPI.getMovies(type, region, adult, nextPageNumber)
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

    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}