package com.zinary.liber.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.zinary.liber.api.RetrofitInstance
import com.zinary.liber.models.*
import com.zinary.liber.repo.MoviesRepo
import com.zinary.liber.ui.search.SearchPageSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOException

class MovieDetailViewModel : ViewModel() {
    private val movieRepo by lazy { MoviesRepo() }
    val movie: MutableLiveData<Resource<Movie>> = MutableLiveData()

    var apiError = MutableLiveData<String>()

    fun getMovieDetails(id: Int) {
        movie.postValue(Resource.Loading())
        viewModelScope.launch {
            try {
                val response = movieRepo.getMovieDetails(id)
                if (response.isSuccessful) {
                    val movieResponse = response.body()
                    if (movieResponse != null) {
                        movie.postValue(Resource.Success(movieResponse!!))
                    }
                } else {
                    movie.postValue(Resource.Error(response.message()))
                }
            } catch (e: Exception) {
                Log.e("getMovieDetails", e.message, e)
            } catch (e: IOException) {
                apiError.postValue(e.message)
            }
        }
    }

    fun getReviews(movieId: Int): LiveData<PagingData<Review>> {
        return Pager(PagingConfig(pageSize = 20)) {
            ReviewPageSource(movieId)
        }.liveData.cachedIn(viewModelScope)
    }
}